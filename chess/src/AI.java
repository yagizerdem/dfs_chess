import java.util.ArrayList;
import java.util.Hashtable;

public class AI {
    static int[][] pawnTableArray = {
            { 0, 0, 0, 0, 0, 0, 0, 0 },
            { 5, 10, 10, -20, -20, 10, 10, 5 },
            { 5, -5, -10, 0, 0, -10, -5, 5 },
            { 0, 0, 0, 20, 20, 0, 0, 0 },
            { 5, 5, 10, 25, 25, 10, 5, 5 },
            { 10, 10, 20, 30, 30, 20, 10, 10 },
            { 50, 50, 50, 50, 50, 50, 50, 50 },
            { 0, 0, 0, 0, 0, 0, 0, 0 }
    };
    static int[][] horseTableArray = {
            {-50, -40, -30, -30, -30, -30, -40, -50},
            {-40, -20, 0, 5, 5, 0, -20, -40},
            {-30, 5, 10, 15, 15, 10, 5, -30},
            {-30, 0, 15, 20, 20, 15, 0, -30},
            {-30, 5, 15, 20, 20, 15, 5, -30},
            {-30, 0, 10, 15, 15, 10, 0, -30},
            {-40, -20, 0, 0, 0, 0, -20, -40},
            {-50, -40, -30, -30, -30, -30, -40, -50}
    };

