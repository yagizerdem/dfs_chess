using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class Board
    {
        public object[,] board;

        public Board()
        {
            PieceTypes[] pieceTypes = [PieceTypes.Rook, PieceTypes.Horse, PieceTypes.Bishop, PieceTypes.Queen, PieceTypes.King, PieceTypes.Bishop, PieceTypes.Horse, PieceTypes.Rook];
            this.board = new object[8, 8];
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (i == 0 || i == 7)
                    {
                        this.board[i, j] = Piece.CreatePiece(pieceTypes[j] , i == 0 ? Color.Black : Color.White);
                    }
                    else if (i == 1 || i == 6)
                    {
                        this.board[i, j] = Piece.CreatePiece(PieceTypes.Pawn, i == 1 ? Color.Black : Color.White);
                    }
                    else
                        board[i, j] = null;
                }
            }
        }
    }
}

