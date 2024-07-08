using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class Ai
    {
        int[,] whitePawntable = new int[,]
        {
    { 0, 0, 0, 0, 0, 0, 0, 0 },
    { 5, 10, 10, -20, -20, 10, 10, 5 },
    { 5, -5, -10, 0, 0, -10, -5, 5 },
    { 0, 0, 0, 20, 20, 0, 0, 0 },
    { 5, 5, 10, 25, 25, 10, 5, 5 },
    { 10, 10, 20, 30, 30, 20, 10, 10 },
    { 50, 50, 50, 50, 50, 50, 50, 50 },
    { 0, 0, 0, 0, 0, 0, 0, 0 }
        };

        int[,] whiteHorsetable = new int[,]
        {
    { -50, -40, -30, -30, -30, -30, -40, -50 },
    { -40, -20, 0, 5, 5, 0, -20, -40 },
    { -30, 5, 10, 15, 15, 10, 5, -30 },
    { -30, 0, 15, 20, 20, 15, 0, -30 },
    { -30, 5, 15, 20, 20, 15, 5, -30 },
    { -30, 0, 10, 15, 15, 10, 0, -30 },
    { -40, -20, 0, 0, 0, 0, -20, -40 },
    { -50, -40, -30, -30, -30, -30, -40, -50 }
        };

        int[,] whiteBishopstable = new int[,]
        {
    { -20, -10, -10, -10, -10, -10, -10, -20 },
    { -10, 5, 0, 0, 0, 0, 5, -10 },
    { -10, 10, 10, 10, 10, 10, 10, -10 },
    { -10, 0, 10, 10, 10, 10, 0, -10 },
    { -10, 5, 5, 10, 10, 5, 5, -10 },
    { -10, 0, 5, 10, 10, 5, 0, -10 },
    { -10, 0, 0, 0, 0, 0, 0, -10 },
    { -20, -10, -10, -10, -10, -10, -10, -20 }
        };

        int[,] whiteRookstable = new int[,]
        {
    { 0, 0, 0, 5, 5, 0, 0, 0 },
    { -5, 0, 0, 0, 0, 0, 0, -5 },
    { -5, 0, 0, 0, 0, 0, 0, -5 },
    { -5, 0, 0, 0, 0, 0, 0, -5 },
    { -5, 0, 0, 0, 0, 0, 0, -5 },
    { -5, 0, 0, 0, 0, 0, 0, -5 },
    { 5, 10, 10, 10, 10, 10, 10, 5 },
    { 0, 0, 0, 0, 0, 0, 0, 0 }
        };

        int[,] whiteQueenstable = new int[,]
        {
    { -20, -10, -10, -5, -5, -10, -10, -20 },
    { -10, 0, 0, 0, 0, 0, 0, -10 },
    { -10, 5, 5, 5, 5, 5, 0, -10 },
    { 0, 0, 5, 5, 5, 5, 0, -5 },
    { -5, 0, 5, 5, 5, 5, 0, -5 },
    { -10, 0, 5, 5, 5, 5, 0, -10 },
    { -10, 0, 0, 0, 0, 0, 0, -10 },
    { -20, -10, -10, -5, -5, -10, -10, -20 }
        };

        int[,] whiteKingstable = new int[,]
        {
    { 20, 30, 10, 0, 0, 10, 30, 20 },
    { 20, 20, 0, 0, 0, 0, 20, 20 },
    { -10, -20, -20, -20, -20, -20, -20, -10 },
    { -20, -30, -30, -40, -40, -30, -30, -20 },
    { -30, -40, -40, -50, -50, -40, -40, -30 },
    { -30, -40, -40, -50, -50, -40, -40, -30 },
    { -30, -40, -40, -50, -50, -40, -40, -30 },
    { -30, -40, -40, -50, -50, -40, -40, -30 }
        };

        int[,] blackPawntable = new int[8,8];
        int[,] blackHorsetable = new int[8, 8];
        int[,] blackBishopstable = new int[8, 8];
        int[,] blackRookstable = new int[8, 8];
        int[,] blackQueenstable  = new int[8, 8];
        int[,] blackKingstable = new int[8, 8];

        Engine engine;
        Dictionary<string, int> pointMap = new Dictionary<string, int>();
        Dictionary<string, int[,]> positionMap = new Dictionary<string, int[,]>();
        public Ai()
        {
            this.engine = new Engine();
            for (int i = 0; i <8;i++)
            {
                for (int j = 0; j <8;j++)
                {
                    blackPawntable[7 - i, 7 - j] = -whitePawntable[i, j];
                    blackHorsetable[7 - i, 7 - j] = -whiteHorsetable[i, j];
                    blackBishopstable[7 - i, 7 - j] = -whiteBishopstable[i, j];
                    blackRookstable[7-i , 7-j] = -whiteRookstable[i, j];
                    blackQueenstable[7-i,7-j] = -whiteQueenstable[i, j];
                    blackKingstable[7-i,7-j] = -whiteKingstable[i, j];
                }
            }
            pointMap.Add($"{PieceTypes.Pawn}{Color.White}", 10);
            pointMap.Add($"{PieceTypes.Horse}{Color.White}", 30);
            pointMap.Add($"{PieceTypes.Bishop}{Color.White}", 30);
            pointMap.Add($"{PieceTypes.Rook}{Color.White}", 50);
            pointMap.Add($"{PieceTypes.Queen}{Color.White}", 90);
            pointMap.Add($"{PieceTypes.King}{Color.White}", 99999);

            pointMap.Add($"{PieceTypes.Pawn}{Color.Black}", -10);
            pointMap.Add($"{PieceTypes.Horse}{Color.Black}", -30);
            pointMap.Add($"{PieceTypes.Bishop}{Color.Black}", -30);
            pointMap.Add($"{PieceTypes.Rook}{Color.Black}", -50);
            pointMap.Add($"{PieceTypes.Queen}{Color.Black}", -90);
            pointMap.Add($"{PieceTypes.King}{Color.Black}", -9999999);


            positionMap.Add($"{PieceTypes.Pawn}{Color.White}", whitePawntable);
            positionMap.Add($"{PieceTypes.Horse}{Color.White}", whiteHorsetable);
            positionMap.Add($"{PieceTypes.Bishop}{Color.White}", whiteBishopstable);
            positionMap.Add($"{PieceTypes.Rook}{Color.White}", whiteRookstable);
            positionMap.Add($"{PieceTypes.Queen}{Color.White}", whiteQueenstable);
            positionMap.Add($"{PieceTypes.King}{Color.White}", whiteKingstable);

            positionMap.Add($"{PieceTypes.Pawn}{Color.Black}", blackPawntable);
            positionMap.Add($"{PieceTypes.Horse}{Color.Black}", blackHorsetable);
            positionMap.Add($"{PieceTypes.Bishop}{Color.Black}", blackBishopstable);
            positionMap.Add($"{PieceTypes.Rook}{Color.Black}", blackRookstable);
            positionMap.Add($"{PieceTypes.Queen}{Color.Black}", blackQueenstable);
            positionMap.Add($"{PieceTypes.King}{Color.Black}", blackKingstable);
        }

 

        // white is maximazing player
        public DTO FindBestMove(IEnumerable<DTO> allPossibleMoves, bool isMaximazing , Board board)
        {
            if (allPossibleMoves.Count() == 0)
            {
                return null;
            }
            int bestScore = isMaximazing? int.MinValue : int.MaxValue;
            DTO bestMove = null;
            foreach (DTO dto in allPossibleMoves)
            {
                engine.MovePiece(dto , board);
                int score = dfs(board, isMaximazing , 1);
                if(isMaximazing && score > bestScore)
                {
                    bestScore = score;
                    bestMove = dto;
                }; // white
                if (!isMaximazing && score < bestScore)
                {
                    bestScore = score;
                    bestMove = dto;
                } ; // black
                engine.ReMove(dto , board);
            }

            // helper method
            int dfs(Board board , bool isMaximazing , int depth = 1)
            {
                Random rnd = new Random();
                if(depth == 4)
                {
                    int point = 0;
                    // calculate and return score
                    for (int i = 0; i< 8;i++)
                    {
                        for (int j = 0; j <8;j++)
                        {
                            Piece? piece = (Piece)board.board[i, j];
                            if (piece != null)
                            {
                                point += pointMap[$"{piece.type}{piece.color}"];
                                point += positionMap[$"{piece.type}{piece.color}"][i, j];
                            }
                        }
                    }
                    return point;
                }
                int bestScore = isMaximazing ? int.MinValue : int.MaxValue;
                IEnumerable<DTO> allPossibleMoves = this.generateAllMoves(color:isMaximazing?Color.White : Color.Black , board);
                if(allPossibleMoves.Count() == 0)
                {
                    if (isMaximazing) return 99999;
                    return -99999;
                }
                foreach (DTO dto in allPossibleMoves)
                {
                    engine.MovePiece(dto, board);
                    int score = dfs(board, !isMaximazing , depth +1);
                    if (isMaximazing && score > bestScore) bestScore = score; // white
                    if (!isMaximazing && score < bestScore) bestScore = score; // black
                    engine.ReMove(dto, board);
                }

                return bestScore;
            }
            return bestMove;
        }

        public IEnumerable<DTO> generateAllMoves(Color color, Board board)
        {
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Piece p = (Piece)board.board[i, j];
                    if (p == null || p.color != color) continue;
                    int[] from = [i, j];
                    for (int x = 0; x < 8; x++)
                    {
                        for (int y = 0; y < 8; y++)
                        {
                            int[] to = [x, y];
                            Engine.Result data = engine.CheckMove(board, from, to);
                            if (data.flag)
                            {
                                yield return data.dto;
                            }
                        }
                    }
                }
            }
        }
    }
}
