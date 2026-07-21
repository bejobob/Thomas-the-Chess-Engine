package chess;
/**
 * Move
 * A class representing a move on a chess board
 * @author Benjamin Kealey
 * @version 2026/07/20
 */
public class Move {
    int from;
    int to;

    // these are optional fields:
    int captureOn;
    PieceType pieceType;
    PieceType captureType;
    PieceType promotion;

    /**
     * Default move constructor. This assumes there is no capture or promotion
     * @param from the starting square
     * @param to the target square
     * @param pieceType the piece type
     */
    public Move(int from, int to, PieceType pieceType){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
    }

    public Move(int from, int to, PieceType pieceType, int captureOn, PieceType captureType){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.captureOn = captureOn;
        this.captureType = captureType;
        System.out.println("Capture created: pieceType = " + pieceType);

    }

    /**
     * Move constructor for a pawn promoting
     * @param from the starting square
     * @param to the target square
     * @param pieceType the piece type, though this will always be a pawn in the case of a promotion
     * @param promotion the type of piece the pawn is becoming
     * 
     * TODO: figure out if I can use the default constructor for this? If I remove the piecetype because it's always a pawn, it's the same as the first...
     */
    public Move(int from, int to, PieceType pieceType, PieceType promotion){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.promotion = promotion;
    }

    /**
     * Move constructor for a pawn that captures a piece and promotes on the same move
     * @param from the starting square
     * @param to the target square
     * @param pieceType the type of piece being moved
     * @param captureOn the square the capture takes place on
     * @param captureType the type of piece that was captured
     * @param promotion the type of piece that the pawn in promoting into
     */
    public Move(int from, int to, PieceType pieceType, int captureOn, PieceType captureType, PieceType promotion){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.captureOn = captureOn;
        this.captureType = captureType;
        this.promotion = promotion;
    }
    
    /**
     * Returns the move in algebraic notation
     * @param move the move being converted
     * @return the algebraic representation of the move
     */
    public String toAlgebraic(Move move){
        char file = (char) ('a' + (move.to % 8));
        int rank = move.to / 8 + 1;
        return move.pieceType.toString() + file + rank;
    }

    public String toString(){
        return toAlgebraic(this);
    }
}
