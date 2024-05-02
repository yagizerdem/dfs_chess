import javax.print.attribute.standard.MediaSize;
import java.sql.SQLOutput;

public class UI {

    public static void ClearLayout(){
        EnigmaConfig.cnt.setCursorPosition(0,0);
        for(int i = 0 ; i< 20 ; i++){
            System.out.println(" ".repeat(40));
        }
    }

    public static void PrintBoardBluePrespective(Object[][] board) {
        EnigmaConfig.cnt.setCursorPosition(0,0);
        for (int i = 0; i < 8; i++) {
            if (i == 0) System.out.println(" " + "-".repeat(19));
            for (int j = 0; j < 8; j++) {

                if(j == 0){
                    System.out.print(" | " );
                }

                Character symbol = board[i][j] == null ? '.' : ((Piece)board[i][j]).symbol;

                if(board[i][j] != null){
                    if(((Piece)board[i][j]).color == SD.RED ) EnigmaConfig.cn.setTextAttributes(EnigmaConfig.RED);
                    else EnigmaConfig.cn.setTextAttributes(EnigmaConfig.BLUE);

                    if(SD.PLAYERCOLOR == SD.BLUE  && SD.from[0] != -1 && SD.from[0] == i && SD.from[1] == j){
                        EnigmaConfig.cn.setTextAttributes(EnigmaConfig.GOLD);
                    }
                    if(SD.PLAYERCOLOR == SD.RED  && SD.from[0] != -1 && SD.from[0] == 7 -i && SD.from[1] == 7 -j){
                        EnigmaConfig.cn.setTextAttributes(EnigmaConfig.GOLD);
                    }

                    System.out.print(symbol + " ");
                    EnigmaConfig.cn.setTextAttributes(EnigmaConfig.WHITE);
                }
                else   System.out.print(symbol + " ");

                if(j == 7) System.out.print("|" );

            }

            System.out.println();
            if (i == 7) System.out.println(" " + "-".repeat(19));
        }
    }

    public static void PrintBoardRedPerspective(Object[][] board){
        Object[][] deepCopy = new Object[8][8];
        for(int i =0 ; i< 8 ; i++){
            for(int j  =0 ; j < 8 ; j++){
                deepCopy[7-i][7-j] = board[i][j];
            }
        }
        PrintBoardBluePrespective(deepCopy);
    }

    public static void PrintBoard(){
        if(SD.PLAYERCOLOR == SD.BLUE){
            PrintBoardBluePrespective(Board.board);
        }
        else{
            PrintBoardRedPerspective(Board.board);
        }
    }

    public static void SyncCursor(){
        int[] row_col = Cursor.getCursorPositionUI();
        EnigmaConfig.cnt.setCursorPosition(row_col[1],row_col[0]);
    }

}
