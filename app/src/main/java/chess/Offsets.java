package chess;

public class Offsets {
    int[] offsets;
    int itter;
    public static int[] getOffsets(PieceType pieceType){
        switch (pieceType) {
            case KNIGHT:
                return new int[]{17, 10, -6, -15, -17, -10, 6, 15};
            case BISHOP:
                return new int[]{-9, -7, 9, 7};
            case ROOK:
                return new int[]{1, -1, 8, -8};
            case QUEEN:
                return new int[]{-9, -7, 9, 7, 1, -1, 8, -8};
            case KING:
                return new int[]{1, -1, 8, -8, 9, -9, 7, -7};
            default:
                break;
        }
        return null;
    }

    public static int getItter(PieceType piecesType){
        switch (piecesType) {
            case KNIGHT:
                return 1;
            case BISHOP:
                return 7;
            case ROOK:
                return 7;
            case QUEEN:
                return 7;
            case KING:
                return 1;        
            default:
                break;
        }
        return 0;
    }
}
