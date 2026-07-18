package chess;

public class Move {
    int from;
    int to;
    int captureOn;
    String pieceType;
    String captureType;
    String promotion;

    public Move(int from, int to, String pieceType){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;

    }

    public Move(int from, int to, String pieceType, int captureOn, String captureType){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.captureOn = captureOn;
        this.captureType = captureType;
    }

    public Move(int from, int to, String pieceType, String promotion){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.promotion = promotion;
    }

    public Move(int from, int to, String pieceType, int captureOn, String captureType, String promotion){
        this.from = from;
        this.to = to;
        this.pieceType = pieceType;
        this.captureOn = captureOn;
        this.captureType = captureType;
        this.promotion = promotion;
    }
    
    public String toAlgebraic(Move move){
        char file = (char) ('a' + (move.to % 8));
        int rank = move.to / 8 + 1;
        return move.pieceType + file + rank;
    }

    public String toString(){
        return toAlgebraic(this);
    }
}
