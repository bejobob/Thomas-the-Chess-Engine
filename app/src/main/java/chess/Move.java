/**
 * Move
 * Represents a move made in a game of chess. Can also convert a move into algebraic notation.
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */

package chess;

public class Move {
    int from;
    int to;
    MoveType moveType;
    boolean breaksCastle;
    boolean white;

    // these are optional fields:
    int captureOn;
    PieceType pieceType;
    PieceType captureType;
    PieceType promotion;

    /**
     * @param from the starting square
     * @param to the target square
     * @param pieceType the type of piece that is moving
     * @param captureOn the square a capture takes place on. This is separate from moveTo because of en-passant
     * @param captureType the type of piece that is being captured
     * @param promotion the type of piece that a pawn is promoting to
     * @param moveType the type of move
     * @param breaksCastle whether or not this move breaks castling rights.
     * @param white whether the move belongs to the white player or the black player
     */
    public Move(MoveType moveType, int from, int to, PieceType pieceType, boolean white, boolean breaksCastle){
        this.moveType = MoveType.MOVE;
        this.from = from;
        this. to = to;
        this.pieceType = pieceType;
        this.white = white;
        this.breaksCastle = breaksCastle;
    }

    public Move(MoveType moveType, int from, int to, int captureOn, PieceType pieceType, PieceType captureType, boolean white, boolean breaksCastle){
        this.moveType = moveType;
        this.from = from;
        this.to = to;
        this.captureOn = captureOn;
        this.pieceType = pieceType;
        this.captureType = captureType;
        this.white = white;
        this.breaksCastle = breaksCastle;
    }

    @Override
    public String toString(){
        char fileFrom = (char) ('a' + from%8);
        int rankFrom = from/8+1;
        char fileTo = (char) ('a' + to%8);
        int rankTo = to/8 + 1;
        return pieceType.toString() + fileFrom + rankFrom + ((this.moveType == MoveType.CAPTURE)?" takes " : " to ") + fileTo + rankTo; //
    }
}
