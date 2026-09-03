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
    private static Board board = new Board(0L, 0x0000000000000042L, 0x0000000000000024L, 0x0000000000000081L, 0x0000000000000008L, 0x0000000000000010L, 0L, 0x4200000000000000L, 0x2400000000000000L, 0x8100000000000000L, 0x0800000000000000L, 0x1000000000000000L);
    private static Game game = new Game(board);

    public static ArrayList<Move> masterMoves(Game game){
        ArrayList<Move> toReturn = new ArrayList<>();
        int[] offsets;
        int itter;
        long specificPieces;
        int square;
        int targetSquare;
        boolean white = game.getTurn();
        //System.out.println(white);
        long otherPieces = white? game.getBoard().blackPieces : game.getBoard().whitePieces; // the pieces of the opposite colour
        long pieces = white? game.getBoard().whitePieces : game.getBoard().blackPieces; // the pieces of the same colour

        for (PieceType pieceType : PieceType.values()){ // we go over every piece type
            offsets = pieceType.getOffsets(pieceType); // we get the offsets for the piece type
            itter = pieceType.getItter(pieceType); // we get the maximum number of times that piece can move in each offset
            specificPieces = game.getBoard().getBitBoard(pieceType, white); // we get the bitboard representing the piece type for the correct colour
            //System.out.println(white + " " + pieceType + " " + Long.toHexString(specificPieces));
            if (specificPieces == 0){
                continue;
            }
            while (specificPieces != 0){ //  this makes sure we find the moves for every instance of that piece on the board
                square = Long.numberOfTrailingZeros(specificPieces); // the square the piece is on
                //System.out.println(pieceType);
                for (int offset : offsets){
                    for (int i = 0; i < itter; i++){
                        //System.out.println(offset + " " + i);
                        targetSquare = square + offset*(i+1); // the square the piece is going to try to move to
                        if (targetSquare < 0 || targetSquare > 63) break; // we make sure the square is on the board
                        if (((1L << targetSquare) & pieces) != 0) break; // we make sure the target square is not occupied by a piece of the same colour
                        if (Math.abs((((square + offset*(i))) % 8) - (targetSquare % 8)) > 2) break; // we make sure the move doesn't involve wrapping around the board
                        if (((1L << targetSquare) & otherPieces) != 0){
                            toReturn.add(new Move(MoveType.CAPTURE, square, targetSquare, targetSquare, pieceType, game.getBoard().getPieceOnSquare(targetSquare), white, false));
                            break;
                        } else {
                            toReturn.add(new Move(MoveType.MOVE, square, targetSquare, pieceType, white, false));
                        }
                    }
                }
                specificPieces &= ~(1L << (square));
            }
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

    public static ArrayList<Move> getLegalMoves(){
        ArrayList<Move> toReturn = new ArrayList<>();
        pseudoLegalMoves = masterMoves(game);
        //System.out.println(pseudoLegalMoves.size());
        for (Move move : pseudoLegalMoves){
            makeMove(move, game);
            if ((getOtherMoves(game) & (game.getBoard().getBitBoard(PieceType.KING, move.white))) == 0){
                toReturn.add(move);
            } else {
                // useful breakpoint line and good for testing
            }
            unMove(move, game);
        }
        for (Move move : toReturn){
            System.out.println(move);
        }
        System.out.println(toReturn.size());
        return toReturn;
    
    }

    public static void makeMove(Move move, Game game){
        Board board = game.getBoard();
        long pieces = board.getBitBoard(move.pieceType, game.getTurn());
        //System.out.println(move+  " (" + Math.abs(move.from - move.to) + ")");
        if (move.moveType == MoveType.MOVE){
            pieces &= ~(1L << move.from);
            pieces |= 1L << move.to;
            board.setBitBoard(move.pieceType, game.getTurn(), pieces);
        } else if (move.moveType == MoveType.CAPTURE){
            long capturedPieces = board.getBitBoard(board.getPieceOnSquare(move.captureOn), !move.white);
            pieces &= ~(1L << move.from);
            pieces |= 1L << move.to;
            capturedPieces &= ~(1L << move.captureOn);
            board.setBitBoard(move.pieceType, move.white, pieces);
            board.setBitBoard(move.captureType, !move.white, capturedPieces);
        }
    }

    public static void unMove(Move move, Game game){
        Board board = game.getBoard();
        long pieces = game.getBoard().getBitBoard(move.pieceType, game.getTurn());
        if (move.moveType == MoveType.MOVE){
            pieces &= ~(1L << move.to);
            pieces |= 1L << move.from;
            game.getBoard().setBitBoard(move.pieceType, game.getTurn(), pieces);
        } else if (move.moveType == MoveType.CAPTURE){
            long capturedPieces = board.getBitBoard(board.getPieceOnSquare(move.captureOn), !move.white);
            pieces &= ~(1L << move.to);
            pieces |= 1L << move.from;
            capturedPieces |= 1L << move.captureOn;
            board.setBitBoard(move.pieceType, move.white, pieces);
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