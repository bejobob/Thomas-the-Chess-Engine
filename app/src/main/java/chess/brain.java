package chess;

import java.security.DrbgParameters.Capability;
import java.util.ArrayList;

public class brain {

    private static ArrayList<Move> PmovesL = new ArrayList<>();
    private static ArrayList<Move> movesL = new ArrayList<>();

    /**
     * Checks if the current player is in check
     * @param white whether the player is white or black, referring to the colour of the pieces they are using and not that of their skin
     * @param moves the bitboard representing all the legal moves for the OTHER player
     * @return boolean, whether the current player is in check or not
     */
    public static boolean isInCheck(boolean white, long otherMoves, Board board){
        long moves = otherMoves;
        long king = white ? board.whiteKing : board.blackKing;
        return (king & moves) != 0L;
    }
   
    /**
     * Determines whether or not the square in question in occupied by a piece of either colour
     * @param square the square in question
     * @param board the board
     * @return a boolean
     */
    public static boolean isOccupied(int square, Board board) {
        return ((board.whitePieces | board.blackPieces) & (1L << square)) != 0;
    }
    /**
     * Determines the piece type of the captured piece, though this could be used to find the piece type of a piece on a square, not necessarily being captured
     * @param square the square the capture takes place on 
     * @param white whether the player is white or not
     * @param board the board
     * @return a String
     */
    public static PieceType captureType(long square, boolean white, Board board){
        if ((square & (white? board.whitePawns : board.blackPawns)) != 0L){
            return PieceType.PAWN;
        } else if ((square & (white? board.whiteKnights : board.blackKnights)) != 0L){
            return PieceType.KNIGHT;
        } else if ((square & (white? board.whiteBishops : board.blackBishops)) != 0L){
            return PieceType.BISHOP;
        } else if ((square & (white? board.whiteRooks : board.blackRooks)) != 0L){
            return PieceType.ROOK;
        } else if ((square & (white? board.whiteQueens : board.blackQueens)) != 0L){
            return PieceType.QUEEN;
        }else if ((square & (white? board.whiteKing : board.blackKing)) != 0L){
            return PieceType.QUEEN;
        }
        return null;
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
    public static long masterMoves(PieceType pieceType, int square, Board board, boolean white, boolean list){
        int[] offsets = Offsets.getOffsets(pieceType);
        int itter = Offsets.getItter(pieceType);
        long moves = 0L;
        //System.out.println(pieceType + " on square " + square + " (" + Long.toHexString(1L<<square) + ")"); //uncomment to see step-by-step of move checking
        //System.out.println("Itter " + itter); uncomment to see step-by-step of move checking
        for (int offset : offsets){
            //System.out.println("Offset " + offset); uncomment to see step-by-step of move checking
            for (int i = 1; i <= itter; i++){
                
                //System.out.println("Testing square "+ (square+offset*i) + " (" + Long.toHexString(1L<<(square+offset*i)) + ")"); //uncomment to see step-by-step of move checking
                if ((square + offset*i < 0) || (square + offset*i > 63)){ // if the move takes us out of the board
                    //System.out.println("Out of bounds"); uncomment to see step-by-step of move checking
                    break;
                }
                if (Math.abs((square + offset*i)%8-(square + offset*(i-1))%8) > 2){ // if we wrap around the board. I say 2 to permit knights to move properly
                    //System.out.println("Wrap" + Math.abs((square + offset*i)%8-(square + offset*(i-1)))); uncomment to see step-by-step of move checking
                    break;
                }
                if (((white? board.whitePieces : board.blackPieces) & (1L << (square + offset*i))) != 0){ // if the target square contains a friendly piece
                    //System.out.println("Friendly"); uncomment to see step-by-step of move checking
                    break;
                }
                boolean breaksCastle = false;
                if ((pieceType == PieceType.KING || pieceType == PieceType.ROOK)){
                    if ((square%8 == 0 || square%8 == 7 || square%8 == 4)&&(square/8 == 0 || square/8 == 7)) breaksCastle = true;
                }
                if (((white? board.blackPieces : board.whitePieces) & (1L << (square + offset*i))) != 0){ // if the target square contains an enemy piece, add the possible move to the move list, and then break
                    //System.out.println("Enemy"); //uncomment to see step-by-step of move checking
                    PieceType captured = captureType(1L << (square + offset*i), !white, board);
                    if (list) PmovesL.add(new Move(square, square+offset*i, pieceType, square+offset*i, captured, null, MoveType.CAPTURE, breaksCastle)); //TODO: figure out how to identify what the captured piece is so I can remove it from the enemy boards
                    
                    moves |= 1l << square+offset*i;
                    break;
                }
                //System.out.println("Clear"); uncomment to see step-by-step of move checking
                if (list) PmovesL.add(new Move(square, square+offset*i, pieceType, 0, null, null, MoveType.MOVE, breaksCastle));
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

        if ((!isOccupied(square+forward, board)) && (0 <= (square+forward) && (square+forward) < 64)) { // if the square ahead is unoccupied and is on the board
            moves |= 1L << (square+forward);
            PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, null, null, false));
        }
        if ((square / 8 == (white ? 1 : 6)) && (!isOccupied(square+forward, board)) && (!isOccupied(square+2*forward, board) && (0 <= (square+2*forward) && (square+2*forward) < 64))) {
            moves |= 1L << (square+2*forward); // same as before, but for the first double-move
            PmovesL.add(new Move(square, square+2*forward, PieceType.PAWN, 0, null, null, MoveType.MOVE, false));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) { // if there is a piece on the left capture square
            if (0 > (square+leftCapture) || (square+leftCapture) >= 64) {
                return moves; // if the capture is off the board. We return because we have already checked for going ahead and the double first move.
            }
            if (Math.abs((square % 8) - ((square + leftCapture) % 8)) > 1) { // if we aren't wrapping around the board
                moves |= 1L << (square+leftCapture);
                PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, leftCapture, captureType(leftCapture, white, board), null, MoveType.CAPTURE, false));
            }
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) { // ditto but on the right
            if (0 > (square+rightCapture) || (square+rightCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + rightCapture) % 8)) > 1) {
                moves |= 1L << (square+rightCapture);
                PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, rightCapture, captureType(rightCapture, white, board), null, MoveType.CAPTURE, false));
            }
        }
        return moves;
    }

    // END OF THE GENERATION OF PSEUDO-LEGAL MOVES //

    /**
     * Finds all the pseudo-legal moves
     * @param whitePieces the white pieces
     * @param blackPieces the black piece
     * @param white whether the current turn is white's or black's
     * @param board the board
     * @param list whether we want to update the list of pseudo-legal moves or not. This is to avoid a concurrent modification error
     * @return a bitboard representing all the places !white could move to
     */
    public static long allPseudoLegalMovesBitBoard(long whitePieces, long blackPieces, boolean white, Board board, boolean list) {
        movesL.clear();
        long pieces = white? whitePieces : blackPieces;
        int square = 0;
        long moves = 0L;
        while (pieces != 0L) {
            //System.out.println("Pieces " + Long.toHexString(pieces)); uncomment to see step-by-step of move checking

            square = Long.numberOfTrailingZeros(pieces);
            if (((1L<<square) & board.getBitboard(PieceType.PAWN, white)) != 0){
                moves |= pawnMoves(square, white, board);
            } else if (((1L<<square) & board.getBitboard(PieceType.KNIGHT, white)) != 0){
                moves |= masterMoves(PieceType.KNIGHT, square, board, white, list);
            } else if (((1L<<square) & board.getBitboard(PieceType.BISHOP, white)) != 0){
                moves |= masterMoves(PieceType.BISHOP, square, board, white, list);
            } else if (((1L<<square) & board.getBitboard(PieceType.ROOK, white)) != 0){
                moves |= masterMoves(PieceType.ROOK, square, board, white, list);
            } else if (((1L<<square) & board.getBitboard(PieceType.QUEEN, white)) != 0){
                moves |= masterMoves(PieceType.QUEEN, square, board, white, list);
            } else if (((1L<<square) & board.getBitboard(PieceType.KING, white)) != 0){
                moves |= masterMoves(PieceType.KING, square, board, white, list);
            }
            pieces &= (pieces -1);
        }
        return moves;
    }

    public static void makeMove(Move move, boolean white, Board board){
        long specificPieces = board.getBitboard(move.pieceType, white);
        if (move.captureType != null){
            board.setBitboard(move.captureType, board.getBitboard(move.captureType, !white) & ~(1L << move.captureOn), !white);
        }
        if (move.breaksCastle){
            if (white){
                if (move.pieceType == PieceType.KING){
                    board.wO_O = false;
                    board.wO_O_O = false;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        board.wO_O = false;
                    } else {
                        board.wO_O_O = false;
                    }
                }
            } else {
                if (move.pieceType == PieceType.KING){
                    board.bO_O = false;
                    board.bO_O_O = false;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        board.bO_O = false;
                    } else {
                        board.bO_O_O = false;
                    }
                }
            }
        }

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
        if (move.captureType != null){
            board.setBitboard(move.captureType, board.getBitboard(move.captureType, !white) | (1L<< move.captureOn), !white);
        }
        if (move.breaksCastle){
            if (white){
                if (move.pieceType == PieceType.KING){
                    board.wO_O = true;
                    board.wO_O_O = true;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        board.wO_O = true;
                    } else {
                        board.wO_O_O = true;
                    }
                }
            } else {
                if (move.pieceType == PieceType.KING){
                    board.bO_O = true;
                    board.bO_O_O = true;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        board.bO_O = true;
                    } else {
                        board.bO_O_O = true;
                    }
                }
            }
        }
        specificPieces &= ~(1L << move.to);
        specificPieces |= 1L << move.from;

        board.setBitboard(move.pieceType, specificPieces, white);
    }

    public static ArrayList<Move> allLegalMoves(long whitePieces, long blackPieces, boolean white, Board board){
        long otherMoves = allPseudoLegalMovesBitBoard(whitePieces, blackPieces, white, board, true);
        for (Move move : PmovesL){
            System.out.println("Trying move: " + move);
            makeMove(move, white, board);
            otherMoves = allPseudoLegalMovesBitBoard(whitePieces, blackPieces, !white, board, false);
            printBitboard(otherMoves);

            if (isInCheck(white, otherMoves, board)){
                System.out.println("Move: " + move + " causes self-check.");
                unMove(move, white, board);
            } else {
                System.out.println("Move: " + move + " is clear.");
                movesL.add(move);
                unMove(move, white, board);
            }
        }
        return movesL;
    }

    public static void printBitboard(long bitboard) {
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                int square = rank * 8 + file;
                System.out.print(((bitboard >>> square) & 1L) + " ");
            }
            System.out.println();
        }
    }
}
