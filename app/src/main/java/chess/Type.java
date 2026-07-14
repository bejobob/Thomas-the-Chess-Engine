package chess;

public class Type {
    String pieceType;
    int[] offsets;

    public Type(String pieceType, int[] offsets){
        this.pieceType = pieceType;
        this.offsets = offsets;
    }

    public int[] getOffsets(String pieceType){
        int[] offsets;
        switch (pieceType) {
            case "P":
                offsets = new int[]{1};
                break;
            case "N":
                offsets = new int[]{17, 10, -6, -15, -17, -10, 6, 15};
                break;
            case "B":
                offsets = {9, 18, 27, 36, 45, 54, 63, 7, 14, 21, 28, 35, 42, 49, 56}
        
            default:
                offsets = new int[]{};
                break;
        }
        return offsets;
    }
}
