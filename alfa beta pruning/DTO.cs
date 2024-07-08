using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class DTO
    {
        public Piece moved1 = null;
        public Piece moved2 = null;
        public Piece captured = null;
        public PieceTypes moved1type;
        public int[] from1 = null;
        public int[] to1 = null;
        public int[] from2 = null;
        public int[] to2 = null;
    }
}
