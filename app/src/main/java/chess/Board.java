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
    
}
