using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class Piece
    {
        public PieceTypes type;
        public int moveCounter;
        public bool canCastle;
        public bool isSelected;
        public Color color;
        public static readonly Dictionary<PieceTypes, char> iconMap;

        static Piece()
        {
            iconMap = new Dictionary<PieceTypes, char>();
            iconMap.Add(PieceTypes.Pawn, 'P');
            iconMap.Add(PieceTypes.Bishop, 'B');
            iconMap.Add(PieceTypes.Rook, 'R');
            iconMap.Add(PieceTypes.Horse, 'H');
            iconMap.Add(PieceTypes.King, 'K');
            iconMap.Add(PieceTypes.Queen, 'Q');
        }


        public static Piece CreatePiece(PieceTypes type , Color color)
        {
            Piece newPiece = new();
            newPiece.type = type;
            if(newPiece.type == PieceTypes.Rook  || newPiece.type == PieceTypes.King)
            {
                newPiece.canCastle = true;
            }
            newPiece.moveCounter = 0;
            newPiece.color = color;
            newPiece.isSelected = false;

            return newPiece;
        }
    }
}
