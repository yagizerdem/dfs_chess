import java.lang.invoke.MethodHandle;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class Engine {

    // retun move type if not valid return null
    public static String Validate(int[] from , int[] to){
        if(Board.board[from[0]][from[1]] == null) return  null; // cant select empty square
        Piece selected = (Piece) Board.board[from[0]][from[1]];
        // cant capture own color
        if(Board.board[to[0]][to[1]] != null &&((Piece)Board.board[to[0]][to[1]]).color == selected.color){
            return null;
        }
        boolean flag = GenericCheckPiece(selected.symbol , from , to);
        if(!flag) return  null;
        // specify move type
        String moveType = MoveDTO.NORMALMOVE;
        if(selected.symbol == 'K' && from[0] - to[0] == 0 && Math.abs(from[1] - to[1]) == 2){
            // castle
            // check can castle
            String type = null;
            if(selected.color  == SD.RED && from[1] < to[1]){
                moveType = MoveDTO.REDSMALLCASTLE;
            }
            else if(selected.color == SD.RED && from[1] > to[1]){
                moveType = MoveDTO.REDBIGCASTLE;
            }
            else if(selected.color == SD.BLUE && from[1] > to[1]){
                moveType = MoveDTO.BLUEBIGCASTLE;
            }
            else{
                moveType = MoveDTO.BLUESMALLCASTLE;
            }
            flag = CheckCanCastle(moveType);
            if(!flag) return null;
            // check threats
            int diff = Math.abs(to[1] - from[1]);
            int step = to[1] - from[1] < 0 ? -1 : 1;
            int r = to[0];
            int c = from[1];
            for(int i = 0 ; i <= diff;i++){
                if(i != 0 && Board.board[r][c] != null){
                    return  null;
                }
                flag = isSuqareSafer(selected.color == SD.BLUE ? SD.RED : SD.BLUE , new int[]{r,c});
                if(!flag) return null;
                c += step;
            }
        }
        else if(selected.symbol == 'P' && to[0] == SD.enPassant[0] && to[1] == SD.enPassant[1]){
            // en passant
            MoveDTO dto = EnPassant(from , to);
            int[] king_row_col = getKingSquare(selected.color);
            flag = isSuqareSafer(selected.color == SD.RED ? SD.BLUE :SD.RED , king_row_col );
            ReMove(dto);
            if(flag) moveType = MoveDTO.ENPASSANT;
        }
        else{
            MoveDTO dto = NormalMove(from , to);
            // check king is in danger
            int[] king_row_col = getKingSquare(selected.color);
            flag = isSuqareSafer(selected.color == SD.RED ? SD.BLUE :SD.RED , king_row_col );
            ReMove(dto);
        }



        if(!flag) return  null;

        return moveType;
    }
    private static MoveDTO NormalMove(int[] from , int[] to){
        Piece selected = (Piece)Board.board[from[0]][from[1]];
        MoveDTO dto = createMoveDTO(from , to);
        if(selected.symbol == 'P' && selected.moveCounter == 0 && Math.abs(from[0] - to[0]) == 2){
            int r = selected.color == SD.RED ? 2 : 5;
            int c = to[1];
            SD.enPassant = new int[]{r,c};
        }else{
            SD.enPassant = new int[]{-1,-1};
        }


        Board.board[from[0]][from[1]] = null;
        Board.board[to[0]][to[1]] = selected;
        selected.moveCounter++;


        return dto;
    }
    public static void ReMove(MoveDTO dto){
        if(dto.specialMove != MoveDTO.ENPASSANT && dto.specialMove != MoveDTO.NORMALMOVE){
            // redo castles
            if(dto.specialMove == MoveDTO.BLUESMALLCASTLE){
                Piece king = (Piece) Board.board[7][6];
                Piece rook = (Piece) Board.board[7][5];

                Board.board[7][6] = null;
                Board.board[7][5] = null;
                king.moveCounter--;
                rook.moveCounter--;

                Board.board[7][7] = rook;
                Board.board[7][4] = king;
            }
            else if(dto.specialMove == MoveDTO.BLUEBIGCASTLE){
                Piece king = (Piece) Board.board[7][2];
                Piece rook = (Piece) Board.board[7][3];

                Board.board[7][3] = null;
                Board.board[7][2] = null;
                king.moveCounter--;
                rook.moveCounter--;

                Board.board[7][0] = rook;
                Board.board[7][4] = king;
            }
            else if(dto.specialMove == MoveDTO.REDSMALLCASTLE){
                Piece king = (Piece) Board.board[0][6];
                Piece rook = (Piece) Board.board[0][5];

                Board.board[0][6] = null;
                Board.board[0][5] = null;
                king.moveCounter--;
                rook.moveCounter--;

                Board.board[0][7] = rook;
                Board.board[0][4] = king;
            }
            else if(dto.specialMove == MoveDTO.REDBIGCASTLE){
                Piece king = (Piece) Board.board[0][2];
                Piece rook = (Piece) Board.board[0][3];

                Board.board[0][2] = null;
                Board.board[0][3] = null;
                king.moveCounter--;
                rook.moveCounter--;

                Board.board[0][0] = rook;
                Board.board[0][4] = king;
            }
            return;
        }
        Board.board[dto.from[0]][dto.from[1]] = dto.moved;
        Board.board[dto.to[0]][dto.to[1]] = null;
        Board.board[dto.capturedCoord[0]][dto.capturedCoord[1]] = dto.captured;
        SD.enPassant = dto.enPassant;
    }
    // special moves
    private static MoveDTO EnPassant(int[] from , int[] to){
        Piece selected = (Piece)Board.board[from[0]][from[1]];
        MoveDTO dto = createMoveDTO(from ,to);
        int r = selected.color == SD.RED ? 4 : 3;
        int c = to[1];
        Piece captured = (Piece)Board.board[r][c];
        Piece deepCopyCaptured = new Piece(captured.color , captured.moveCounter , captured.symbol);
        dto.capturedCoord = new int[]{r,c};
        dto.captured = deepCopyCaptured;

        SD.enPassant = new int[]{-1,-1};
        Board.board[to[0]][to[1]] = Board.board[from[0]][from[1]];
        Board.board[from[0]][from[1]] = null;
        Board.board[r][c] = null;
        selected.moveCounter++;
        return dto;
    }
    private static MoveDTO Castle(String type){
        MoveDTO dto = new MoveDTO();
        if(type == MoveDTO.BLUESMALLCASTLE){
            Piece king = (Piece) Board.board[7][4];
            Piece rook = (Piece) Board.board[7][7];
            Board.board[7][6] = king;
            Board.board[7][5] = rook;
            king.moveCounter++;
            rook.moveCounter++;

            Board.board[7][4] = null;
            Board.board[7][7] = null;
            dto.specialMove = MoveDTO.BLUESMALLCASTLE;
        }
        else if(type == MoveDTO.BLUEBIGCASTLE){
            Piece king = (Piece) Board.board[7][4];
            Piece rook = (Piece) Board.board[7][0];
            Board.board[7][2] = king;
            Board.board[7][3] = rook;
            king.moveCounter++;
            rook.moveCounter++;

            Board.board[7][4] = null;
            Board.board[7][0] = null;
            dto.specialMove  = MoveDTO.BLUEBIGCASTLE;
        }
        else if(type == MoveDTO.REDSMALLCASTLE){
            Piece king = (Piece) Board.board[0][4];
            Piece rook = (Piece) Board.board[0][7];
            Board.board[0][6] = king;
            Board.board[0][5] = rook;
            king.moveCounter++;
            rook.moveCounter++;

            Board.board[0][4] = null;
            Board.board[0][7] = null;
            dto.specialMove = MoveDTO.REDSMALLCASTLE;
        }
        else {
            // RED big castle
            Piece king = (Piece) Board.board[0][4];
            Piece rook = (Piece) Board.board[0][0];
            Board.board[0][2] = king;
            Board.board[0][3] = rook;
            king.moveCounter++;
            rook.moveCounter++;

            Board.board[0][4] = null;
            Board.board[0][0] = null;
            dto.specialMove  =MoveDTO.REDBIGCASTLE;
        }

       return dto;
    }
    public static MoveDTO Move(int[] from , int to[] , String type){
        MoveDTO dto;
        if(type == MoveDTO.NORMALMOVE){
            dto = NormalMove(from , to);
        }
        else if(type == MoveDTO.ENPASSANT){
            dto = EnPassant(from , to);
        }
        else{
            dto = Castle(type);
        }
        return dto;
    }
    public static boolean CheckHorse(int[] from , int[] to){
            int diff = (int)Math.pow(from[0] - to[0], 2)  + (int)Math.pow(to[1]- from[1],2);
            return  diff == 5;
    }
    public static boolean CheckBishop(int[] from , int[] to){

        int diffrow = to[0] - from[0];
        int diffcol = to[1] - from[1];
        if(!(Math.abs(diffrow) == Math.abs(diffcol))) return false;
        if(!CheckObstacles(from,to)) return false;
        return true;
    }
    public static boolean CheckRook(int[] from , int[] to){
        if(!CheckObstacles(from,to)) return false;
        int diffrow = to[0] - from[0];
        int diffcol = to[1] - from[1];
        if(!((diffcol == 0 && diffrow != 0)|| (diffcol != 0 && diffrow == 0))) return false;
        if(!CheckObstacles(from,to)) return false;
        return true;

    }
    public static boolean CheckQueen(int[] from , int[] to){

        int diffrow = to[0] - from[0];
        int diffcol = to[1] - from[1];
        if(!(((diffcol == 0 && diffrow != 0)|| (diffcol != 0 && diffrow == 0)) || (Math.abs(diffrow) == Math.abs(diffcol)))) return false;
        if(!CheckObstacles(from,to)) return false;
        return true;
    }
    public static boolean CheckKing(int[] from , int[]to){
        int diffrow = to[0] - from[0];
        int diffcol = to[1] - from[1];
        if(diffrow ==0 && Math.abs(diffcol) == 2){
            // check castle

            return true;
        }
        else{
            if(Math.abs(diffcol) > 1 || Math.abs(diffrow) > 1) return false;
            return true;
         }
    }
    public static boolean CheckObstacles(int[] from , int[] to){
        int diffrow = to[0] - from[0];
        int diffcol = to[1] - from[1];
        int steprow = diffrow < 0 ? -1 : 1;
        int stepcol = diffcol < 0 ? -1 : 1;
        if(diffrow == 0) steprow = 0;
        if(diffcol == 0) stepcol = 0;

        int diff = Math.max(Math.abs(diffrow) , Math.abs(diffcol));
        for(int i = 0 , r = steprow + from[0] , c = stepcol + from[1]; i < diff -1 ; i++ , r+=steprow , c+=stepcol){
            if(Board.board[r][c] != null) return false;
        }

        return  true;

    }
    public static boolean CheckPawn(int[] from , int[] to){
        int diffrow = to[0] - from[0];
        int diffcol = to[1] - from[1];
        Piece selectedPiece = (Piece)Board.board[from[0]][from[1]];
        if(Math.abs(diffrow) > 2 || Math.abs(diffcol) > 2) return false;
        if((selectedPiece.color == SD.RED && diffrow < 0) || (selectedPiece.color == SD.BLUE && diffrow > 0)) return false;
        if(Math.abs(diffrow) == 2 && Math.abs(diffcol) > 0) return false;
        if(Math.abs(diffrow) == 2 && selectedPiece.color == SD.RED && from[0] != 1) return false;
        if(Math.abs(diffrow) == 2 && selectedPiece.color == SD.BLUE && from[0] != 6) return false;
        if(Math.abs(diffcol) == 0 && Board.board[to[0]][to[1]] != null) return false;
        if(Math.abs(diffcol) > 1) return false;
        if(!CheckObstacles(from,to)) return false;

        if(Math.abs(diffcol) == 1){
            if(SD.enPassant[0] == to[0] && SD.enPassant[1] == to[1]){
                // en passant case
            }
            else{
                if(Board.board[to[0]][to[1]] == null) return false;
            }
        }

        return true;
    }
    public static boolean GenericCheckPiece(Character symbol , int[] from , int[] to){
        boolean flag = true;
        if(symbol == 'H'){
            flag = CheckHorse(from , to);
        }
        else if(symbol == 'B'){
            flag = CheckBishop(from , to);
        }
        else if(symbol == 'R'){
            flag = CheckRook(from , to);
        }
        else if(symbol == 'Q'){
            flag = CheckQueen(from , to);
        }
        else if(symbol == 'K'){
            flag = CheckKing(from , to);
        }
        else if(symbol == 'P'){
            flag = CheckPawn(from , to);
        }
        return flag;
    }
    public static boolean isSuqareSafer(String enemyColor,int[] coords){
        for(int i = 0 ; i <8 ; i++){
            for(int j = 0 ; j <8 ; j++){
                if(Board.board[i][j] == null) continue;
                Piece piece = (Piece)Board.board[i][j];
                if(piece.color != enemyColor) continue;;
                boolean flag = GenericCheckPiece(piece.symbol , new int[]{i,j} , coords);
                if(flag) return false;
            }
        }

        return  true;
    }
    public static int[] getKingSquare(String color){
        for(int i = 0 ; i < 8 ; i++){
            for(int j = 0 ; j < 8 ; j++){
                if(Board.board[i][j] != null && ((Piece)Board.board[i][j]).symbol == 'K' && ((Piece)Board.board[i][j]).color == color){
                    return  new int[]{i,j};
                }
            }
        }
        return null;
    }
    private static MoveDTO createMoveDTO(int[] from , int[] to){
        Piece selected = (Piece)Board.board[from[0]][from[1]];
        Piece deepCopyMoved = new Piece(selected.color , selected.moveCounter , selected.symbol );
        Piece deepCopyCaptured = null;
        if(Board.board[to[0]][to[1]] != null){
            Piece captured = (Piece)Board.board[to[0]][to[1]];
            deepCopyCaptured = new Piece(captured.color , captured.moveCounter, captured.symbol );
        }

        //
        MoveDTO dto = new MoveDTO();
        dto.enPassant = SD.enPassant;
        dto.from = from;
        dto.to = to;
        dto.capturedCoord = to;
        dto.specialMove = MoveDTO.NORMALMOVE;
        dto.captured = deepCopyCaptured;
        dto.moved = deepCopyMoved;

        return dto;
    }
    
    private static boolean CheckCanCastle(String type){
        if(type == MoveDTO.BLUEBIGCASTLE){
            if(!(Board.board[7][4] instanceof  Piece)) return false;
            if(!(Board.board[7][0] instanceof  Piece)) return false;
            Piece king = (Piece)Board.board[7][4];
            Piece rook = (Piece) Board.board[7][0];
            if(king.moveCounter + rook.moveCounter > 0) return false;
        }
        else if(type == MoveDTO.BLUESMALLCASTLE){
            if(!(Board.board[7][4] instanceof  Piece)) return false;
            if(!(Board.board[7][7] instanceof  Piece)) return false;
            Piece king = (Piece)Board.board[7][4];
            Piece rook = (Piece) Board.board[7][7];
            if(king.moveCounter + rook.moveCounter > 0) return false;
        }
        else if(type == MoveDTO.REDBIGCASTLE){
            if(!(Board.board[0][4] instanceof  Piece)) return false;
            if(!(Board.board[0][0] instanceof  Piece)) return false;
            Piece king = (Piece)Board.board[0][4];
            Piece rook = (Piece) Board.board[0][0];
            if(king.moveCounter + rook.moveCounter > 0) return false;
        }
        else{
            // red small castle
            if(!(Board.board[0][4] instanceof  Piece)) return false;
            if(!(Board.board[0][0] instanceof  Piece)) return false;
            Piece king = (Piece)Board.board[0][4];
            Piece rook = (Piece) Board.board[0][0];
            if(king.moveCounter + rook.moveCounter > 0) return false;
        }
        return true;
    }


    public static ArrayList<MoveDTO> GenerateAllMoves(String color){
        ArrayList<MoveDTO>allMoves = new ArrayList<MoveDTO>();
        for(int i = 0 ; i< 8 ; i++){
            for(int j = 0 ; j <8 ;j++){
                if(Board.board[i][j] != null){
                    Piece selected = (Piece) Board.board[i][j];
                    if(selected.color != color) continue;
                    int[] from = new int[]{i,j};
                    for(int r = 0 ; r <8 ; r++ ){
                        for(int c = 0 ; c < 8 ;c++){
                            int[] to = new int[]{r,c};
                            String type = Validate(from , to);
                            if(type ==  null) continue; // invalid moves
                            MoveDTO dto = new MoveDTO();
                            dto.from = from;
                            dto.to = to;
                            dto.specialMove = type;
                            allMoves.add(dto);
                        }
                    }

                }
            }
        }
        return allMoves;
    }

    public static void Promote(){
        for(int i =  0 ; i < 8 ;i++){
                if(Board.board[0][i] != null){
                    Piece p = (Piece)Board.board[0][i];
                    if(p.symbol == 'P' && p.color == SD.BLUE){
                        Board.board[0][i] = new Piece(p.color ,p.moveCounter ,'Q');
                    }
                }
                else if(Board.board[7][i] != null){
                    Piece p = (Piece)Board.board[7][i];
                    if(p.symbol == 'P' && p.color == SD.RED){
                        Board.board[7][i] = new Piece(p.color ,p.moveCounter ,'Q');
                    }
                }
        }
    }
}
