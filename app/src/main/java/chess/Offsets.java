package chess;

public class Offsets {
    int[] offsets;
    public static int[] getOffsets(String pieceType){
        int[] offsets;
        switch (pieceType) {
            case "N":
                return new int[]{17, 10, -6, -15, -17, -10, 6, 15};
            case "B":
                offsets = new int[28];
                for (int i = 1; i <= 7; i++){
                    offsets[i-1] = i*7;
                    offsets[i+6] = i*9;
                    offsets[i+13] = i*-9;
                    offsets[i+20] = i*-7;
                }
                return offsets;
            case "R":
                offsets = new int[28];
                for (int i = 1; i <= 7; i++){
                    offsets[i-1] = i;
                    offsets[i+6] = (i)*8;
                    offsets[i+13] = -(i);
                    offsets[i+20] = -(i)*8;
                }
                return offsets;
            case "Q":
                offsets = new int[56];
                for (int i = 1; i <= 7; i++){
                    offsets[i-1] = i*7;
                    offsets[i+6] = i*9;
                    offsets[i+13] = i*-9;
                    offsets[i+20] = i*-7;
                }
                for (int i = 1; i <= 7; i++){
                    offsets[i+27] = i;
                    offsets[i+34] = (i)*8;
                    offsets[i+41] = -(i);
                    offsets[i+48] = -(i)*8;
                }
                return offsets;
            case "K":
                return new int[]{1, -1, 8, -8, 9, -9, 7, -7};
        
            default:
                offsets = new int[]{};
                break;
        }
        return offsets;
    }
}
