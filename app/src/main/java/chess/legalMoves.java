package chess;

import java.util.ArrayList;

public class legalMoves {

    public static ArrayList<String> algebraicMoves = new ArrayList<>();
    public static ArrayList<Move> PmovesL = new ArrayList<>();
    public static ArrayList<Move> movesL = new ArrayList<>();

    public static Long occupied;

    /**
     * Checks if the current player is in check
     * @param white whether the player is white or black, referring to the colour of the pieces they are using and not that of their skin
     * @param moves the bitboard representing all the legal moves for the OTHER player
     * @return boolean, whether the current player is in check or not
     * 
     */
    public static boolean isInCheck(boolean white, long otherMoves, Board board){
        long moves = otherMoves;
        long king = white ? board.whiteKing : board.blackKing;
        return (king & moves) != 0L;
    }

    public static String toAlgebraic(int square) {
        char file = (char) ('a' + (square % 8));
        int rank = square / 8 + 1;
        return "" + file + rank;
    }

    public static boolean isOccupied(int square, Board board) {
        return ((board.whitePieces | board.blackPieces) & (1L << square)) != 0;
    }
    // STARTING FROM HERE WE ARE GENERATING PSEUDO-LEGAL MOVES. THESE DO NOT REPRESENT THE ACTUAL LEGAL MOVES //
    
    /**
     * Finds all the available pseudo-legal moves for a piece
     * @param pieceType the piece type
     * @param square the square the piece is currently on
     * @param board the chess board
     * @param white whether the piece is a white piece or a black piece
     * @return a bitboard representing all the possible pseudo-legal target squares
     */
    public static long masterMoves(String pieceType, int square, Board board, boolean white){
        int[] offsets = Offsets.getOffsets(pieceType);
        int itter = Offsets.getItter(pieceType);
        long moves = 0L;
        System.out.println("Square " + square);
        System.out.println("Itter " + itter);
        for (int offset : offsets){
            System.out.println("Offset " + offset);
            for (int i = 1; i <= itter; i++){
                
                System.out.println("Testing square "+ (square+offset*i) + " (" + Integer.toHexString(square+offset*i) + ")");
                if ((square + offset*i < 0) || (square + offset*i > 63)){ // if the move takes us out of the board
                    System.out.println("Out of bounds");
                    break;
                }
                if (Math.abs((square + offset*i)%8-(square + offset*(i-1))%8) > 2){ // if we wrap around the board. I say 2 to permit knights to move properly
                    System.out.println("Wrap" + Math.abs((square + offset*i)%8-(square + offset*(i-1))));
                    break;
                }
                if (((white? board.whitePieces : board.blackPieces) & (1L << (square + offset*i))) != 0){ // if the target square contains a friendly piece
                    System.out.println("Friendly");
                    break;
                }
                if (((white? board.blackPieces : board.whitePieces) & (1L << (square + offset*i))) != 0){ // if the target square contains an enemy piece, add the possible move to the move list, and then break
                    System.out.println("Enemy");
                    PmovesL.add(new Move(square, square+offset*i, pieceType)); //TODO: figure out how to identify what the captured piece is so I can remove it from the enemy boards
                    moves |= square+offset*i;
                    break;
                }
                System.out.println("Clear");
                PmovesL.add(new Move(square, square+offset*i, pieceType));
                moves |= 1L << square+offset*i;
            }
        }
        return moves;
    }
        
