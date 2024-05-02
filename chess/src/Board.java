public class Board {

    static Object[][] board = new Object[8][8];

    public static void init(){
        Character[] symbols = new Character[]{'R','H','B','Q','K' ,'B','H','R'};

        for(int i = 0 ; i <  8;i++){

            for(int j = 0; j < 8; j++){
                if(i == 0){
                    board[i][j] = new Piece(SD.RED , 0 , symbols[j] );
                }
                else if(i == 1){
                    board[i][j] = new Piece(SD.RED , 0 , 'P');
                }
                else if(i == 6){
                    board[i][j] = new Piece(SD.BLUE , 0 , 'P');
                }
                else if(i == 7){
                    board[i][j] = new Piece(SD.BLUE , 0 , symbols[j]);
                }else{
                    board[i][j] = null;
                }
            }
        }
    }
}
