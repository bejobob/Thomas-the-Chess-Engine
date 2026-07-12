package chess;

import java.util.ArrayList;

public class legalMoves {

    public static ArrayList<String> algebraicMoves = new ArrayList<>();
    public static ArrayList<Move> PmovesL = new ArrayList<>();
    public static ArrayList<Move> movesL = new ArrayList<>();
    public static long blackPieces;
    public static long whitePieces;
    public static long whiteKing;
    public static long blackKing;
    public static Long occupied;
    public static Board board;


    /**
     * Checks if the current player is in check
     * @param white whether the player is white or black, referring to the colour of the pieces they are using and not that of their skin
     * @param moves the bitboard representing all the legal moves for the OTHER player
     * @return boolean, whether the current player is in check or not
     * 
     */
    public static boolean isInCheck(boolean white, long moves){
        long king = white ? whiteKing : blackKing;
        return (king & moves) != 0L;
    }

    public static void loadData(Board board) {
        blackPieces = board.blackPieces;
        whitePieces = board.whitePieces;
        whiteKing = board.whiteKing;
        blackKing = board.blackKing;
        occupied = blackPieces | whitePieces;
        legalMoves.board = board;
    }

    public static String toAlgebraic(int square) {
        char file = (char) ('a' + (square % 8));
        int rank = square / 8 + 1;
        return "" + file + rank;
    }

    public static boolean isOccupied(int square) {
        return (occupied & (1L << square)) != 0;
    }
    /**
     * 
     * @param square the square the pawn is on
     * @param whitePieces // the bitboard representing all the white pieces
     * @param blackPieces // the bitboard representing all the black pieces
     * @param white // is the current player white or black, referring to the colour of the pieces they are using and not that of their skin
     * @return a bitboard representing all the legal moves for the pawn on the given square
     */

    // STARTING FROM HERE WE ARE GENERATING PSEUDO-LEGAL MOVES. THESE DO NOT REPRESENT THE ACTUAL LEGAL MOVES //
    public static long pawnMoves(int square, long whitePieces, long blackPieces, boolean white) {
        long otherPieces = white ? blackPieces : whitePieces;
        int forward = white ? 8 : -8;
        int leftCapture = white ? 7 : -9;
        int rightCapture = white ? 9 : -7;
        long moves = 0L;

        if ((!isOccupied(square+forward)) && (0 <= (square+forward) && (square+forward) < 64)) {
            moves |= 1L << (square+forward);
            algebraicMoves.add(toAlgebraic(square+forward));
            movesL.add(new Move(square, square+forward, "P"));
        }
        if ((square / 8 == (white ? 1 : 6)) && (!isOccupied(square+forward)) && (!isOccupied(square+2*forward) && (0 <= (square+2*forward) && (square+2*forward) < 64))) {
            moves |= 1L << (square+2*forward);
            algebraicMoves.add(toAlgebraic(square+2*forward));
            movesL.add(new Move(square, square+2*forward, "P"));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) {
            if (0 > (square+leftCapture) || (square+leftCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + leftCapture) % 8)) > 1) {
                moves |= 1L << (square+leftCapture);
                algebraicMoves.add(toAlgebraic(square+leftCapture));
                movesL.add(new Move(square, square+leftCapture, "P"));
            }
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) {
            if (0 > (square+rightCapture) || (square+rightCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + rightCapture) % 8)) > 1) {
                moves |= 1L << (square+rightCapture);
                algebraicMoves.add(toAlgebraic(square+rightCapture));
                movesL.add(new Move(square, square+rightCapture, "P"));
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
            
            if (isOccupied(square+offset)) { // if there is a piece on the target square
                
                if ((otherPieces & (1L << (square + offset))) == 0L) { // if it is a friendly piece
                    continue;
                } else {
                    moves |= 1L << (square + offset);
                    algebraicMoves.add("Nx" + toAlgebraic(square + offset));
                    movesL.add(new Move(square, square+offset, "N"));
                    return moves;
                }
            }
            moves |= 1L << (square + offset);
            algebraicMoves.add("N" + toAlgebraic(square + offset));
            movesL.add(new Move(square, square+offset, "N"));
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
                    movesL.add(new Move(square, currentSquare, letter));
                    break;
                }
                if (!isOccupied(currentSquare)) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                    movesL.add(new Move(square, currentSquare, letter));
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    public static long bishopMoves(int square, long whitePieces, long blackPieces, boolean white, String letter){
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
                    movesL.add(new Move(square, currentSquare, letter));
                    break;
                }
                if (!isOccupied(currentSquare)) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                    movesL.add(new Move(square, currentSquare, letter));
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
        long otherPieces = white ? blackPieces : whitePieces;
        long moves = 0L;
        int[] directions = {8, 1, -1, -8, 9, 7, -7, -9};
        for (int direction : directions) {
            if (0<= (square + direction) && (square + direction) < 64) {
                if (!isOccupied(square+direction) || (otherPieces & (1L << (square + direction))) != 0L) {
                    moves |= 1L << (square + direction);
                    algebraicMoves.add("K" + toAlgebraic(square + direction));
                    movesL.add(new Move(square, square+direction, "K"));
                }
            }
        }
        return moves;
    }

    // END OF THE GENERATION OF PSEUDO-LEGAL MOVES //

    public static long allPseudoLegalMovesBitBoard(int square, long whitePieces, long blackPieces, boolean white) {
        long piece = 1L << square;

        if ((piece & board.whitePawns) != 0){
            return pawnMoves(square, whitePieces, blackPieces, white);
        } else if ((piece & board.whiteKnights) != 0){
            return knightMoves(square, whitePieces, blackPieces, white);
        } else if ((piece & board.whiteRooks) != 0){
            return rookMoves(square, whitePieces, blackPieces, white, "R");
        } else if ((piece & board.whiteBishops) != 0){
            return bishopMoves(square, whitePieces, blackPieces, white, "B");
        } else if ((piece & board.whiteQueens) != 0){
            return queenMoves(square, whitePieces, blackPieces, white);
        } else if ((piece & board.whiteKing) != 0){
            return kingMoves(square, whitePieces, blackPieces, white);
        }
        return 0L;
    }

    public static long allLegalMoves(){}

    public static ArrayList<String> allLegalMovesAlgebraic(int square, long whitePieces, long blackPieces, boolean white) {
        algebraicMoves.clear();
        allPseudoLegalMovesBitBoard(square, whitePieces, blackPieces, white);
        return new ArrayList<>(algebraicMoves);
    }

    public static ArrayList<String> getAlgebraicMoves(){
        return algebraicMoves;
    }
}