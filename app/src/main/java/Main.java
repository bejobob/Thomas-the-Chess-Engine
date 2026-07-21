

//TODO: Verify that captures really work. I think they do, but make sure.
//TODO: Castling
//TODO: En-passant
//TODO: Stalemate
//TODO: Threefold repetition
//TODO: Checkmate

import chess.legalMoves;
import chess.Board;
import java.util.ArrayList;
import chess.Move;

public class Main {

    int BASE_PAWN_VALUE = 1;
    int BASE_KNIGHT_VALUE = 3;
    int BASE_BISHOP_VALUE = 3;
    int BASE_ROOK_VALUE = 5;
    int BASE_QUEEN_VALUE = 9;

    long whitePawns = 0x0L; // 
    long whiteRooks = 0x0000000010000000L; // e4
    long whiteKnights = 0x0L; // 
    long whiteBishops = 0x0L; // 
    long whiteQueens = 0x0L; // 
    long whiteKing = 0x0000000000001000L; // e2

    long blackPawns = 0L;
    long blackRooks = 0x1000000000000000L;
    long blackKnights = 0L;
    long blackBishops = 0L;
    long blackQueens = 0L;
    long blackKing = 0x8000000000000000L; // h8
    //long whitePawns = 0x000000000000FF00L; // starting position
    //long whiteRooks = 0x0000000000000081L; // starting position
    //long whiteKnights = 0x0000000000000042L; // starting position
    //long whiteBishops = 0x0000000000000024L; // starting position
    //long whiteQueens = 0x0000000000000008L; // starting position
    //long whiteKing = 0x0000000000000010L; // starting position
    long whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;

    //long blackPawns = 0x00FF000000000000L; // starting position
    //long blackRooks = 0x8100000000000000L; // starting position
    //long blackKnights = 0x4200000000000000L; // starting position
    //long blackBishops = 0x2400000000000000L; // starting position
    //long blackQueens = 0x0800000000000000L; // starting position
    //long blackKing = 0x1000000000000000L; // starting position
    long blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;
    long whitePawnsCopy = whitePawns;
    long blackPawnsCopy = blackPawns;
    long whiteRooksCopy = whiteRooks;
    long blackRooksCopy = blackRooks;
    long whiteKnightsCopy = whiteKnights;
    long blackKnightsCopy = blackKnights;
    long whiteBishopsCopy = whiteBishops;
    long blackBishopsCopy = blackBishops;
    long whiteQueensCopy = whiteQueens;
    long blackQueensCopy = blackQueens;
    long whiteKingCopy = whiteKing;
    long blackKingCopy = blackKing;
    long[] whitePiecesCopy = {whitePawnsCopy, whiteRooksCopy, whiteKnightsCopy, whiteBishopsCopy, whiteQueensCopy, whiteKingCopy};
    long [] blackPiecesCopy = {blackPawnsCopy, blackRooksCopy, blackKnightsCopy, blackBishopsCopy, blackQueensCopy, blackKingCopy};

    Board board = new Board(whitePawns, whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
            blackPawns, blackRooks, blackKnights, blackBishops, blackQueens, blackKing);

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        whiteMoves();
        
    }
    
    public ArrayList<Move> whiteMoves(){
        ArrayList<Move> legalMovesL = new ArrayList<>();

            //System.out.printf("%016X%n", group);


                //System.out.println(square);
                legalMovesL = legalMoves.allLegalMoves(whitePieces, blackPieces, true, board);
                //System.out.println(legalMovesL.size());
                for (Move move : legalMovesL){
                    //System.out.println(move.toAlgebraic(move));
                }            
                
            
        
        return legalMovesL;
    }
}
