using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace alfa_beta_pruning
{
    internal class UI
    {
        public int offsetx;
        public int offsety; 

        public UI()
        {
            this.offsetx = SD.offsetx;
            this.offsety = SD.offsety;
        }
        public void PrintBoard(Board board)
        {
            object[,] matrix = board.board;
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    Console.SetCursorPosition(j * 2 + this.offsetx,i + this.offsety);
                    object item = SD.playerColor == Color.Black ? matrix[7-i, 7-j] : matrix[i, j];
                    setColor(item);
                    if(item is Piece)
                    {
                        Piece p = (Piece)item;
                        Console.Write($"{Piece.iconMap[p.type]} ");
                    }
                    else {
                        Console.Write(". ");
                    }
                    ResetColor();
                }
            }
        }
        public void PrintBoardOutline()
        {
            Console.SetCursorPosition(0, 0);
            string outline = """
                ---------------------
                |                   |
                |                   |
                |                   |
                |                   |
                |                   |
                |                   |
                |                   |
                |                   |
                ---------------------
                """;
            Console.WriteLine(outline); 
        }
        
        public void setColor(object item)
        {
            if (item is Piece)
            {
                Piece p = (Piece)item;
                if (p.color == Color.White)
                {
                    Console.ForegroundColor = p.isSelected ? ConsoleColor.Yellow : ConsoleColor.Blue;
                }
                else
                {
                    Console.ForegroundColor = p.isSelected ? ConsoleColor.Yellow : ConsoleColor.Red;
                }
            }
            else
            {
                Console.ForegroundColor = ConsoleColor.White;
            }
        }
        public void ResetColor()
        {
            Console.ForegroundColor = ConsoleColor.White;
        }
        public void SyncCursor(Cursor cursor)
        {
            Console.SetCursorPosition(cursor.cx , cursor.cy);
        }
    }



}