    static int[][] BishopstableArray = {
            {-20, -10, -10, -10, -10, -10, -10, -20},
            {-10, 5, 0, 0, 0, 0, 5, -10},
            {-10, 10, 10, 10, 10, 10, 10, -10},
            {-10, 0, 10, 10, 10, 10, 0, -10},
            {-10, 5, 5, 10, 10, 5, 5, -10},
            {-10, 0, 5, 10, 10, 5, 0, -10},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-20, -10, -10, -10, -10, -10, -10, -20}
    };
    static int[][] rookstableArray = {
            {0, 0, 0, 5, 5, 0, 0, 0},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {-5, 0, 0, 0, 0, 0, 0, -5},
            {5, 10, 10, 10, 10, 10, 10, 5},
            {0, 0, 0, 0, 0, 0, 0, 0}
    };
    static int[][] queenstable = {
            {-20, -10, -10, -5, -5, -10, -10, -20},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-10, 5, 5, 5, 5, 5, 0, -10},
            {0, 0, 5, 5, 5, 5, 0, -5},
            {-5, 0, 5, 5, 5, 5, 0, -5},
            {-10, 0, 5, 5, 5, 5, 0, -10},
            {-10, 0, 0, 0, 0, 0, 0, -10},
            {-20, -10, -10, -5, -5, -10, -10, -20}
    };

    static int[][] kingstable = {
            {20, 30, 10, 0, 0, 10, 30, 20},
            {20, 20, 0, 0, 0, 0, 20, 20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30}
    };

    static  int[][] whitePawnTable = new int[8][8];
    static int[][] whiteHorseTable = new int[8][8];
    static int[][] whiteBishopTable = new int[8][8];
    static int[][] whiteRookTable = new int[8][8];
    static int[][] whiteQueenTable = new int[8][8];
    static int[][] whiteKingTable = new int[8][8];

    static  int[][] blackPawnTable = new int[8][8];
    static int[][] blackHorseTable = new int[8][8];
    static int[][] blackBishopTable = new int[8][8];
    static int[][] blackRookTable = new int[8][8];
    static int[][] blackQueenTable = new int[8][8];
    static int[][] blackKingTable = new int[8][8];

    static  Hashtable<Character , Integer> whitePiecePoints = new Hashtable<Character, Integer>();
    static  Hashtable<Character , Integer> blackPiecePoints = new Hashtable<Character, Integer>();

    static void init(){
        for(int i = 0 ; i <8 ; i++){
            whitePawnTable[i]  = pawnTableArray[7-i];
            whiteHorseTable[i]  = horseTableArray[7-i];
            whiteBishopTable[i] = BishopstableArray[7-i];
            whiteQueenTable[i] = queenstable[7-i];
            whiteRookTable[i] = rookstableArray[7-i];
            whiteKingTable[i] = kingstable[7-i];
        }
        for(int i = 0 ; i < 8 ; i++){
            for(int j = 0 ; j <8 ;j++){
                blackPawnTable[i][j]  = whitePawnTable[7-i][7-j] * -1;
                blackHorseTable[i][j]  = whiteHorseTable[7-i][7-j]*-1;
                blackBishopTable[i][j] = whiteBishopTable[7-i][7-j]*-1;
                blackQueenTable[i][j] = whiteQueenTable[7-i][7-j]*-1;
                blackRookTable[i][j] = whiteRookTable[7-i][7-j]*-1;
                blackKingTable[i][j] = whiteKingTable[7-i][7-j]*-1;
            }
        }

        whitePiecePoints.put('P',10);
        whitePiecePoints.put('H',30);
        whitePiecePoints.put('B',30);
        whitePiecePoints.put('Q',90);
        whitePiecePoints.put('K',99999);
        whitePiecePoints.put('R',50);

        blackPiecePoints.put('P',-10);
        blackPiecePoints.put('H',-30);
        blackPiecePoints.put('B',-30);
        blackPiecePoints.put('Q',-90);
        blackPiecePoints.put('K',-99999);
        blackPiecePoints.put('R',-50);

    }

    public static int dfs(String color , int depth){
        if(depth == 4){
            // calculate
            int points = 0;
            for(int i = 0 ; i  < 8 ; i++){
                for(int j = 0; j <8 ; j++){
                    if(Board.board[i][j] != null){
                        Piece p = (Piece)Board.board[i][j];
                        if(p.color == SD.RED){
                            points += blackPiecePoints.get(p.symbol);
                            if(p.symbol == 'P'){
                                points += blackPawnTable[i][j];
                            }
                            else if(p.symbol == 'R'){
                                points += blackRookTable[i][j];
                            }
                            else if(p.symbol == 'Q'){
                                points += blackQueenTable[i][j];
                            }
                            else if(p.symbol == 'K'){
                                points += blackKingTable[i][j];
                            }
                            else if(p.symbol == 'B'){
                                points += blackBishopTable[i][j];
                            }
                            else if(p.symbol == 'H'){
                                points += blackHorseTable[i][j];
                            }
                        }
                        if(p.color == SD.BLUE){
                            points += whitePiecePoints.get(p.symbol);
                            if(p.symbol == 'P'){
                                points += whitePawnTable[i][j];
                            }
                            else if(p.symbol == 'R'){
                                points += whiteRookTable[i][j];
                            }
                            else if(p.symbol == 'Q'){
                                points += whiteQueenTable[i][j];
                            }
                            else if(p.symbol == 'K'){
                                points += whiteKingTable[i][j];
                            }
                            else if(p.symbol == 'B'){
                                points += whiteBishopTable[i][j];
                            }
                            else if(p.symbol == 'H'){
                                points += whiteHorseTable[i][j];
                            }
                        }

                    }
                }
            }

            return points;
        }
        ArrayList<MoveDTO> allMoves = Engine.GenerateAllMoves(color);
        Integer maxPoint = -99999999;
        for(MoveDTO dto : allMoves){
            try {
                MoveDTO remake =  Engine.Move(dto.from , dto.to , dto.specialMove);
                int score = dfs(color == SD.RED ? SD.BLUE : SD.RED , depth+ 1);
                if(score > maxPoint){
                    maxPoint = score;
                }
                Engine.ReMove(remake);
            }catch (Error err){

            }
        }

        return  maxPoint;
    }

    public static MoveDTO FindBestMove(){
        ArrayList<MoveDTO> allMoves = Engine.GenerateAllMoves(SD.RED);
        int minPoint = 9999999;
        MoveDTO bestMove = null;
        for(MoveDTO dto : allMoves){
            try {
                MoveDTO remake =  Engine.Move(dto.from , dto.to , dto.specialMove);
                int score = dfs( SD.BLUE  , 1);
                if(score < minPoint){
                    minPoint = score;
                    bestMove = remake;
                }
                Engine.ReMove(remake);
            }catch (Error err){

            }
        }
        return bestMove;
    }

}
