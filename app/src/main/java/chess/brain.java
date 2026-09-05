/**
 * brain
 * Finds all the legal moves in a position and identifies when an end-of-game condition has been met.
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */

package chess;

import java.util.ArrayList;

public class Brain {

    private static ArrayList<Move> pseudoLegalMoves = new ArrayList<>();
    private static ArrayList<Move> legalMoves = new ArrayList<>();
    //private static Game game = new Game(board);

    /**
     * Identifies all the pseudo-legal moves for all the pieces of the current player with the exception of pawns.
     * @param game the game state
     * @return an arraylist containing all the pseudo-legal moves in the position for the current player
     */
    public static ArrayList<Move> masterMoves(Game game){
        ArrayList<Move> toReturn = new ArrayList<>();
        int[] offsets;
        long specificPieces;
        int square, targetSquare, itter;
        boolean white = game.getTurn();
        long otherPieces = white? game.getBoard().blackPieces : game.getBoard().whitePieces; // the pieces of the opposite colour
        long pieces = white? game.getBoard().whitePieces : game.getBoard().blackPieces; // the pieces of the same colour

        for (PieceType pieceType : PieceType.values()){ // we go over every piece type
            offsets = pieceType.getOffsets(pieceType); // we get the offsets for the piece type
            itter = pieceType.getItter(pieceType); // we get the maximum number of times that piece can move in each offset
            specificPieces = game.getBoard().getBitBoard(pieceType, white); // we get the bitboard representing the piece type for the correct colour
            if (specificPieces == 0) continue; // if we've counted the moves for all instances of the piece type we can move on to the next piece type
            while (specificPieces != 0){ //  this makes sure we find the moves for every instance of that piece on the board
                square = Long.numberOfTrailingZeros(specificPieces); // the square the piece is on
                for (int offset : offsets){
                    for (int i = 0; i < itter; i++){
                        targetSquare = square + offset*(i+1); // the square the piece is going to try to move to
                        if (targetSquare < 0 || targetSquare > 63) break; // we make sure the square is on the board
                        if (((1L << targetSquare) & pieces) != 0) break; // we make sure the target square is not occupied by a piece of the same colour
                        if (Math.abs((((square + offset*(i))) % 8) - (targetSquare % 8)) > 2) break; // we make sure the move doesn't involve wrapping around the board
                        if (((1L << targetSquare) & otherPieces) != 0){
                            toReturn.add(new Move(MoveType.CAPTURE, square, targetSquare, targetSquare, pieceType, game.getBoard().getPieceOnSquare(targetSquare), white, false));
                            break; // we capture the piece and move on to the next offset. We can't move on through a piece.
                        } else {
                            toReturn.add(new Move(MoveType.MOVE, square, targetSquare, pieceType, white, false));
                        }
                    }
                }
                specificPieces &= ~(1L << (square)); // once we've counted all the moves for this instance of the piece, we remove it from the bitboard so we don't recount it.
            }
        }
        return toReturn;
    }

