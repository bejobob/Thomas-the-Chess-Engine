package chess;

import java.util.ArrayList;

public class legalMoves {

    public static ArrayList<String> algebraicMoves = new ArrayList<>();

    public static String toAlgebraic(int square) {
        char file = (char) ('a' + (square % 8));
        int rank = square / 8 + 1;
        return "" + file + rank;
    }
    
    public static long pawnMoves(int square, long whitePieces, long blackPieces, boolean white) {
        long occupied = whitePieces | blackPieces;
        long otherPieces = white ? blackPieces : whitePieces;
        int forward = white ? 8 : -8;
        int leftCapture = white ? 7 : -9;
        int rightCapture = white ? 9 : -7;
        long moves = 0L;
        if (((occupied & (1L << (square+forward))) == 0L) && (0 <= (square+forward) && (square+forward) < 64)) {
            moves |= 1L << (square+forward);
            algebraicMoves.add(toAlgebraic(square+forward));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) {
            moves |= 1L << (square+leftCapture);
            algebraicMoves.add(toAlgebraic(square+leftCapture));
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) {
            moves |= 1L << (square+rightCapture);
            algebraicMoves.add(toAlgebraic(square+rightCapture));
        }
        return moves;
    }

    public static long knightMoves(int square, long whitePieces, long blackPieces, boolean white) {
        long occupied = whitePieces | blackPieces;
        long otherPieces = white ? blackPieces : whitePieces;
        long moves = 0L;
        int[] knightOffsets = {17, 10, -6, -15, -17, -10, 6, 15};
        for (int offset : knightOffsets) {
            if (0 <= (square + offset) && (square + offset) < 64) {
                if ((occupied & (1L << (square + offset))) == 0L || (otherPieces & (1L << (square + offset))) != 0L) {
                    moves |= 1L << (square + offset);
                    algebraicMoves.add("N" + toAlgebraic(square + offset));
                }
            }
        }
        return moves;
    }

    public static long rookMoves(int square, long whitePices, long blackPieces, boolean white, String letter){
        long occupied = whitePices | blackPieces;
        long otherPieces = white ? blackPieces : whitePices;
        long moves = 0L;
        int[] directions = {8, -8, 1, -1};
        for (int direction : directions) {
            int currentSquare = square;
            while ((currentSquare + direction) >= 0 && (currentSquare + direction) < 64) {
                currentSquare += direction;
                if ((otherPieces & (1L << currentSquare)) != 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                    break;
                }
                if ((occupied & (1L << currentSquare)) == 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    public static long bishopMoves(int square, long whitePieces, long blackPieces, boolean white, String letter){
        long occupied = whitePieces | blackPieces;
        long otherPieces = white ? blackPieces : whitePieces;
        long moves = 0L;
        int[] directions = {9, 7, -9, -7};
        for (int direction : directions) {
            int currentSquare = square;
            while ((currentSquare + direction) >= 0 && (currentSquare + direction) < 64) {
                currentSquare += direction;
                if ((otherPieces & (1L << currentSquare)) != 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                    break;
                }
                if ((occupied & (1L << currentSquare)) == 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    public static long queenMoves(int square, long whitePieces, long blackPieces, boolean white){
        return rookMoves(square, whitePieces, blackPieces, white, "Q") | bishopMoves(square, whitePieces, blackPieces, white, "Q");
    }

    public static long kingMoves(int square, long whitePieces, long blackPieces, boolean white){
        long occupied = whitePieces | blackPieces;
        long otherPieces = white ? blackPieces : whitePieces;
        long moves = 0L;
        int[] directions = {8, 1, -1, -8, 9, 7, -7, -9};
        for (int direction : directions) {
            if (0<= (square + direction) && (square + direction) < 64) {
                if ((occupied & (1L << (square + direction))) == 0L || (otherPieces & (1L << (square + direction))) != 0L) {
                    moves |= 1L << (square + direction);
                }
            }
        }
        return moves;
    }

    public static long allLegalMovesBitBoard(int square, long whitePieces, long blackPieces, boolean white) {
        return pawnMoves(square, whitePieces, blackPieces, white) | knightMoves(square, whitePieces, blackPieces, white) | rookMoves(square, whitePieces, blackPieces, white, "R") | bishopMoves(square, whitePieces, blackPieces, white, "B") | queenMoves(square, whitePieces, blackPieces, white) | kingMoves(square, whitePieces, blackPieces, white);
    }

    public static ArrayList<String> allLegalMovesAlgebraic(int square, long whitePieces, long blackPieces, boolean white) {
        algebraicMoves.clear();
        allLegalMovesBitBoard(square, whitePieces, blackPieces, white);
        return new ArrayList<>(algebraicMoves);
    }

    public static ArrayList<String> getAlgebraicMoves(){
        return algebraicMoves;
    }
}