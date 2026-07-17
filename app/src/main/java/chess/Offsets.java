package chess;

public class Offsets {
    int[] offsets;
    int itter;
    public static int[] getOffsets(String pieceType){
        switch (pieceType) {
            case "N":
                return new int[]{17, 10, -6, -15, -17, -10, 6, 15};
            case "B":
                return new int[]{-9, -7, 9, 7};
            case "R":
                return new int[]{1, -1, 8, -8};
            case "Q":
                return new int[]{-9, -7, 9, 7, 1, -1, 8, -8};
            case "K":
                return new int[]{1, -1, 8, -8, 9, -9, 7, -7};
        
            default:
                break;
        }
        return null;
    }

    public static int getItter(String piecesType){
        switch (piecesType) {
            case "N":
                return 1;
            case "B":
                return 7;
            case "R":
                return 7;
            case "Q":
                return 7;
            case "K":
                return 1;        
            default:
                break;
        }
        return 0;
    }
}