    /**
     * Finds all the available pseudo-legal moves for a pawn. This must be a separate method because of the unique behaviour of pawn movement
     * @param square the square the pawn is on
     * @param whitePieces // the bitboard representing all the white pieces
     * @param blackPieces // the bitboard representing all the black pieces
     * @param white // is the current player white or black, referring to the colour of the pieces they are using and not that of their skin
     * @return a bitboard representing all the legal moves for the pawn on the given square
     */
    public static long pawnMoves(int square, boolean white, Board board) {
        long otherPieces = white ? board.blackPieces : board.whitePieces;
        int forward = white ? 8 : -8;
        int leftCapture = white ? 7 : -9;
        int rightCapture = white ? 9 : -7;
        long moves = 0L;

        if ((!isOccupied(square+forward, board)) && (0 <= (square+forward) && (square+forward) < 64)) {
            moves |= 1L << (square+forward);
            algebraicMoves.add(toAlgebraic(square+forward));
            PmovesL.add(new Move(square, square+forward, "P"));
        }
        if ((square / 8 == (white ? 1 : 6)) && (!isOccupied(square+forward, board)) && (!isOccupied(square+2*forward, board) && (0 <= (square+2*forward) && (square+2*forward) < 64))) {
            moves |= 1L << (square+2*forward);
            algebraicMoves.add(toAlgebraic(square+2*forward));
            PmovesL.add(new Move(square, square+2*forward, "P"));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) {
            if (0 > (square+leftCapture) || (square+leftCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + leftCapture) % 8)) > 1) {
                moves |= 1L << (square+leftCapture);
                algebraicMoves.add(toAlgebraic(square+leftCapture));
                PmovesL.add(new Move(square, square+leftCapture, "P"));
            }
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) {
            if (0 > (square+rightCapture) || (square+rightCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + rightCapture) % 8)) > 1) {
                moves |= 1L << (square+rightCapture);
                algebraicMoves.add(toAlgebraic(square+rightCapture));
                PmovesL.add(new Move(square, square+rightCapture, "P"));
            }
        }
        return moves;
    }

    /* deprecated pseudo-legal move generators
    public static long knightMoves(int square, boolean white, Board board) {
        long otherPieces = white ? board.blackPieces : board.whitePieces;
        long moves = 0L;
        int[] knightOffsets = {17, 10, -6, -15, -17, -10, 6, 15};
        for (int offset : knightOffsets) {
            
            if (0 > (square + offset) || (square + offset) >= 64) { // if the move ends up out of bounds
                continue;
            }

            if (Math.abs((square % 8) - ((square + offset) % 8)) > 2) { // if the target square is up on the next row
                continue; // this avoids having the knight jump up a row
            }
            
            if (isOccupied(square+offset, board)) { // if there is a piece on the target square
                
                if ((otherPieces & (1L << (square + offset))) == 0L) { // if it is a friendly piece
                    continue;
                } else {
                    moves |= 1L << (square + offset);
                    algebraicMoves.add("Nx" + toAlgebraic(square + offset));
                    PmovesL.add(new Move(square, square+offset, "N"));
                    return moves;
                }
            }
            moves |= 1L << (square + offset);
            algebraicMoves.add("N" + toAlgebraic(square + offset));
            PmovesL.add(new Move(square, square+offset, "N"));
        }
            return moves;
    }

    public static long rookMoves(int square, boolean white, String letter, Board board){
         
        

        long otherPieces = white ? board.blackPieces : board.whitePieces;
        long moves = 0L;
        int[] directions = {8, -8, 1, -1};
        
        for (int direction : directions) {
            System.out.println("Starting square: " + square + " (" + toAlgebraic(square) + ")");
            System.out.println("Direction: " + direction); 
            int currentSquare = square;
            System.out.println(    "currentSquare = " + currentSquare +    " (" + toAlgebraic(currentSquare) + ")");

            while ((currentSquare + direction) >= 0 && (currentSquare + direction) < 64) {
                if (Math.abs((currentSquare % 8) - ((currentSquare + direction) % 8)) > 1){
                    break;
                }

                currentSquare += direction;
                //System.out.println(currentSquare - square);

                if ((otherPieces & (1L << currentSquare)) != 0L) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + "x" + toAlgebraic(currentSquare));
                    PmovesL.add(new Move(square, currentSquare, letter));
                    break;
                }
                System.out.println("Checking: " + toAlgebraic(currentSquare));
                System.out.printf("whitePieces = %016X%n", board.whitePieces);
                System.out.printf("blackPieces = %016X%n", board.blackPieces);
                System.out.println("Occupied? " + isOccupied(currentSquare, board));
                if (!isOccupied(currentSquare, board)) {
                    //System.out.println("marker");
                    //System.out.println(Long.toHexString(1L << currentSquare));
                    //System.out.println(Long.toHexString(board.whiteKing));
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                    PmovesL.add(new Move(square, currentSquare, letter));
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    public static long bishopMoves(int square, boolean white, String letter, Board board){
        long otherPieces = white ? board.blackPieces : board.whitePieces;
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
                    PmovesL.add(new Move(square, currentSquare, letter));
                    break;
                }
                if (!isOccupied(currentSquare, board)) {
                    moves |= 1L << currentSquare;
                    algebraicMoves.add(letter + toAlgebraic(currentSquare));
                    PmovesL.add(new Move(square, currentSquare, letter));
                } else {
                    break;
                }
            }
        }
        return moves;
    }

    public static long queenMoves(int square, boolean white, Board board){
        return rookMoves(square, white, "Q", board) | bishopMoves(square, white, "Q", board);
    }

    public static long kingMoves(int square, boolean white, Board board){
        long otherPieces = white ? board.blackPieces : board.whitePieces;
        long moves = 0L;
        int[] directions = {8, 1, -1, -8, 9, 7, -7, -9};
        for (int direction : directions) {
            if (0<= (square + direction) && (square + direction) < 64) {
                if (!isOccupied(square+direction, board) || (otherPieces & (1L << (square + direction))) != 0L) {
                    moves |= 1L << (square + direction);
                    algebraicMoves.add("K" + toAlgebraic(square + direction));
                    PmovesL.add(new Move(square, square+direction, "K"));
                }
            }
        }
        return moves;
    }
        */

    // END OF THE GENERATION OF PSEUDO-LEGAL MOVES //

    public static long allPseudoLegalMovesBitBoard(long whitePieces, long blackPieces, boolean white, Board board) {
        movesL.clear();
        long pieces = white? whitePieces : blackPieces;
        int square = 0;
        long moves = 0L;
        while (pieces != 0L) {
            System.out.println("Pieces " + Long.toHexString(pieces));
            //System.out.println(Long.toHexString(board.whiteKing));
            //System.out.println("test");
            square = Long.numberOfTrailingZeros(pieces);
            //System.out.println(square);
            if ((pieces & board.getBitboard("P", white)) != 0){
                moves |= pawnMoves(square, white, board);
            } else if ((pieces & board.getBitboard("N", white)) != 0){
                moves |= masterMoves("N", square, board, white);
            } else if ((pieces & board.getBitboard("R", white)) != 0){
                moves |= masterMoves("R", square, board, white);
            } else if ((pieces & board.getBitboard("B", white)) != 0){
                moves |= masterMoves("B", square, board, white);
            } else if ((pieces & board.getBitboard("Q", white)) != 0){
                moves |= masterMoves("Q", square, board, white);
            } else if ((pieces & board.getBitboard("K", white)) != 0){
                moves |= masterMoves("K", square, board, white);
            }
            pieces &= (pieces -1);
            //System.out.println(Long.toHexString(pieces));
        }
        System.out.println(PmovesL.size());
        return moves;
    }

    public static void makeMove(Move move, boolean white, Board board){
        long specificPieces = board.getBitboard(move.pieceType, white);
        specificPieces &= ~(1L << move.from);
        specificPieces |= 1L << move.to;
        board.setBitboard(move.pieceType, specificPieces, white);
    }
    /**
     * This methods assumes the move has already been made, and undoes it.
     * @param move the move that must be undone
     * @param white whether it is white's turn or not
     */
    public static void unMove(Move move, boolean white, Board board){
        long specificPieces = board.getBitboard(move.pieceType, white);
        specificPieces &= ~(1L << move.to);
        specificPieces |= 1L << move.from;

        board.setBitboard(move.pieceType, specificPieces, white);
    }

    public static ArrayList<Move> allLegalMoves(long whitePieces, long blackPieces, boolean white, Board board){
        long otherMoves = allPseudoLegalMovesBitBoard(whitePieces, blackPieces, white, board);
        for (Move move : PmovesL){
            makeMove(move, white, board);
            if (isInCheck(white, otherMoves, board)){
                unMove(move, white, board);
            } else {
                movesL.add(move);
            }
        }
        return movesL;
    }

    public static ArrayList<String> allLegalMovesAlgebraic(int square, long whitePieces, long blackPieces, boolean white, Board board) {
        algebraicMoves.clear();
        allPseudoLegalMovesBitBoard(whitePieces, blackPieces, white, board);
        return new ArrayList<>(algebraicMoves);
    }

    public static ArrayList<String> getAlgebraicMoves(){
        return algebraicMoves;
    }
}