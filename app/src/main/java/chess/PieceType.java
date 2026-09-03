/**
 * PieceType
 * An ENUM for all the different piece types on a chess board
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */

package chess;

public enum PieceType {
    PAWN(""),
    KNIGHT("N"),
    BISHOP("B"),
    ROOK("R"),
    QUEEN("Q"),
    KING("K");

    private final String symbol;

    PieceType(String symbol){
        this.symbol = symbol;
    }

    public String symbol(){
        return symbol;
    }

    public int[] getOffsets(PieceType pieceType){
        if (pieceType == KNIGHT){
            return new int[]{17, 10, -6, -15, -17, -10, 6, 15};
        } else if (pieceType == BISHOP){
            return new int[]{9, 7, -7, -9};
        } else if (pieceType == ROOK){
            return new int[]{8, 1, -1, -8};
        } else if (pieceType == QUEEN || pieceType == KING){
            return new int[]{8, 1, -1, -8, 9, 7, -7, -9};
        }
        return new int[0];
    }

    public int getItter(PieceType pieceType){
        if (pieceType == KNIGHT || pieceType == KING){
            return 1;
        } else if (pieceType == BISHOP || pieceType == ROOK || pieceType == QUEEN){
            return 7;
        }
        return 0;
    }

    @Override
    public String toString(){
        return symbol;
    }
}
