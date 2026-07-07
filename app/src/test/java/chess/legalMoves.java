package chess;

public class legalMoves {
    
    public static long pawnMoves(int square, long whitePieces, long blackPieces, boolean white) {
        long occupied = whitePieces | blackPieces;
        long otherPieces = white ? blackPieces : whitePieces;
        int forward = white ? 8 : -8;
        int leftCapture = white ? 7 : -9;
        int rightCapture = white ? 9 : -7;
        long moves = 0L;
        if ((occupied & (1L << (square+forward))) == 0L) {
            moves |= 1L << (square+forward);
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) {
            moves |= 1L << (square+leftCapture);
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) {
            moves |= 1L << (square+rightCapture);
        }
        return moves;
    }
}
