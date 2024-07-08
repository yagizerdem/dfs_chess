namespace alfa_beta_pruning
{
    internal class Program
    {
        static void Main(string[] args)
        {
            SD.playerColor = Color.White;

            Board board = new Board();  
            UI ui = new UI();
            Cursor cursor = new Cursor();
            Ai ai = new Ai();   
            PrintUi(); 

       

            Engine engine = new Engine();

            while (true)
            {
                ConsoleKeyInfo ckeyinfo = Console.ReadKey(true);
                // cursor key middleware
                bool flag = cursor.ExecuteArrowKeyMoves(ckeyinfo);
                if(flag)
                {
                    ui.SyncCursor(cursor);
                    continue;
                }

                // select piece
                if (ckeyinfo.Key.ToString() == "Enter")
                {
                    int[] boardPositions = cursor.convertCursorPositionToBoardPosition();
                    Piece? selectedPiece = board.board[boardPositions[0],boardPositions[1]] != null ? (Piece)board.board[boardPositions[0], boardPositions[1]] : null;
                   
                    // not valid
                    if(SD.from == null && selectedPiece == null)
                    {
                        ;
                    }
                    // select piece
                    else if(SD.from == null && selectedPiece != null)
                    {
                        if (selectedPiece.color != SD.playerColor) continue;
                        selectedPiece.isSelected = true;
                        SD.from = boardPositions;
                        PrintUi();

                    }
                    // move
                    else if(SD.from != null)
                    {
                        if (SD.turn != SD.playerColor) continue;
                        int[] to = cursor.convertCursorPositionToBoardPosition();
                        Engine.Result data = engine.CheckMove(board, SD.from, to);
                        if(data.flag)
                        {
                            engine.MovePiece(data.dto , board);
                            engine.PromotePawn(board);
                            //switchTurn();
                            //switchColor();

                            // Ai move
                            IEnumerable<DTO> allPossibleMoves = ai.generateAllMoves(Color.Black, board);
                            DTO besteMove = ai.FindBestMove(allPossibleMoves, false , board);
                            engine.MovePiece(besteMove, board);
                        }
                        dropoPiece();
                        PrintUi();
                    }
                    
                }
                if(ckeyinfo.Key.ToString() == "Backspace")
                {
                    dropoPiece();
                    PrintUi();
                }
            }
            
            
            
            // utility
            void dropoPiece()
            {
                SD.from = null;
                for (int i = 0; i < board.board.GetLength(0); i++)
                {
                    for (int j = 0; j < board.board.GetLength(1); j++)
                    {
                        Piece? piece = board.board[i, j] != null ? (Piece)board.board[i, j] : null;
                        if (piece != null)
                        {
                            piece.isSelected = false;
                        }
                    }
                }
            }
            void PrintUi()
            {
                ui.PrintBoardOutline();
                ui.PrintBoard(board);
                ui.SyncCursor(cursor);
            }
            void switchTurn()
            {
                SD.turn = SD.turn == Color.White ? Color.Black : Color.White;
            }
            void switchColor()
            {
                SD.playerColor = SD.playerColor == Color.White ? Color.Black : Color.White;
            }
        }
    }
}
