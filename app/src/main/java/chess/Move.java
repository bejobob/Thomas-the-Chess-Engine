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
}
