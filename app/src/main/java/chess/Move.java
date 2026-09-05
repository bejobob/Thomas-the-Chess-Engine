/**
 * Move
 * Represents a move made in a game of chess. Can also convert a move into algebraic notation.
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */

package chess;
import java.util.ArrayList;

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
    PieceType promotionType;

    /**
     * A constructor for a NORMAL MOVE
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
    /**
     * A constructor for a NORMAL CAPTURE
     * @param moveType
     * @param from
     * @param to
     * @param captureOn
     * @param pieceType
     * @param captureType
     * @param white
     * @param breaksCastle
     */
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
    /**
     * A constructor for a NORMAL PROMOTION
     * @param moveType
     * @param from
     * @param to
     * @param promotion
     * @param white
     */
    public Move(int from, int to, PieceType promotion, boolean white){
        pieceType = PieceType.PAWN;
        moveType = MoveType.PROMOTION;
        this.from = from;
        this.to = to;
        this.promotionType = promotion;
        this.white = white;
        breaksCastle = false;
    }

    /**
     * A constructor for a CAPTURE PROMOTION
     * @param from
     * @param to
     * @param promotion
     * @param captureType
     * @param white
     */
    public Move(int from, int to, PieceType promotion, PieceType captureType, boolean white){
        pieceType = PieceType.PAWN;
        moveType = MoveType.CAPTURE_PROMOTION;
        this.from = from;
        this.to = to;
        this.promotionType = promotion;
        this.captureType = captureType;
        this.white = white;
        breaksCastle = false;
    }

    public static ArrayList<Move> promotion(int from, int to, boolean white){
        ArrayList<Move> toReturn = new ArrayList<>();
        for (PieceType pieceType : PieceType.values()){
            if (pieceType == PieceType.PAWN || pieceType == PieceType.KING) continue;
            toReturn.add(new Move(from, to, pieceType, white));
        }
        return toReturn;
    }

    public static ArrayList<Move> capturePromotion(int from, int to, PieceType captureType, boolean white){
        ArrayList<Move> toReturn =  new ArrayList<>();
        for (PieceType pieceType : PieceType.values()){
            if (pieceType == PieceType.PAWN || pieceType == PieceType.KING) continue;
            toReturn.add(new Move(from, to, pieceType, captureType, white));
        }
        return toReturn;
    }

    @Override
    public String toString(){
        char fileFrom = (char) ('a' + from%8);
        int rankFrom = from/8+1;
        char fileTo = (char) ('a' + to%8);
        int rankTo = to/8 + 1;
        String promotion =  ((promotionType != null)? (" ="+promotionType.symbol()) : "\0");
        return pieceType.toString() + fileFrom + rankFrom + ((captureType != null)?" takes " : " to ") + fileTo + rankTo + promotion; //
    }
}