    public static ArrayList<Move> pawnMoves(Game game){
        ArrayList<Move> toReturn = new ArrayList<>();
        Board board = game.getBoard();
        Boolean white = game.getTurn();
        long pawns = board.getBitBoard(PieceType.PAWN, white);
        int startingRank = white? 1 : 6;
        int promotionRank = white? 7 : 0;
        int forward = white? 8 : -8;
        int captureRight = white? 9 : -7;
        int captureLeft = white? 7 : -9;
        //int braveRank = white? 4:3;
        //int cowardRank = white? 3:4;
        int square;
        long otherPieces = white? game.getBoard().blackPieces : game.getBoard().whitePieces; // the pieces of the opposite colour
        while (pawns != 0){
            square = Long.numberOfTrailingZeros(pawns);
            if (board.getPieceOnSquare(square+forward) == null){ // if there is no piece directly in front of the pawn
                if ((square/8 == startingRank)&&(board.getPieceOnSquare(square+2*forward) == null)){ // if we are on the starting rank and the two square in front of the pawn are empty
                    toReturn.add(new Move(MoveType.MOVE, square, square+2*forward, PieceType.PAWN, white, false)); // we add the double-forward move
                }
                if ((square+forward)/8 == promotionRank) {
                    toReturn.addAll(Move.promotion(square, square+forward, white));
                } else {
                    toReturn.add(new Move(MoveType.MOVE, square, square+forward, PieceType.PAWN, white, false)); // we add the single-forward move
                }
            }
            if (((1L<<(square+captureRight)) & otherPieces) != 0){ // if there is an enemy piece on the right capture square
                if ((square+captureRight)/8 == promotionRank){
                    toReturn.addAll(Move.capturePromotion(square, square+captureRight, board.getPieceOnSquare(square+captureRight), white));
                } else {
                    toReturn.add(new Move(MoveType.CAPTURE, square, square+captureRight, square+captureRight, PieceType.PAWN, board.getPieceOnSquare(square+captureRight), white, false));
                }
            }
            if (((1L<<(square+captureLeft)) & otherPieces) != 0){ // if there is an enemy piece on the left capture square
                if ((square+captureLeft)/8 == promotionRank){
                    toReturn.addAll(Move.capturePromotion(square, square+captureLeft, board.getPieceOnSquare(square+captureLeft), white));
                } else {
                    toReturn.add(new Move(MoveType.CAPTURE, square, square+captureLeft, square+captureLeft, PieceType.PAWN, board.getPieceOnSquare(square+captureLeft), white, false));
                }
            }
            if (game.getLastMove() != null){ // is this isn't the first move
            //System.out.println("Last move exists ... check");
                Move lastMove = game.getLastMove();
                if (lastMove.pieceType == PieceType.PAWN){ // if the last move was a pawn move
                    //System.out.println("Last move is pawn move ... check");
                    if (Math.abs(lastMove.from - lastMove.to) == 16){ // if the last move was a double pawn move and the last move was from the starting rank
                        //System.out.println("Last move from correct rank ... check");
                        if (lastMove.from/8 == (lastMove.white? 1:6)){
                            //System.out.println("Last move to correct rank ... check");
                            if (Math.abs((lastMove.to%8) - (square%8))==1){ // if the last move put the pawn on a square right beside the current pawn
                                toReturn.add(new Move(MoveType.CAPTURE, square, lastMove.to + forward, lastMove.to, PieceType.PAWN, PieceType.PAWN, white, false));
                                //System.out.println("Move added ... check");
                            }
                        }
                    }
                }
            }
            pawns &= ~(1L<<square);  
        }
        return toReturn;
    }

    public static long getOtherMoves(Game game){
        long toReturn = 0L;
        game.changeTurn();
        for (Move move : new ArrayList<>(masterMoves(game))){
            toReturn |= 1L << move.to;
        }
        //printBitboard(toReturn, "Othermoves: ");
        game.changeTurn();
        return toReturn;
    }

    /**
     * Determines if the current player is in check in the current position
     * @param game the game state
     * @return true if the current player is in check in the passed position, false otherwise
     */

    public static ArrayList<Move> getLegalMoves(Game game){
        ArrayList<Move> toReturn = new ArrayList<>();
        pseudoLegalMoves = masterMoves(game);
        pseudoLegalMoves.addAll(pawnMoves(game));
        //System.out.println(pseudoLegalMoves.size());
        for (Move move : pseudoLegalMoves){
            makeMove(move, game);
            //board.printBoard(board);

            if ((getOtherMoves(game) & (game.getBoard().getBitBoard(PieceType.KING, move.white))) == 0){
                toReturn.add(move);
            } else {
                // useful breakpoint line and good for testing
            }
            unMove(move, game);
        }
        for (Move move : toReturn){
            //System.out.println(move);
        }
        return toReturn;
    }

