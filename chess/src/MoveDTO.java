public class MoveDTO {
    Piece moved;
    Piece captured;
    int[] from ;
    int[] to;
    int[] capturedCoord;
    String specialMove;
    int[] enPassant = new int[]{-1,-1};

    static String REDSMALLCASTLE = "REDSMALLCASTLE";
    static String REDBIGCASTLE = "REDBIGCASTLE";
    static String BLUESMALLCASTLE = "BLUESMALLCASTLE";
    static String BLUEBIGCASTLE = "BLUEBIGCASTLE";
    static String ENPASSANT = "ENPASSANT";
    static String NORMALMOVE = "NORMALMOVE";
}
