/**
 * PieceType
 * An ENUM for all the different piece types on a chess board
 * @author Benjamin Kealey
 * @version 2026/08/20
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

    @Override
    public String toString(){
        return symbol;
    }
}
