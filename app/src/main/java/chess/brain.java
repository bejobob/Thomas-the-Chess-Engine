/**
 * brain
 * Finds all the legal moves in a position
 * @author Benjamin Kealey
 * @version 2026/08/20
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
     * @param otherMoves the bitboard representing the possible moves for the other player
     * @param game the game state. Includes the board and the turn.
     * @return boolean, whether the current player is in check or not
     */
    public static boolean isInCheck(long otherMoves,  Game game){
        long moves = otherMoves;
        long king = game.isWhiteToMove() ? game.getBoard().whiteKing : game.getBoard().blackKing;
        return (king & moves) != 0L;
    }
   
    /**
     * Determines whether or not the square in question in occupied by a piece of either colour
     * @param square the square in question
     * @param game the game state. Includes the board and the turn.
     * @return a boolean
     */
    public static boolean isOccupied(int square, Game game) {
        return ((game.getBoard().whitePieces | game.getBoard().blackPieces) & (1L << square)) != 0;
    }
    /**
     * Determines the piece type of the captured piece, though this could be used to find the piece type of a piece on a square, not necessarily being captured
     * @param square the square the capture takes place on 
     * @param game the game state. Includes the board and the turn.
     * @return a String
     */
    public static PieceType captureType(long square, Game game){
        if ((square & (game.isWhiteToMove()? game.getBoard().whitePawns : game.getBoard().blackPawns)) != 0L){
            return PieceType.PAWN;
        } else if ((square & (game.isWhiteToMove()? game.getBoard().whiteKnights : game.getBoard().blackKnights)) != 0L){
            return PieceType.KNIGHT;
        } else if ((square & (game.isWhiteToMove()? game.getBoard().whiteBishops : game.getBoard().blackBishops)) != 0L){
            return PieceType.BISHOP;
        } else if ((square & (game.isWhiteToMove()? game.getBoard().whiteRooks : game.getBoard().blackRooks)) != 0L){
            return PieceType.ROOK;
        } else if ((square & (game.isWhiteToMove()? game.getBoard().whiteQueens : game.getBoard().blackQueens)) != 0L){
            return PieceType.QUEEN;
        }else if ((square & (game.isWhiteToMove()? game.getBoard().whiteKing : game.getBoard().blackKing)) != 0L){
            return PieceType.KING;
        }
        return null;
    }
    // STARTING FROM HERE WE ARE GENERATING PSEUDO-LEGAL MOVES. THESE DO NOT REPRESENT THE ACTUAL LEGAL MOVES //
    
    /**
     * Finds all the available pseudo-legal moves for a piece
     * @param pieceType the piece type
     * @param square the square the piece is currently on
     * @param game the game state. Includes the board and the turn.
     * @param list whether or not to add the moves to the list of pseudo-legal moves. This is to avoid concurrent modification errors
     * @return a bitboard representing all the possible pseudo-legal target squares
     */
    public static long masterMoves(PieceType pieceType, int square, Game game, boolean list){
        int[] offsets = Offsets.getOffsets(pieceType);
        int itter = Offsets.getItter(pieceType);
        long moves = 0L;
        for (int offset : offsets){
            for (int i = 1; i <= itter; i++){
                
                if ((square + offset*i < 0) || (square + offset*i > 63)){ // if the move takes us out of the board
                    break;
                }
                if (Math.abs((square + offset*i)%8-(square + offset*(i-1))%8) > 2){ // if we wrap around the game.getBoard(). I say 2 to permit knights to move properly
                    break;
                }
                if (((game.isWhiteToMove()? game.getBoard().whitePieces : game.getBoard().blackPieces) & (1L << (square + offset*i))) != 0){ // if the target square contains a friendly piece
                    break;
                }
                boolean breaksCastle = (pieceType == PieceType.KING || pieceType == PieceType.ROOK);
                if (((game.isWhiteToMove()? game.getBoard().blackPieces : game.getBoard().whitePieces) & (1L << (square + offset*i))) != 0){ // if the target square contains an enemy piece, add the possible move to the move list, and then break
                    PieceType captured = captureType(1L << (square + offset*i), game);
                    if (list) PmovesL.add(new Move(square, square+offset*i, pieceType, square+offset*i, captured, null, MoveType.CAPTURE, breaksCastle));
                    moves |= 1l << square+offset*i;
                    break;
                }
                if (list) PmovesL.add(new Move(square, square+offset*i, pieceType, 0, null, null, MoveType.MOVE, breaksCastle));
                moves |= 1L << square+offset*i;
            }
        }
        return moves;
    }
        
    /**
     * Finds all the available pseudo-legal moves for a pawn. This must be a separate method because of the unique behaviour of pawn movement
     * @param square the square the pawn is on
     * @param game the game state. Includes the board and the turn.
     * @param list whether or not to add the moves to the list of pseudo-legal moves. This is to avoid concurrent modification errors
     * @return a bitboard representing all the legal moves for the pawn on the given square
     */
    public static long pawnMoves(int square, Game game, boolean list) {
        long otherPieces = game.isWhiteToMove()? game.getBoard().blackPieces : game.getBoard().whitePieces;
        int forward = game.isWhiteToMove()? 8 : -8;
        int leftCapture = game.isWhiteToMove()? 7 : -9;
        int rightCapture = game.isWhiteToMove()? 9 : -7;
        long moves = 0L;

        if ((!isOccupied(square+forward, game)) && (0 <= (square+forward) && (square+forward) < 64)) { // if the square ahead is unoccupied and is on the board
            if ((square + forward) / 8 == (game.isWhiteToMove()? 7 : 0)) { // if the pawn is moving to the last rank
                if (list) {
                    PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, PieceType.QUEEN, MoveType.PROMOTION, false));
                    PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, PieceType.ROOK, MoveType.PROMOTION, false));
                    PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, PieceType.BISHOP, MoveType.PROMOTION, false));
                    PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, PieceType.KNIGHT, MoveType.PROMOTION, false));
                } // we don't update the bitboard here because pawns can't capture forwards, only diagonally
            } else {
                if (list) PmovesL.add(new Move(square, square+forward, PieceType.PAWN, 0, null, null, MoveType.MOVE, false));
            }
        }
        if ((square / 8 == (game.isWhiteToMove()? 1 : 6)) && (!isOccupied(square+forward, game)) && (!isOccupied(square+2*forward, game) && (0 <= (square+2*forward) && (square+2*forward) < 64))) {
            moves |= 1L << (square+2*forward); // same as before, but for the first double-move
            if (list) PmovesL.add(new Move(square, square+2*forward, PieceType.PAWN, 0, null, null, MoveType.MOVE, false));
        }
        if ((otherPieces & (1L << (square+leftCapture))) != 0L) { // if there is a piece on the left capture square
            
            if (Math.abs((square % 8) - ((square + leftCapture) % 8)) <= 1) { // if we aren't wrapping around the board
                
                if ((square + leftCapture) / 8 == (game.isWhiteToMove()? 7 : 0)) { // if the pawn is moving to the last rank
                    if (list) {
                        PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, square+leftCapture, captureType(leftCapture, game), PieceType.QUEEN, MoveType.CAPTURE_PROMOTION, false));
                        PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, square+leftCapture, captureType(leftCapture, game), PieceType.ROOK, MoveType.CAPTURE_PROMOTION, false));
                        PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, square+leftCapture, captureType(leftCapture, game), PieceType.BISHOP, MoveType.CAPTURE_PROMOTION, false));
                        PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, square+leftCapture, captureType(leftCapture, game), PieceType.KNIGHT, MoveType.CAPTURE_PROMOTION, false));
                    }
                } else {
                    if (list) PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, square+leftCapture, captureType(leftCapture, game), null, MoveType.CAPTURE, false));
                }
                moves |= 1L << (square+leftCapture);
                if (list) PmovesL.add(new Move(square, square+leftCapture, PieceType.PAWN, leftCapture, captureType(leftCapture, game), null, MoveType.CAPTURE, false));
            }
        }
        if ((otherPieces & (1L << (square+rightCapture))) != 0L) { // ditto but on the right
            if (Math.abs((square % 8) - ((square + rightCapture) % 8)) <= 1) {
                if ((square + rightCapture) / 8 == (game.isWhiteToMove()? 7 : 0)) { // if the pawn is moving to the last rank
                    if (list) {
                        PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, square+rightCapture, captureType(rightCapture, game), PieceType.QUEEN, MoveType.CAPTURE_PROMOTION, false));
                        PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, square+rightCapture, captureType(rightCapture, game), PieceType.ROOK, MoveType.CAPTURE_PROMOTION, false));
                        PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, square+rightCapture, captureType(rightCapture, game), PieceType.BISHOP, MoveType.CAPTURE_PROMOTION, false));
                        PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, square+rightCapture, captureType(rightCapture, game), PieceType.KNIGHT, MoveType.CAPTURE_PROMOTION, false));
                    }
                } else {
                    if (list) PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, square+rightCapture, captureType(rightCapture, game), null, MoveType.CAPTURE, false));
                }
                moves |= 1L << (square+rightCapture);
                if (list) PmovesL.add(new Move(square, square+rightCapture, PieceType.PAWN, rightCapture, captureType(rightCapture, game), null, MoveType.CAPTURE, false));
            }
        }
        // from here I'm just doing en-passant stuff
        Move lastMove = game.lastMove();
        if (square/8 == (game.isWhiteToMove()? 4 : 3)){ // if the pawn is a "brave pawn".
            if (lastMove.pieceType == PieceType.PAWN){ // if the last move was not a pawn move, en-passant is not possible
                if (lastMove.from/8 == (game.isWhiteToMove()? 6 : 1)){ // if the last move was from the starting position of a pawn
                    if (lastMove.to/8 == (game.isWhiteToMove()? 4 : 3)){ // if the last move was to the "coward" row
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

    /**
     * Determines whether the player can castle short
     * @param game the game state. Includes the board and the turn.
     * @param otherMoves the bitboard representing the possible moves for the other player
     * @param list whether or not to add the castling moves to the list of pseudo-legal moves. This is to avoid concurrent modification errors
     * @return whether or not the player can castle short
     */
    public static boolean canSCastle(Game game, long otherMoves, boolean list){
        long king = game.getBoard().getBitboard(PieceType.KING, game.isWhiteToMove());
        long shortCastle = king << 1 | king << 2;

        if (!(game.isWhiteToMove()? game.getBoard().wO_O : game.getBoard().bO_O)){ 
            return false;} // if the player has already lost rights to O-O

        if (isInCheck(otherMoves, game)){
            return false;} // if the player is in check
        
        // check for any piece (either colour) between king and rook
        if ((shortCastle & (game.getBoard().whitePieces | game.getBoard().blackPieces)) != 0){
            return false;} // if there are pieces between the king and the rook
        
        if (((shortCastle & otherMoves) != 0)) {
            return false;} // if the player would castle through check

        if (list) PmovesL.add(new Move(game.isWhiteToMove()? 4 : 60, game.isWhiteToMove()? 6 : 62, PieceType.KING, 0, null, null, MoveType.SHORTCASTLE, true));
        return true;
    }

    /**
     * Determines if the player can castle long
     * @param game the game state. Includes the board and the turn.
     * @param otherMoves the bitboard representing the possible moves for the other player
     * @param list whether or not to add the castling moves to the list of pseudo-legal moves. This is to avoid concurrent modification errors
     * @return whether the player can castle long
     */
    public static boolean canLCastle(Game game, long otherMoves, boolean list) {
        long king = game.getBoard().getBitboard(PieceType.KING, game.isWhiteToMove());
        long longCastle = king >> 1 | king >> 2 | king >> 3;
        
        if (!(game.isWhiteToMove()? game.getBoard().wO_O_O : game.getBoard().bO_O_O)){ // if the player has already lost rights to O-O
            return false;} // if the player has already lost rights to O-O

        if (isInCheck(otherMoves, game)){ 
            return false; }// if the player is in check
        
        if ((longCastle & (game.getBoard().whitePieces | game.getBoard().blackPieces)) != 0) {
            return false;} // if there are pieces between the king and the rook
        
        if (((longCastle & otherMoves) != 0)) {
            return false;} // if the player would castle through check

        if (list) PmovesL.add(new Move(game.isWhiteToMove()? 4 : 60, game.isWhiteToMove()? 2 : 58, PieceType.KING, 0, null, null, MoveType.LONGCASTLE, true));
        return true;
    }

    /**
     * Adds the castinling moves to the list of pseudo-legal moves (if they are legal moves)
     * @param game the game state. Includes the board and the turn.
     */
    public static void addCastling(Game game){
        long otherMoves = allPseudoLegalMovesBitBoard(game, false);
        canSCastle(game, otherMoves, true);
        canLCastle(game, otherMoves, true);
    }

    // END OF THE GENERATION OF PSEUDO-LEGAL MOVES //

    // END-OF-GAME CHECKING //

    /**
     * Determines whether the game has ended due to threefold repetition
     * @param game the game state. Includes the board and the turn.
     * @return whether the game has ended due to threefold repetition
     */
    public static boolean checkRepetition(Game game){
        return game.threefoldRepetition(); // TODO: move this fully to brain.java? Or is this fine?
    }

    /**
     * Determines whether the game has ended due to stalemate
     * @param otherMoves the bitboard representing the possible moves for the other player
     * @param game the game state. Includes the board and the turn.
     * @return whether the game has ended due to stalemate
     */
    public static boolean stalemate(long otherMoves, Game game){
        if (isInCheck(otherMoves, game)) return false; // if the player is in check, then it isn't stalemate
        if (allLegalMoves(game).size() != 0) return false; // if they have legal moves, then it isn't stalemate
        return true; // if they are not in check and have no legal moves, then it is stalemate
    }

    /**
     * Determines whether the game has ended due to checkmate
     * @param otherMoves the bitboard representing the possible moves for the other player
     * @param game the game state. Includes the board and the turn.
     * @return whether the game has ended due to checkmate
     */
    public static boolean checkmate(long otherMoves, Game game){
        if (!isInCheck(otherMoves, game)) return false; // if the player is not in check, then it sure as heck isn't checkmate
        if (allLegalMoves(game).size() != 0) return false; // if they have legal moves, then it isn't checkmate
        return true; // if they are in check and have no legal moves, then it is checkmate
    }

    /**
     * Determines whether the game has ended due to the 50-move rule
     * @param game the game state. Includes the board and the turn.
     * @return whether the game has ended due to the 50-move rule
     */
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
     * @param game the game state. Includes the board and the turn.
     * @param list whether we want to update the list of pseudo-legal moves or not. This is to avoid a concurrent modification error
     * @return a bitboard representing all the places !white could move to
     */
    public static long allPseudoLegalMovesBitBoard(Game game, boolean list) {
        long pieces = game.isWhiteToMove()? game.getBoard().whitePieces : game.getBoard().blackPieces;
        int square = 0;
        long moves = 0L;
        while (pieces != 0L) {

            square = Long.numberOfTrailingZeros(pieces);
            if (((1L<<square) & game.getBoard().getBitboard(PieceType.PAWN, game.isWhiteToMove())) != 0){
                moves |= pawnMoves(square, game, list);
            } else if (((1L<<square) & game.getBoard().getBitboard(PieceType.KNIGHT, game.isWhiteToMove())) != 0){
                moves |= masterMoves(PieceType.KNIGHT, square, game, list);
            } else if (((1L<<square) & game.getBoard().getBitboard(PieceType.BISHOP, game.isWhiteToMove())) != 0){
                moves |= masterMoves(PieceType.BISHOP, square, game, list);
            } else if (((1L<<square) & game.getBoard().getBitboard(PieceType.ROOK, game.isWhiteToMove())) != 0){
                moves |= masterMoves(PieceType.ROOK, square, game, list);
            } else if (((1L<<square) & game.getBoard().getBitboard(PieceType.QUEEN, game.isWhiteToMove())) != 0){
                moves |= masterMoves(PieceType.QUEEN, square, game, list);
            } else if (((1L<<square) & game.getBoard().getBitboard(PieceType.KING, game.isWhiteToMove())) != 0){
                moves |= masterMoves(PieceType.KING, square, game, list);
            }
            pieces &= (pieces -1);
        }
        return moves;
    }

    /**
     * Makes the given move on the board
     * @param move the move that must be made on the board
     * @param game the game state. Includes the board and the turn.
     */
    public static void makeMove(Move move, Game game){
        long specificPieces = game.getBoard().getBitboard(move.pieceType, game.isWhiteToMove());
        if (move.captureType != null){
            game.getBoard().setBitboard(move.captureType, game.getBoard().getBitboard(move.captureType, !game.isWhiteToMove()) & ~(1L << move.captureOn), !game.isWhiteToMove());
        }
        if (move.breaksCastle){
            if (game.isWhiteToMove()){
                if (move.pieceType == PieceType.KING){
                    game.getBoard().wO_O = false;
                    game.getBoard().wO_O_O = false;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        game.getBoard().wO_O_O = false;
                    } else {
                        game.getBoard().wO_O = false;
                    }
                }
            } else {
                if (move.pieceType == PieceType.KING){
                    game.getBoard().bO_O = false;
                    game.getBoard().bO_O_O = false;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        game.getBoard().bO_O_O = false;
                    } else {
                        game.getBoard().bO_O = false;
                    }
                }
            }
        }
        if (move.moveType == MoveType.SHORTCASTLE){
            if (game.isWhiteToMove()){
                game.getBoard().setBitboard(PieceType.ROOK, (game.getBoard().whiteRooks & ~(1L << 7)) | (1L << 5), true);
                game.getBoard().setBitboard(PieceType.KING, 1L << move.to, true);
            } else {
                game.getBoard().setBitboard(PieceType.ROOK, (game.getBoard().blackRooks & ~(1L << 63)) | (1L << 61), false);
                game.getBoard().setBitboard(PieceType.KING, 1L << move.to, false);
            }
        } else if (move.moveType == MoveType.LONGCASTLE){
            if (game.isWhiteToMove()){
                game.getBoard().setBitboard(PieceType.ROOK, (game.getBoard().whiteRooks & ~(1L << 0)) | (1L << 3), true);
                game.getBoard().setBitboard(PieceType.KING, 1L << move.to, true);
            } else {
                game.getBoard().setBitboard(PieceType.ROOK, (game.getBoard().blackRooks & ~(1L << 56)) | (1L << 59), false);
                game.getBoard().setBitboard(PieceType.KING, 1L << move.to, false);
            }
        } else {
            specificPieces &= ~(1L << move.from);
            specificPieces |= 1L << move.to;
            game.getBoard().setBitboard(move.pieceType, specificPieces, game.isWhiteToMove());
        }
    }
    /**
     * This methods assumes the move has already been made, and undoes it.
     * @param move the move that must be undone
     * @param game the game state. Includes the board and the turn.
     */
    public static void unMove(Move move, Game game){
        long specificPieces = game.getBoard().getBitboard(move.pieceType, game.isWhiteToMove());
        if (move.captureType != null){
            game.getBoard().setBitboard(move.captureType, game.getBoard().getBitboard(move.captureType, !game.isWhiteToMove()) | (1L<< move.captureOn), !game.isWhiteToMove());
        }
        if (move.breaksCastle){
            if (game.isWhiteToMove()){
                if (move.pieceType == PieceType.KING){
                    game.getBoard().wO_O = true;
                    game.getBoard().wO_O_O = true;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        game.getBoard().wO_O_O = true;
                    } else {
                        game.getBoard().wO_O = true;
                    }
                }
            } else {
                if (move.pieceType == PieceType.KING){
                    game.getBoard().bO_O = true;
                    game.getBoard().bO_O_O = true;
                }
                if (move.pieceType == PieceType.ROOK){
                    if (move.from%8 == 0){
                        game.getBoard().bO_O_O = true;
                    } else {
                        game.getBoard().bO_O = true;
                    }
                }
            }
        }
        specificPieces &= ~(1L << move.to);
        specificPieces |= 1L << move.from;

        game.getBoard().setBitboard(move.pieceType, specificPieces, game.isWhiteToMove());
    }


    /**
     * Determines which of the pseudo-legal moves are actually legal
     * @param game the game state. Includes the board and the turn.
     * @return a list of all the legal moves for the current player
     */
    public static ArrayList<Move> allLegalMoves(Game game){
        movesL.clear();
        PmovesL.clear();
        long otherMoves;
        allPseudoLegalMovesBitBoard(game, true);
        addCastling(game);
        
        for (Move move : PmovesL){
            if (move.moveType != MoveType.SHORTCASTLE || move.moveType != MoveType.LONGCASTLE){
                System.out.println("Trying move: " + move);
                makeMove(move, game);
                otherMoves = allPseudoLegalMovesBitBoard(game, false);

                if (!isInCheck(otherMoves, game)) movesL.add(move);
                
                unMove(move, game);
            }
        }
        for (Move move : movesL){
            System.out.println(move);
        }
        return movesL;
    }

    /**
     * Prints the given bitboard row by row
     * @param bitboard the bitboard to be printed
     */
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
