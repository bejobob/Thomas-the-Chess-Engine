/**
 * brain
 * Finds all the legal moves in a position
 * @author Benjamin Kealey
 * @version 2026/07/30
 */

package chess;

import java.security.DrbgParameters.Capability;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class brain {

    private static ArrayList<Move> PmovesL = new ArrayList<>();
    private static ArrayList<Move> movesL = new ArrayList<>();
    private static Map<Board,Integer> positions = new HashMap<>();

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
            return PieceType.KING;
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
                boolean breaksCastle = (pieceType == PieceType.KING || pieceType == PieceType.ROOK);
                if (((white? board.blackPieces : board.whitePieces) & (1L << (square + offset*i))) != 0){ // if the target square contains an enemy piece, add the possible move to the move list, and then break
                    //System.out.println("Enemy"); //uncomment to see step-by-step of move checking
                    PieceType captured = captureType(1L << (square + offset*i), !white, board);
                    if (list) PmovesL.add(new Move(square, square+offset*i, pieceType, square+offset*i, captured, null, MoveType.CAPTURE, breaksCastle));
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
    public static long pawnMoves(int square, boolean white, Board board, boolean list, Game game) {
        long otherPieces = white ? board.blackPieces : board.whitePieces;
        int forward = white ? 8 : -8;
        int leftCapture = white ? 7 : -9;
        int rightCapture = white ? 9 : -7;
        long moves = 0L;

        if ((!isOccupied(square+forward, board)) && (0 <= (square+forward) && (square+forward) < 64)) { // if the square ahead is unoccupied and is on the board
            moves |= 1L << (square+forward);
            if (list) PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, null, MoveType.MOVE, false));
        }
        if ((square / 8 == (white ? 1 : 6)) && (!isOccupied(square+forward, board)) && (!isOccupied(square+2*forward, board) && (0 <= (square+2*forward) && (square+2*forward) < 64))) {
            moves |= 1L << (square+2*forward); // same as before, but for the first double-move
            if (list) PmovesL.add(new Move(square, square+2*forward, PieceType.PAWN, 0, null, null, MoveType.MOVE, false));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) { // if there is a piece on the left capture square
            if (0 > (square+leftCapture) || (square+leftCapture) >= 64) {
                return moves; // if the capture is off the board. We return because we have already checked for going ahead and the double first move.
            }
            if (Math.abs((square % 8) - ((square + leftCapture) % 8)) > 1) { // if we aren't wrapping around the board
                moves |= 1L << (square+leftCapture);
                if (list) PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, leftCapture, captureType(leftCapture, white, board), null, MoveType.CAPTURE, false));
            }
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) { // ditto but on the right
            if (0 > (square+rightCapture) || (square+rightCapture) >= 64) {
                return moves;
            }
            if (Math.abs((square % 8) - ((square + rightCapture) % 8)) > 1) {
                moves |= 1L << (square+rightCapture);
                if (list) PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, rightCapture, captureType(rightCapture, white, board), null, MoveType.CAPTURE, false));
            }
        }
        // from here I'm just doing en-passant stuff
        Move lastMove = game.lastMove();
        if (square/8 == (white ? 4 : 3)){ // if the pawn is a "brave pawn".
            if (lastMove.pieceType == PieceType.PAWN){ // if the last move was not a pawn move, en-passant is not possible
                if (lastMove.from/8 == (white? 6 : 1)){ // if the last move was from the starting position of a pawn
                    if (lastMove.to/8 == (white? 4 : 3)){ // if the last move was to the "coward" row
                        if (lastMove.to == square+1){ // if the pawn moved to the square to the right of the brave pawn
                            moves |= 1L << (square+rightCapture);
                            if (list) PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, square+rightCapture, PieceType.PAWN, null, MoveType.CAPTURE, false));

                        } else if (lastMove.to == square-1){ // if the pawn moved to the square to the left of the brave pawn
                            moves |= 1L << (square+leftCapture);
                            if (list) PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, square+leftCapture, PieceType.PAWN, null, MoveType.CAPTURE, false));
                        }
                    }
                }
            }
        }

        return moves;
    }

    public static boolean canSCastle(boolean white, Board board, long otherMoves, boolean list){
        long king = board.getBitboard(PieceType.KING, white);
        long shortCastle = king << 1 | king << 2;

        if (!(white? board.wO_O : board.bO_O)){ 
            System.out.println("no rigts short");
            return false;} // if the player has already lost rights to O-O

        if (isInCheck(white, otherMoves, board)){
            System.out.println("in check short");
            return false;} // if the player is in check
        
        // check for any piece (either colour) between king and rook
        if ((shortCastle & (board.whitePieces | board.blackPieces)) != 0){
            System.out.println("no path short");
            return false;} // if there are pieces between the king and the rook
        
        if (((shortCastle & otherMoves) != 0)) {
            System.out.println("through check short");
            return false;} // if the player would castle through check

        if (list) PmovesL.add(new Move(white? 4 : 60, white? 6 : 62, PieceType.KING, 0, null, null, MoveType.SHORTCASTLE, true));
        System.out.println("clear???");
        return true;
    }

    public static boolean canLCastle(boolean white, Board board, long otherMoves, boolean list) {
        long king = board.getBitboard(PieceType.KING, white);
        long longCastle = king >> 1 | king >> 2 | king >> 3;
        
        if (!(white? board.wO_O_O : board.bO_O_O)){ // if the player has already lost rights to O-O
            System.out.println("no rights long");
            return false;} // if the player has already lost rights to O-O

        if (isInCheck(white, otherMoves, board)){ 
            System.out.println("in check long");
            return false; }// if the player is in check
        
        if ((longCastle & (board.whitePieces | board.blackPieces)) != 0) {
            System.out.println("no path long");
            return false;} // if there are pieces between the king and the rook
        
        if (((longCastle & otherMoves) != 0)) {
            System.out.println("through check long");
            return false;} // if the player would castle through check

        if (list) PmovesL.add(new Move(white? 4 : 60, white? 2 : 58, PieceType.KING, 0, null, null, MoveType.LONGCASTLE, true));
        System.out.println("clear??");
        return true;
    }

    public static void addCastling(boolean white, Board board, Game game){
        long otherMoves = allPseudoLegalMovesBitBoard(board.whitePieces, board.blackPieces, !white, board, false, game);
        System.out.println(canSCastle(white, board, otherMoves, true));
        System.out.println(canLCastle(white, board, otherMoves, true));
    }

    // END OF THE GENERATION OF PSEUDO-LEGAL MOVES //

    // END-OF-GAME CHECKING //

    public static boolean checkRepetition(Game game){
        return game.threefoldRepetition(); // TODO: move this fully to brain.java? Or is this fine?
    }

    public static boolean stalemate(boolean white, long otherMoves, Board board, Game game){
        if (isInCheck(white, otherMoves, board)) return false; // if the player is in check, then it isn't stalemate
        if (allLegalMoves(board.whitePieces, board.blackPieces, white, board, game).size() != 0) return false; // if they have legal moves, then it isn't stalemate
        return true; // if they are not in check and have no legal moves, then it is stalemate
    }

    public static boolean checkmate(boolean white, long otherMoves, Board board, Game game){
        if (!isInCheck(white, otherMoves, board)) return false; // if the player is not in check, then it sure as heck isn't checkmate
        if (allLegalMoves(board.whitePieces, board.blackPieces, white, board, game).size() != 0) return false; // if they have legal moves, then it isn't checkmate
        return true; // if they are in check and have no legal moves, then it is checkmate
    }

    public static boolean fiftyMoves(Game game){
        for (int i = game.getSelectedMoves().size() - 101; i < game.getSelectedMoves().size(); i++){ // in chess, 1 move is a move for both white and black. My moves are just one colour moving, so we need to go back 100 moves to really go back 50 moves. 101 to avoid range errors.
            if (i < 0) return false; // if fewer than 50 moves have been played, then you can't have played 50 moves without a pawn move or capture
            Move move = game.getSelectedMoves().get(i);
            if (move.pieceType == PieceType.PAWN || move.captureType != null) return false; // if there has been a pawn move or a capture in the last 50 moves then the 50-move rule is not invoked
        }
        return true; // 50 moves without a pawn move or capture!
    }

    // END OF END-OF-GAME CHECKING //

    /**
     * Finds all the pseudo-legal moves
     * @param whitePieces the white pieces
     * @param blackPieces the black piece
     * @param white whether the current turn is white's or black's
     * @param board the board
     * @param list whether we want to update the list of pseudo-legal moves or not. This is to avoid a concurrent modification error
     * @return a bitboard representing all the places !white could move to
     */
    public static long allPseudoLegalMovesBitBoard(long whitePieces, long blackPieces, boolean white, Board board, boolean list, Game game) {
        long pieces = white? whitePieces : blackPieces;
        int square = 0;
        long moves = 0L;
        while (pieces != 0L) {
            //System.out.println("Pieces " + Long.toHexString(pieces)); uncomment to see step-by-step of move checking

            square = Long.numberOfTrailingZeros(pieces);
            if (((1L<<square) & board.getBitboard(PieceType.PAWN, white)) != 0){
                moves |= pawnMoves(square, white, board, list, game);
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
                        board.wO_O_O = false;
                    } else {
                        board.wO_O = false;
                    }
                }
            } else {
                if (move.pieceType == PieceType.KING){
                    board.bO_O = false;
                    board.bO_O_O = false;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        board.bO_O_O = false;
                    } else {
                        board.bO_O = false;
                    }
                }
            }
        }
        if (move.moveType == MoveType.SHORTCASTLE){
            if (white){
                board.setBitboard(PieceType.ROOK, (board.whiteRooks & ~(1L << 7)) | (1L << 5), true);
                board.setBitboard(PieceType.KING, 1L << move.to, true);
            } else {
                board.setBitboard(PieceType.ROOK, (board.blackRooks & ~(1L << 63)) | (1L << 61), false);
                board.setBitboard(PieceType.KING, 1L << move.to, false);
            }
        } else if (move.moveType == MoveType.LONGCASTLE){
            if (white){
                board.setBitboard(PieceType.ROOK, (board.whiteRooks & ~(1L << 0)) | (1L << 3), true);
                board.setBitboard(PieceType.KING, 1L << move.to, true);
            } else {
                board.setBitboard(PieceType.ROOK, (board.blackRooks & ~(1L << 56)) | (1L << 59), false);
                board.setBitboard(PieceType.KING, 1L << move.to, false);
            }
        } else {
            specificPieces &= ~(1L << move.from);
            specificPieces |= 1L << move.to;
            board.setBitboard(move.pieceType, specificPieces, white);
            //System.out.println("Made move: " + move);
        }
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
                        board.wO_O_O = true;
                    } else {
                        board.wO_O = true;
                    }
                }
            } else {
                if (move.pieceType == PieceType.KING){
                    board.bO_O = true;
                    board.bO_O_O = true;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        board.bO_O_O = true;
                    } else {
                        board.bO_O = true;
                    }
                }
            }
        }
        specificPieces &= ~(1L << move.to);
        specificPieces |= 1L << move.from;

        board.setBitboard(move.pieceType, specificPieces, white);
    }

    public static ArrayList<Move> allLegalMoves(long whitePieces, long blackPieces, boolean white, Board board, Game game){
        movesL.clear();
        PmovesL.clear();
        long otherMoves;
        allPseudoLegalMovesBitBoard(whitePieces, blackPieces, white, board, true, game);
        addCastling(white, board, game);
        
        for (Move move : PmovesL){
            if (move.moveType != MoveType.SHORTCASTLE || move.moveType != MoveType.LONGCASTLE){
                System.out.println("Trying move: " + move);
                makeMove(move, white, board);
                otherMoves = allPseudoLegalMovesBitBoard(board.whitePieces, board.blackPieces, !white, board, false, game);
                //printBitboard(otherMoves);

                if (isInCheck(white, otherMoves, board)){
                    System.out.println("Move: " + move + " causes self-check.");
                    if (move.moveType == MoveType.SHORTCASTLE || move.moveType == MoveType.LONGCASTLE){
                        printBitboard(board.getBitboard(PieceType.KING, white));
                    }
                    unMove(move, white, board);
                } else {
                    //System.out.println("Move: " + move + " is clear.");
                    movesL.add(move);
                    //System.out.println("Added move: " + move);
                    unMove(move, white, board);
                }
            } else {
                //TODO: Handle castling. Change how castling works. Detected whether it's valid here instead of in the canCastle methods.
            }
        }
        //System.out.println(canLCastle(white, board, otherMoves, false));
        for (Move move : movesL){
            System.out.println(move);
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
