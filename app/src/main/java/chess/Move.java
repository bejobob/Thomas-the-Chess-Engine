/**
 * Move
 * A class representing a move on a chess board
 * @author Benjamin Kealey
 * @version 2026/07/20
 */

package chess;

public class Move {
    int from;
    int to;
    MoveType moveType;
    boolean breaksCastle;

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
     */
    public Move(int from, int to, PieceType pieceType, int captureOn, PieceType captureType, PieceType promotion, MoveType moveType, boolean breaksCastle){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.breaksCastle = breaksCastle;
        this.moveType = moveType;
        switch (moveType) {
            case CAPTURE:
                this.captureOn = captureOn;
                this.captureType = captureType;
                break;
            case SHORTCASTLE:
                break;
            case LONGCASTLE:
                break;
            case PROMOTION:
                this.promotion = promotion;
                break;
            case CAPTURE_PROMOTION:
                this.captureType = captureType; // we don't need to specify the captureOn square because you cannot en-passant and promote
                this.promotion = promotion;
            default:
                break;
        }
    }
    
    /**
     * Returns the move in algebraic notation
     * @param move the move being converted
     * @return the algebraic representation of the move
     */
    public String toAlgebraic(Move move){
        char x = (move.moveType == MoveType.CAPTURE || move.moveType == MoveType.CAPTURE_PROMOTION)? 'x' : '\0';
        char file = (char) ('a' + (move.to % 8));
        int rank = move.to / 8 + 1;
        //TODO: how to add "+" or "#" when the move causes a check
        if (move.moveType == MoveType.SHORTCASTLE){
            return "0-0";
        }
        if (move.moveType == MoveType.LONGCASTLE){
            return "0-0-0";
        }
        return move.pieceType.toString() + x + file + rank;
    }

    public String toString(){
        return toAlgebraic(this);
    }
}
