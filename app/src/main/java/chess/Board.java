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

    public boolean wO_O = true;
    public boolean wO_O_O = true;
    public boolean bO_O = true;
    public boolean bO_O_O = true;

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
        whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;
        blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;
    }

    public long getBitboard(PieceType pieceType, boolean white){
        if (pieceType.equals(PieceType.PAWN)){
            return white? whitePawns : blackPawns;
        } else if (pieceType.equals(PieceType.KNIGHT)){
            return white? whiteKnights : blackKnights;
        } else if (pieceType.equals(PieceType.BISHOP)){
            return white? whiteBishops : blackBishops;
        } else if (pieceType.equals(PieceType.ROOK)){
            return white? whiteRooks : blackRooks;
        } else if (pieceType.equals(PieceType.QUEEN)){
            return white? whiteQueens : blackQueens;
        } else if (pieceType.equals(PieceType.KING)) {
            return white? whiteKing : blackKing;
        }
        return 0L;
    }

    public void setBitboard(PieceType pieceType, Long val, boolean white){
        switch (pieceType) {
            case PAWN:
                if (white){whitePawns = val;} else {blackPawns = val;}
                break;
            case KNIGHT:
                if (white){whiteKnights = val;} else {blackKnights = val;}
                break;
            case BISHOP:
                if (white){whiteBishops = val;} else {blackBishops = val;}
                break;
            case ROOK:
                if (white){whiteRooks = val;} else {blackRooks = val;}
                break;
            case QUEEN:
                if (white){whiteQueens = val;} else {blackQueens = val;}
                break;
            case KING:
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
