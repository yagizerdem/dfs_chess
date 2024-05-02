public class SD {

    // engima
    public static int keypr; // key pressed?
    public static int rkey; // key (for press/release)
    public static int rkeymod; // key modifiers
    public static int capslock = 0; // 0:off 1:on
    //

    static  String RED = "RED";
    static String BLUE = "BLUE";

    // game

    static boolean turn = true;
    static boolean isAiModeOn = false;

    static String PLAYERCOLOR = SD.BLUE;

    static int[] from = new int[]{-1,-1};

    static int[] enPassant = new int[]{-1,-1};


}
