package chess;

import java.util.ArrayList;

public class legalMoves {

    public static ArrayList<String> algebraicMoves = new ArrayList<>();
    public static Long occupiedCells;

    public static boolean isInCheck(boolean white){
        return false;
    }

    public static void setOccupied(long occupied) {
        occupiedCells = occupied;
    }

    public static String toAlgebraic(int square) {
        char file = (char) ('a' + (square % 8));
        int rank = square / 8 + 1;
        return "" + file + rank;
    }

    public static boolean isOccupied(int square) {
        return (occupiedCells & (1L << square)) != 0;
    }
    
    public static long pawnMoves(int square, long whitePieces, long blackPieces, boolean white) {
        long otherPieces = white ? blackPieces : whitePieces;
        int forward = white ? 8 : -8;
        int leftCapture = white ? 7 : -9;
        int rightCapture = white ? 9 : -7;
        long moves = 0L;

        if ((!isOccupied(square+forward)) && (0 <= (square+forward) && (square+forward) < 64)) {
            moves |= 1L << (square+forward);
            algebraicMoves.add(toAlgebraic(square+forward));
        }
        if ((square / 8 == (white ? 1 : 6)) && (!isOccupied(square+forward)) && (!isOccupied(square+2*forward) && (0 <= (square+2*forward) && (square+2*forward) < 64))) {
            moves |= 1L << (square+2*forward);
            algebraicMoves.add(toAlgebraic(square+2*forward));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) {
            if (0 > (square+leftCapture) || (square+leftCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + leftCapture) % 8)) > 1) {
                moves |= 1L << (square+leftCapture);
                algebraicMoves.add(toAlgebraic(square+leftCapture));
            }
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) {
            if (0 > (square+rightCapture) || (square+rightCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + rightCapture) % 8)) > 1) {
                moves |= 1L << (square+rightCapture);
                algebraicMoves.add(toAlgebraic(square+rightCapture));
            }
        }
        return moves;
    }

    public static long knightMoves(int square, long whitePieces, long blackPieces, boolean white) {
        long otherPieces = white ? blackPieces : whitePieces;
        long moves = 0L;
        int[] knightOffsets = {17, 10, -6, -15, -17, -10, 6, 15};
        for (int offset : knightOffsets) {
            
            if (0 > (square + offset) || (square + offset) >= 64) { // if the move ends up out of bounds
                continue;
            }

            if (Math.abs((square % 8) - ((square + offset) % 8)) > 2) { // if the target square is up on the next row
                continue; // this avoids having the knight jump up a row
            }
            
            if (!isOccupied(square+offset)) { // if there is a piece on the target square
                
                if ((otherPieces & (1L << (square + offset))) == 0L) { // if it is a friendly piece
                    continue;
                } else {
                    moves |= 1L << (square + offset);
                    algebraicMoves.add("Nx" + toAlgebraic(square + offset));
                    return moves;
                }
            }
            moves |= 1L << (square + offset);
            algebraicMoves.add("N" + toAlgebraic(square + offset));
        }
            return moves;
    }

    public static long rookMoves(int square, long whitePices, long blackPieces, boolean white, String letter){
        long otherPieces = white ? blackPieces : whitePices;
        long moves = 0L;
        int[] directions = {8, -8, 1, -1};
        for (int direction : directions) {
            int currentSquare = square;
            while ((currentSquare + direction) >= 0 && (currentSquare + direction) < 64) {
                if (Math.abs((currentSquare % 8) - ((currentSquare + direction) % 8)) > 1){
                    break;
                }

                currentSquare += direction;

                if ((otherPieces & (1L << currentSquare)) != 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + "x" + toAlgebraic(currentSquare));
                    break;
                }
                if (!isOccupied(currentSquare)) {
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
                if (Math.abs((currentSquare % 8) - ((currentSquare + direction) % 8)) > 1){
                    break;
                }
                currentSquare += direction;
                if ((otherPieces & (1L << currentSquare)) != 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + "x" + toAlgebraic(currentSquare));
                    break;
                }
                if (!isOccupied(currentSquare)) {
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
                if (!isOccupied(square+direction) || (otherPieces & (1L << (square + direction))) != 0L) {
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