using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Runtime.InteropServices;
using System.Runtime.InteropServices.Marshalling;
using System.Security.Cryptography;
using System.Text;
using System.Text.Json.Nodes;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class Engine
    {
        public struct Result
        {
            public DTO dto;
            public bool flag;
        }
        public Result CheckMove(Board board, int[] from, int[] to)
        {
            DTO dto = new DTO();

            Result r = new Result();
            r.flag = false;

            Piece? selected = board.board[from[0], from[1]] != null ? (Piece)board.board[from[0], from[1]] : null;
            if (selected == null) return r;
            if (from[0] == to[0] && from[1] == to[1]) return r;
            object destItem = board.board[to[0], to[1]];
            if (destItem is Piece)
            {
                Piece desetPiece = (Piece)destItem;
                if (desetPiece.color == selected.color) return r;
            }
            // check castle
            if (selected.type == PieceTypes.King && Math.Abs(from[1] - to[1]) == 2)
            {
                bool canCastle = CanCastle(selected, board, from, to);
                if (!canCastle) return r;
                // if can castle modify dto object
                int diff = to[1] - from[1]; 
                dto.moved2 = diff < 0 ? (Piece)board.board[from[0], 0] : (Piece)board.board[from[0], 7];
                dto.from2 = diff < 0 ? [from[0], 0] : [from[0], 7];
                dto.to2 = diff < 0 ? [from[0], 3] : [from[0], 5];
            }
            // check normal move
            else
            {
                if (!CheckDirection(selected, from, to, (Piece)destItem)) return r;
                if (!CheckBlock(selected, from, to, board)) return r;
            }




            // creat dto

            dto.moved1 = selected;
            dto.from1 = from;
            dto.to1 = to;
            dto.moved1type = selected.type;
            dto.captured = (Piece)destItem;

            r.dto = dto;
            // temp  move
            MovePiece(dto, board);


            // check king is safe
            int[] kingPosition = getKingCoord(selected.color, board);
            bool flag = isSquareSafe(kingPosition, enemyColor: selected.color == Color.White ? Color.Black : Color.White, board);
            if (flag)
            {
                r.flag = true;
            };
            // 
            ReMove(dto, board);





            return r;
        }
        public bool CheckDirection(Piece p, int[] from, int[] to, Piece? destitem)
        {
            int diffrow = from[0] - to[0];
            int diffcol = from[1] - to[1];
            if (p.type == PieceTypes.Horse)
            {
                int diff = (int)(Math.Pow(diffcol, 2) + Math.Pow(diffrow, 2));
                if (diff == 5) return true;
            }
            else if (p.type == PieceTypes.Bishop)
            {
                return Math.Abs(diffcol) == Math.Abs(diffrow);
            }
            else if (p.type == PieceTypes.Rook)
            {
                return diffrow != 0 && diffcol == 0 || diffrow == 0 && diffcol != 0;
            }
            else if (p.type == PieceTypes.Queen)
            {
                return (diffrow != 0 && diffcol == 0 || diffrow == 0 && diffcol != 0) || Math.Abs(diffcol) == Math.Abs(diffrow);
            }
            else if (p.type == PieceTypes.King)
            {
                return Math.Abs(diffcol) <= 1 && Math.Abs(diffrow) <= 1;
            }
            else if (p.type == PieceTypes.Pawn)
            {
                if (p.color == Color.Black && diffrow >= 0 || p.color == Color.White && diffrow <= 0) return false;
                if (Math.Abs(diffrow) > 2 || Math.Abs(diffcol) > 1 || (Math.Abs(diffrow) == 2 && diffcol != 0)) return false;
                if (p.moveCounter != 0 && Math.Abs(diffrow) == 2) return false;
                if (diffcol != 0 && destitem == null) return false;
                return true;
            }
            return false;


        }

        public bool CheckBlock(Piece p, int[] from, int[] to, Board board)
        {
            if (p.type == PieceTypes.Horse) return true;
            int diffrow = to[0] - from[0];
            int diffcol = to[1] - from[1];
            int steprow = diffrow < 0 ? -1 : 1;
            int stepcol = diffcol < 0 ? -1 : 1;
            if (diffrow == 0) steprow = 0;
            if (diffcol == 0) stepcol = 0;
            int diff = Math.Max(Math.Abs(diffrow), Math.Abs(diffcol)); // iteration count
            for (int i = 0, r = from[0] + steprow, c = from[1] + stepcol; i < diff - 1; i++, r += steprow, c += stepcol)
            {
                if (board.board[r, c] != null) return false;
            }
            if (p.type == PieceTypes.Pawn && diffcol == 0 && board.board[to[0], to[1]] != null) return false;

            return true;
        }

        public void MovePiece(DTO dto, Board board)
        {
            board.board[dto.to1[0], dto.to1[1]] = dto.moved1;
            board.board[dto.from1[0], dto.from1[1]] = null;

            dto.moved1.moveCounter++;

            if (dto.moved2 != null)
            {

                board.board[dto.to2[0], dto.to2[1]] = dto.moved2;
                board.board[dto.from2[0], dto.from2[1]] = null;

                dto.moved2.moveCounter++;
            }




        }
        public void ReMove(DTO dto, Board board)
        {
            board.board[dto.to1[0], dto.to1[1]] = dto.captured;
            board.board[dto.from1[0], dto.from1[1]] = dto.moved1;
            // repromote pawn if necessasry
            dto.moved1.type = dto.moved1type;
            dto.moved1.moveCounter--;

            if (dto.moved2 != null)
            {
                board.board[dto.to2[0], dto.to2[1]] = null;
                board.board[dto.from2[0], dto.from2[1]] = dto.moved2;
                dto.moved2.moveCounter--;
            }
        }

        public int[] getKingCoord(Color color, Board board)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Piece? p = (Piece)board.board[i, j];
                    if (p != null && p.type == PieceTypes.King && p.color == color)
                    {
                        return [i, j];
                    }
                }
            }
            return null;
        }
        public bool isSquareSafe(int[] squre, Color enemyColor, Board board)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Piece? pieceFromBoard = (Piece)board.board[i, j];
                    if (pieceFromBoard == null || pieceFromBoard.color != enemyColor) continue;
                    Piece? destItem = (Piece)board.board[squre[0], squre[1]];
                    bool flag = CheckDirection(pieceFromBoard, [i, j], squre, destItem) && CheckBlock(pieceFromBoard, [i, j], squre, board);
                    if (flag) return false;
                }
            }
            return true;
        }

        public bool CanCastle(Piece selected, Board board, int[] from, int[] to)
        {
            if (!selected.canCastle || selected.moveCounter != 0) return false;
            int diff = to[1] - from[1];
            int step = diff < 0 ? -1 : 1;
            // big caste
            if (diff < 0)
            {
                // is rook there and move Counter == 0
                Piece? rook = (Piece)board.board[from[0], 0];
                if (rook == null || rook.moveCounter !=0 ) return false;


                for (int i = 0, r = from[0] , c = from[1] + step; i < 3; i++, c += step)
                {
                    // check square is empyt
                    if (board.board[r, c] != null) return false;
                    // chewck squar is safe
                    if (!isSquareSafe([r, c - step], enemyColor: selected.color == Color.White ? Color.Black : Color.White, board)) return false;
                }
            }
            // small castel
            else
            {
                // is rook there and move Counter == 0
                Piece? rook = (Piece)board.board[from[0], 7];
                if (rook == null || rook.moveCounter != 0) return false;
                for (int i = 0, r = from[0], c = from[1] + step; i < 3; i++, c += step)
                {
                    // check square is empyt
                    if (i < 2)
                    {
                        if (board.board[r, c] != null) return false;
                    }
                    // chewck squar is safe
                    if (!isSquareSafe([r, c - step], enemyColor: selected.color == Color.White ? Color.Black : Color.White, board)) return false;
                }
            }

            return true;
        }

        public void PromotePawn(Board board)
        {
            for (int i = 0;i<8;i++)
            {
                if (board.board[0,i] is Piece)
                {
                    Piece piece = (Piece)board.board[0,i];
                    if(piece.type == PieceTypes.Pawn) piece.type = PieceTypes.Queen;
                }
                if (board.board[7, i] is Piece)
                {
                    Piece piece = (Piece)board.board[7, i];
                    if (piece.type == PieceTypes.Pawn) piece.type = PieceTypes.Queen;
                }
            }
        }

    }
}
