import java.security.PublicKey;

public class Cursor {
   private static int row = 0;
   private static  int col = 0;
    
    public static  int[] getCursorPosition(){
        return new int[]{row ,col};
    }

    public static int[] getCursorPositionUI(){
        int[] row_col = getCursorPosition();
        int row = SD.PLAYERCOLOR == SD.RED ? 7 - row_col[0]:row_col[0];
        int col = SD.PLAYERCOLOR == SD.RED ?  7- row_col[1]:row_col[1];

        int r = row + 1;
        int c = col * 2+ 3;
        return new int[]{r,c};
    }

    public  static void MoveCursor(int[] steps){
        int coefficient = 1;
        if(SD.PLAYERCOLOR == SD.RED) coefficient = -1;
        int r = Cursor.row +  steps[0] * coefficient;
        int c = Cursor.col + steps[1] * coefficient;


        if( r <  0 || r >7 || c <0 || c > 7 )   return;

        Cursor.row = r;
        Cursor.col = c;
    }

}
