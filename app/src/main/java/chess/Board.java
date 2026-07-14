package chess;

public class Board {
    public long whitePawns;
    public long whiteRooks;
    public long whiteKnights;
    public long whiteBishops;
    public long whiteQueens;
    public long whiteKing;
    public long blackPawns;
    public long blackRooks;
    public long blackKnights;
    public long blackBishops;
    public long blackQueens;
    public long blackKing;
    public long whitePieces;
    public long blackPieces;

    public Board(long whitePawns, long whiteRooks, long whiteKnights, long whiteBishops, long whiteQueens, long whiteKing,
                 long blackPawns, long blackRooks, long blackKnights, long blackBishops, long blackQueens, long blackKing) {
        this.whitePawns = whitePawns;
        this.whiteRooks = whiteRooks;
        this.whiteKnights = whiteKnights;
        this.whiteBishops = whiteBishops;
        this.whiteQueens = whiteQueens;
        this.whiteKing = whiteKing;
        this.blackPawns = blackPawns;
        this.blackRooks = blackRooks;
        this.blackKnights = blackKnights;
        this.blackBishops = blackBishops;
        this.blackQueens = blackQueens;
        this.blackKing = blackKing;
        this.whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;
        this.blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;
    }

    public Long getBitboard(String pieceType, boolean white){
        if (pieceType.equals("P")){
            return white? whitePawns : blackPawns;
        } else if (pieceType.equals("N")){
            return white? whiteKnights : blackKnights;
        } else if (pieceType.equals("B")){
            return white? whiteBishops : blackBishops;
        } else if (pieceType.equals("R")){
            return white? whiteRooks : blackRooks;
        } else if (pieceType.equals("Q")){
            return white? whiteQueens : blackQueens;
        } else {
            return white? whiteKing : blackKing;
        }
    }

    public void setBitboard(String pieceType, Long val, boolean white){
        switch (pieceType) {
            case "pawn":
                if (white){whitePawns = val;} else {blackPawns = val;}
                break;
            case "knight":
                if (white){whiteKnights = val;} else {blackKnights = val;}
                break;
            case "bishop":
                if (white){whiteBishops = val;} else {blackBishops = val;}
                break;
            case "rook":
                if (white){whiteRooks = val;} else {blackRooks = val;}
                break;
            case "queen":
                if (white){whiteQueens = val;} else {blackQueens = val;}
                break;
            case "king":
                if (white){whiteKing = val;} else {blackKing = val;}
                break;
            default:
                break;
        }
        
        if (white){
            whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;
        } else {
            blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;

        }
    }
}
