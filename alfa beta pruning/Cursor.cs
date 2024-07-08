using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class Cursor
    {
        private int cy_;
        private int cx_;
        private int stepx;
        private int stepy;
        public int cx
        {
            get => cx_;
            set
            {
                if (value >= SD.offsetx && value  <= SD.offsetx + 7 *2) this.cx_ = value;
            }
        }
        public int cy
        {
            get => cy_;
            set
            {
                if (value >= SD.offsety && value <= SD.offsety + 7) this.cy_ = value; 
            }
        }

        public int boundryX;
        public int boundryY;
        public Dictionary<string, Func<bool>> moveMap;
        public Cursor()
        {
            this.moveMap = new Dictionary<string, Func<bool>>();
            this.moveMap.Add("RightArrow", () =>
            {
                this.cx+= stepx;
                return true;
            });
            this.moveMap.Add("LeftArrow", () =>
            {
                this.cx -= stepx;
                return true;
            });
            this.moveMap.Add("UpArrow", () =>
            {
                this.cy -=stepy;
                return true;
            });
            this.moveMap.Add("DownArrow", () =>
            {
                this.cy += stepy;
                return true;
            });
            // initial cursor opsiiton 
            this.stepx = 2;
            this.stepy = 1;
            this.cx_ = SD.offsetx;
            this.cy_ = SD.offsety;

        }
        public bool ExecuteArrowKeyMoves(ConsoleKeyInfo ckeyinfo)
        {
            string key = ckeyinfo.Key.ToString();
            Func<bool> func = null;
            this.moveMap.TryGetValue(ckeyinfo.Key.ToString(), out func);
            bool flag = func == null ? false : func(); // execute funciton and move cursor
            return flag;
        }
        // row (y)- col (x)
        public int[] convertCursorPositionToBoardPosition()
        {
            return [SD.playerColor == Color.Black ? 7 - ((this.cy - SD.offsety) / this.stepy) : (this.cy - SD.offsety) / this.stepy , SD.playerColor == Color.Black ? 7 - ((this.cx - SD.offsetx) / this.stepx) : (this.cx - SD.offsetx) / this.stepx];
        }

    }
}
