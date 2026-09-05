/**
 * PieceType
 * An ENUM for all the different piece types on a chess board
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */

package chess;

public enum PieceType {
    PAWN("P", 1),
    KNIGHT("N", 3),
    BISHOP("B", 3),
    ROOK("R", 5),
    QUEEN("Q", 9),
    KING("K", 0);

    private final String symbol;
    private final int baseValue;

    float[] pawnTable = {
        0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,

        0.05f,  0.10f,  0.10f, -0.05f, -0.05f,  0.10f,  0.10f,  0.05f,

        0.05f,  0.05f,  0.10f,  0.20f,  0.20f,  0.10f,  0.05f,  0.05f,

        0.00f,  0.00f,  0.10f,  0.25f,  0.25f,  0.10f,  0.00f,  0.00f,

        0.00f,  0.00f,  0.05f,  0.20f,  0.20f,  0.05f,  0.00f,  0.00f,

        0.05f, -0.05f, -0.10f,  0.00f,  0.00f, -0.10f, -0.05f,  0.05f,

        0.05f,  0.05f,  0.05f, -0.10f, -0.10f,  0.05f,  0.05f,  0.05f,

        0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f
    };

    float[] knightTable = {
        -0.5f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.5f,

        -0.4f, -0.2f,  0.0f,  0.0f,  0.0f,  0.0f, -0.2f, -0.4f,

        -0.4f,  0.0f,  0.10f, 0.2f,  0.2f,  0.10f, 0.0f, -0.4f,

        -0.4f,  0.0f,  0.2f,  0.25f, 0.25f, 0.2f,  0.0f, -0.4f,

        -0.4f,  0.0f,  0.2f,  0.25f, 0.25f, 0.2f,  0.0f, -0.4f,

        -0.4f,  0.0f,  0.10f, 0.2f,  0.2f,  0.10f, 0.0f, -0.4f,

        -0.4f, -0.2f,  0.0f,  0.0f,  0.0f,  0.0f, -0.2f, -0.4f,

        -0.5f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.5f,
    };

    float[] bishopTable = {
        -0.2f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.2f,

        -0.1f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f, -0.1f,

        -0.1f,  0.0f,  0.10f, 0.15f, 0.15f, 0.10f, 0.0f, -0.1f,

        -0.1f,  0.05f, 0.10f, 0.20f, 0.20f, 0.10f, 0.05f, -0.1f,

        -0.1f,  0.05f, 0.10f, 0.20f, 0.20f, 0.10f, 0.05f, -0.1f,

        -0.1f,  0.0f,  0.10f, 0.15f, 0.15f, 0.10f, 0.0f, -0.1f,

        -0.1f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f, -0.1f,

        -0.2f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.2f
    };

    float[] rookTable = {
        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.05f,  0.05f,  0.05f,  0.10f,  0.10f,  0.05f,  0.05f,  0.05f,

        0.05f,  0.05f,  0.05f,  0.10f,  0.10f,  0.05f,  0.05f,  0.05f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.10f,  0.10f,  0.10f,  0.15f,  0.15f,  0.10f,  0.10f,  0.10f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f
    };

    float[] queenTable = {
        -0.2f, -0.1f, -0.1f,  0.0f,  0.0f, -0.1f, -0.1f, -0.2f,

        -0.1f,  0.0f,  0.05f, 0.05f, 0.05f, 0.05f,  0.0f, -0.1f,

        -0.1f,  0.05f, 0.10f, 0.10f, 0.10f, 0.10f, 0.05f, -0.1f,

        0.0f,  0.05f, 0.10f, 0.15f, 0.15f, 0.10f, 0.05f,  0.0f,

        0.0f,  0.05f, 0.10f, 0.15f, 0.15f, 0.10f, 0.05f,  0.0f,

        -0.1f,  0.05f, 0.10f, 0.10f, 0.10f, 0.10f, 0.05f, -0.1f,

        -0.1f,  0.0f,  0.05f, 0.05f, 0.05f, 0.05f,  0.0f, -0.1f,

        -0.2f, -0.1f, -0.1f,  0.0f,  0.0f, -0.1f, -0.1f, -0.2f
    };

    float[] kingTable = {
        0.20f,  0.30f,  0.10f,  0.00f,  0.00f,  0.10f,  0.30f,  0.20f,

        0.20f,  0.20f,  0.00f,  0.00f,  0.00f,  0.00f,  0.20f,  0.20f,

        0.10f,  0.00f, -0.10f, -0.20f, -0.20f, -0.10f,  0.00f,  0.10f,

        0.00f,  0.00f, -0.20f, -0.30f, -0.30f, -0.20f,  0.00f,  0.00f,

        0.00f,  0.00f, -0.20f, -0.30f, -0.30f, -0.20f,  0.00f,  0.00f,

        0.10f,  0.00f, -0.10f, -0.20f, -0.20f, -0.10f,  0.00f,  0.10f,

        0.20f,  0.20f,  0.00f,  0.00f,  0.00f,  0.00f,  0.20f,  0.20f,

        0.20f,  0.30f,  0.10f,  0.00f,  0.00f,  0.10f,  0.30f,  0.20f
    };

    PieceType(String symbol, int baseValue){
        this.symbol = symbol;
        this.baseValue = baseValue;
    }

    public String symbol(){
        return symbol;
    }

    public int baseValue(){
        return baseValue;
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

    public float[] getTable(){
        if (this == PAWN){
            return pawnTable;
        } else if (this == KNIGHT){
            return knightTable;
        } else if (this == BISHOP){
            return bishopTable;
        } else if (this == ROOK){
            return rookTable;
        } else if (this == QUEEN){
            return kingTable;
        } else if (this == KING){
            return kingTable;
        } else {
            return null; // this should never be reached.
        }
    }

    @Override
    public String toString(){
        return symbol;
    }
}
