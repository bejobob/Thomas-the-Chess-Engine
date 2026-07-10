/*
Next things to do:

figure out how to get more data into legalMoves.java. I think we need the entire board.
detect checks.
*/

import chess.legalMoves;

public class Main {

    public static void main(String[] args) {
        int BASE_PAWN_VALUE = 1;
        int BASE_KNIGHT_VALUE = 3;
        int BASE_BISHOP_VALUE = 3;
        int BASE_ROOK_VALUE = 5;
        int BASE_QUEEN_VALUE = 9;
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
        
        long whitePawnsCopy = whitePawns;
        System.out.println("Printing legal moves for white pawns from starting position:");
        while (whitePawnsCopy != 0) {
            int square = Long.numberOfTrailingZeros(whitePawnsCopy);
            //System.out.println("Pawn at square: " + square);
            legalMoves.setOccupied(whitePieces | blackPieces);
            legalMoves.pawnMoves(square, whitePieces, blackPieces, true);
            whitePawnsCopy &= (whitePawnsCopy - 1);
        }
        legalMoves.getAlgebraicMoves().forEach(System.out::println);
        legalMoves.getAlgebraicMoves().clear();
        
    }
}
