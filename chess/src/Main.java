import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class Main{
    static EnigmaConfig screen = new EnigmaConfig();
    static Hashtable<Integer , int[]> keyboardlistener = new Hashtable<Integer, int[]>();
    public static void main(String[] args) {
        AI.init();
        init();
        UI.PrintBoard();
        UI.SyncCursor();


        while (true){
            if(SD.keypr == 1){
                if(SD.rkey >= 37 && SD.rkey <= 40){
                    int[] step = keyboardlistener.get(SD.rkey);

                    Cursor.MoveCursor(step);
                    UI.SyncCursor();
                }
                if(SD.rkey == 10){
                    if(SD.from[0] == -1){
                        Select();
                    }
                    else if(SD.from[0] != -1){

                        int[] to = Cursor.getCursorPosition();
                        String result = Engine.Validate(SD.from , to);
                        // valid
                        if(result != null){
                            MoveDTO dto = Engine.Move(SD.from , to ,result);
                            Engine.Promote(); // promote pawn if possible
                            SD.turn = !SD.turn;
                        }

                        DropPiece();
                    }

                    UI.ClearLayout();
                    UI.PrintBoard();
                    UI.SyncCursor();
                }
                if(SD.rkey == 27){
                    DropPiece();
                    UI.ClearLayout();
                    UI.PrintBoard();
                    UI.SyncCursor();
                }
            }

            if(SD.turn == false){
                MoveDTO dto = AI.FindBestMove();
                Engine.Move(dto.from ,dto.to , dto.specialMove) ;
                UI.ClearLayout();
                UI.PrintBoard();
                SD.turn = !SD.turn;
            }

            SD.keypr = 0;
            try{
                Thread.sleep(50);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }



    }
    public static void init(){
        Board.init();
        keyboardlistener.put(37 , new int[]{0,-1});
        keyboardlistener.put(38 , new int[]{-1,0});
        keyboardlistener.put(39 , new int[]{0,1});
        keyboardlistener.put(40 , new int[]{1,0});

    }


    public static void Select(){
        if(!SD.turn) return;
        int[] row_col = Cursor.getCursorPosition();
        if(Board.board[row_col[0]][row_col[1]] == null) return;
        Piece p = (Piece)Board.board[row_col[0]][row_col[1]];
        if(p.color != SD.PLAYERCOLOR) return;
        SD.from = row_col;
    }
    public static void DropPiece(){
        SD.from = new int[]{-1,-1};
    }

    public static void SwitchColor(){
        if(SD.PLAYERCOLOR == SD.RED) SD.PLAYERCOLOR = SD.BLUE;
        else SD.PLAYERCOLOR = SD.RED;
    }



}