    public static void makeMove(Move move, Game game){
        Board board = game.getBoard(); // the current board
        long pieces = board.getBitBoard(move.pieceType, move.white); // the bitboard representing the pieces of which one shall be moved
        long capturedPieces;
        long promotionPieces = (move.promotionType == null)? 0L : board.getBitBoard(move.promotionType, move.white);
        if (move.moveType == MoveType.MOVE){
            pieces &= ~(1L << move.from);
            pieces |= 1L << move.to;
            board.setBitBoard(move.pieceType, move.white, pieces);
        } else if (move.moveType == MoveType.CAPTURE){
            //System.out.println(move.pieceType);
            capturedPieces = board.getBitBoard(move.captureType, !move.white);
            pieces &= ~(1L << move.from);
            pieces |= 1L << move.to;
            capturedPieces &= ~(1L << move.captureOn);
            board.setBitBoard(move.pieceType, move.white, pieces);
            board.setBitBoard(move.captureType, !move.white, capturedPieces);
        } else if (move.moveType == MoveType.PROMOTION){
            pieces &= ~(1L << move.from);
            promotionPieces |= 1L << move.to;
            board.setBitBoard(PieceType.PAWN, move.white, pieces);
            board.setBitBoard(move.promotionType, move.white, promotionPieces);
        } else if (move.moveType == MoveType.CAPTURE_PROMOTION){
            //System.out.println("test");
            capturedPieces = board.getBitBoard(move.captureType, !move.white);
            pieces &= ~(1L << move.from);
            promotionPieces |= 1L << move.to;
            capturedPieces &= ~(1L << move.captureOn);
            board.setBitBoard(PieceType.PAWN, move.white, pieces);
            board.setBitBoard(move.promotionType, move.white, promotionPieces);
            board.setBitBoard(move.captureType, !move.white, capturedPieces);
        }
    }

    public static void unMove(Move move, Game game){
        Board board = game.getBoard();
        long pieces = board.getBitBoard(move.pieceType, move.white);
        long capturedPieces;
        long promotionPieces = (move.promotionType == null)? 0L : board.getBitBoard(move.promotionType, move.white);
        if (move.moveType == MoveType.MOVE){
            pieces &= ~(1L << move.to);
            pieces |= 1L << move.from;
            game.getBoard().setBitBoard(move.pieceType, move.white, pieces);
        } else if (move.moveType == MoveType.CAPTURE){
            capturedPieces = board.getBitBoard(move.captureType, !move.white);
            pieces &= ~(1L << move.to);
            pieces |= 1L << move.from;
            capturedPieces |= 1L << move.captureOn;
            board.setBitBoard(move.pieceType, move.white, pieces);
            board.setBitBoard(move.captureType, !move.white, capturedPieces);
        } else if (move.moveType == MoveType.PROMOTION){
            pieces |= 1L << move.from;
            promotionPieces &= ~(1L << move.to);
            board.setBitBoard(PieceType.PAWN, move.white, pieces);
            board.setBitBoard(move.promotionType, move.white, promotionPieces);
        } else if (move.moveType == MoveType.CAPTURE_PROMOTION){
            capturedPieces = board.getBitBoard(move.captureType, !move.white);
            pieces |= 1L << move.from;
            promotionPieces &= ~(1L << move.to);
            capturedPieces |= 1L << move.captureOn;
            board.setBitBoard(PieceType.PAWN, move.white, pieces);
            board.setBitBoard(move.promotionType, move.white, promotionPieces);
            board.setBitBoard(move.captureType, !move.white, capturedPieces);
        }
    }

    public static void printBitboard(Long board, String header){
         // Process from the 8th byte (most significant) down to the 1st (least significant)
        System.out.println(header);
        for (int i = 7; i >= 0; i--) {
            // Extract the specific byte by shifting right by 8*i bits and masking with 0xFF
            int byteValue = (int) ((board >> (i * 8)) & 0xFF);
            
            // Convert the byte to an 8-bit binary string, padding with leading zeros
            String binaryByte = String.format("%8s", Integer.toBinaryString(byteValue)).replace(' ', '0');
            System.out.println(binaryByte);
        }
        System.out.println("\n");
    }
}