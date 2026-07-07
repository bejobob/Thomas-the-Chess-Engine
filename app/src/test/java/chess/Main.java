package chess;

public class Main {
    long whitePawns = 0x000000000000FF00L;
    long whiteRooks = 0x0000000000000081L;
    long whiteKnights = 0x0000000000000042L;
    long whiteBishops = 0x0000000000000024L;
    long whiteQueens = 0x0000000000000008L;
    long whiteKing = 0x0000000000000010L;
    long whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;

    long blackPawns = 0x00FF000000000000L;
    long blackRooks = 0x8100000000000000L;
    long blackKnights = 0x4200000000000000L;
    long blackBishops = 0x2400000000000000L;
    long blackQueens = 0x0800000000000000L;
    long blackKing = 0x1000000000000000L;
    long blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing; 
    public static void main(String[] args) {
        System.out.println("test");
    }
}
