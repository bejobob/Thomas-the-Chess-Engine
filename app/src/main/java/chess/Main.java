/**
 * Main
 * The main engine for Thomas the Chess Engine
 * @author Benjamin Kealey
 * @version 2026/08/20
 */

package chess;

// TODO: Clean up brain.java.
// TODO: Get to work on evaluating positions and moves
// TODO: Move end-of-game stuff from brain.java to Main.java?
// TODO: Finish writing javadoc for all my methods. Good to stay organized!

import java.util.ArrayList;

public class Main {

    int BASE_PAWN_VALUE = 1;
    int BASE_KNIGHT_VALUE = 3;
    int BASE_BISHOP_VALUE = 3;
    int BASE_ROOK_VALUE = 5;
    int BASE_QUEEN_VALUE = 9;

    long whitePawns = 0x0001000000000000L; // a7
    long whiteRooks = 0x0000000000000081L; // a1 and h1
    long whiteKnights = 0x0L; // 
    long whiteBishops = 0x0L; // 
    long whiteQueens = 0x0L; // 
    long whiteKing = 0x0000000000000010L; // e1

    long blackPawns = 0L;
    long blackRooks = 0x8000000000000000L; // e8
    long blackKnights = 0x0L;
    long blackBishops = 0x0200000000000000L; // b8
    long blackQueens = 0L;
    long blackKing = 0x1000000000000000L; // e8
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

    float[] pawnTable = {
    0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,

    0.05f,  0.10f,  0.10f, -0.05f, -0.05f,  0.10f,  0.10f,  0.05f,

    0.05f,  0.05f,  0.10f,  0.20f,  0.20f,  0.10f,  0.05f,  0.05f,

    0.00f,  0.00f,  0.10f,  0.25f,  0.25f,  0.10f,  0.00f,  0.00f,

    0.00f,  0.00f,  0.05f,  0.20f,  0.20f,  0.05f,  0.00f,  0.00f,

    0.05f, -0.05f, -0.10f,  0.00f,  0.00f, -0.10f, -0.05f,  0.05f,

    0.05f,  0.05f,  0.05f, -0.10f, -0.10f,  0.05f,  0.05f,  0.05f,

    0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f,  0.00f
};

    float[] knightTable = {
        -0.5f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.5f,

        -0.4f, -0.2f,  0.0f,  0.0f,  0.0f,  0.0f, -0.2f, -0.4f,

        -0.4f,  0.0f,  0.10f, 0.2f,  0.2f,  0.10f, 0.0f, -0.4f,

        -0.4f,  0.0f,  0.2f,  0.25f, 0.25f, 0.2f,  0.0f, -0.4f,

        -0.4f,  0.0f,  0.2f,  0.25f, 0.25f, 0.2f,  0.0f, -0.4f,

        -0.4f,  0.0f,  0.10f, 0.2f,  0.2f,  0.10f, 0.0f, -0.4f,

        -0.4f, -0.2f,  0.0f,  0.0f,  0.0f,  0.0f, -0.2f, -0.4f,

        -0.5f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.4f, -0.5f,
    };

    float[] bishopTable = {
        -0.2f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.2f,

        -0.1f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f, -0.1f,

        -0.1f,  0.0f,  0.10f, 0.15f, 0.15f, 0.10f, 0.0f, -0.1f,

        -0.1f,  0.05f, 0.10f, 0.20f, 0.20f, 0.10f, 0.05f, -0.1f,

        -0.1f,  0.05f, 0.10f, 0.20f, 0.20f, 0.10f, 0.05f, -0.1f,

        -0.1f,  0.0f,  0.10f, 0.15f, 0.15f, 0.10f, 0.0f, -0.1f,

        -0.1f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f,  0.0f, -0.1f,

        -0.2f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.1f, -0.2f
    };

    float[] rookTable = {
        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.05f,  0.05f,  0.05f,  0.10f,  0.10f,  0.05f,  0.05f,  0.05f,

        0.05f,  0.05f,  0.05f,  0.10f,  0.10f,  0.05f,  0.05f,  0.05f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f,

        0.10f,  0.10f,  0.10f,  0.15f,  0.15f,  0.10f,  0.10f,  0.10f,

        0.00f,  0.00f,  0.00f,  0.05f,  0.05f,  0.00f,  0.00f,  0.00f
    };

    float[] queenTable = {
        -0.2f, -0.1f, -0.1f,  0.0f,  0.0f, -0.1f, -0.1f, -0.2f,

        -0.1f,  0.0f,  0.05f, 0.05f, 0.05f, 0.05f,  0.0f, -0.1f,

        -0.1f,  0.05f, 0.10f, 0.10f, 0.10f, 0.10f, 0.05f, -0.1f,

        0.0f,  0.05f, 0.10f, 0.15f, 0.15f, 0.10f, 0.05f,  0.0f,

        0.0f,  0.05f, 0.10f, 0.15f, 0.15f, 0.10f, 0.05f,  0.0f,

        -0.1f,  0.05f, 0.10f, 0.10f, 0.10f, 0.10f, 0.05f, -0.1f,

        -0.1f,  0.0f,  0.05f, 0.05f, 0.05f, 0.05f,  0.0f, -0.1f,

        -0.2f, -0.1f, -0.1f,  0.0f,  0.0f, -0.1f, -0.1f, -0.2f
    };

    float[] kingTable = {
        0.20f,  0.30f,  0.10f,  0.00f,  0.00f,  0.10f,  0.30f,  0.20f,

        0.20f,  0.20f,  0.00f,  0.00f,  0.00f,  0.00f,  0.20f,  0.20f,

        0.10f,  0.00f, -0.10f, -0.20f, -0.20f, -0.10f,  0.00f,  0.10f,

        0.00f,  0.00f, -0.20f, -0.30f, -0.30f, -0.20f,  0.00f,  0.00f,

        0.00f,  0.00f, -0.20f, -0.30f, -0.30f, -0.20f,  0.00f,  0.00f,

        0.10f,  0.00f, -0.10f, -0.20f, -0.20f, -0.10f,  0.00f,  0.10f,

        0.20f,  0.20f,  0.00f,  0.00f,  0.00f,  0.00f,  0.20f,  0.20f,

        0.20f,  0.30f,  0.10f,  0.00f,  0.00f,  0.10f,  0.30f,  0.20f
    };

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
    Game game = new Game(board);

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
                legalMovesL = brain.allLegalMoves(game);
                //System.out.println(legalMovesL.size());
                for (Move move : legalMovesL){
                    //System.out.println(move.toAlgebraic(move));
                }            
                
            
        
        return legalMovesL;
    }

    public float evaluate(Game position){
        /*
        score = 
        materialScore +
        positionalScore +
        mobilityScore +
        kingSafetyScore
        */
        return 0.0f;
    }

    public float materialScore(Board board){
        float score = 0.0f;
        score += Long.bitCount(board.whitePawns) * BASE_PAWN_VALUE;
        score += Long.bitCount(board.whiteKnights) * BASE_KNIGHT_VALUE;
        score += Long.bitCount(board.whiteBishops) * BASE_BISHOP_VALUE;
        score += Long.bitCount(board.whiteRooks) * BASE_ROOK_VALUE;
        score += Long.bitCount(board.whiteQueens) * BASE_QUEEN_VALUE;
        score -= Long.bitCount(board.blackPawns) * BASE_PAWN_VALUE;
        score -= Long.bitCount(board.blackKnights) * BASE_KNIGHT_VALUE;
        score -= Long.bitCount(board.blackBishops) * BASE_BISHOP_VALUE;
        score -= Long.bitCount(board.blackRooks) * BASE_ROOK_VALUE;
        score -= Long.bitCount(board.blackQueens) * BASE_QUEEN_VALUE;
        return score;
    }

    public float placementScore(Board board){
        float score = 0.0f;

        long pawns = board.whitePawns;
        long knights = board.whiteKnights;
        long bishops = board.whiteBishops;
        long rooks = board.whiteRooks;
        long queens = board.whiteQueens;
        long kings = board.whiteKing;
        while (pawns != 0){
            int square = Long.numberOfTrailingZeros(pawns);
            score += pawnTable[square];
            pawns &= pawns -1;
        }
        while (knights != 0){
            int square = Long.numberOfTrailingZeros(knights);
            score += knightTable[square];
            knights &= knights -1;
        }
        while (bishops != 0){
            int square = Long.numberOfTrailingZeros(bishops);
            score += bishopTable[square];
            bishops &= bishops -1;
        }
        while (rooks != 0){
            int square = Long.numberOfTrailingZeros(rooks);
            score += rookTable[square];
            rooks &= rooks -1;
        }
        while (queens != 0){
            int square = Long.numberOfTrailingZeros(queens);
            score += queenTable[square];
            queens &= queens -1;
        }
        while (kings != 0){
            int square = Long.numberOfTrailingZeros(kings);
            score += kingTable[square];
            kings &= kings -1;
        }

        pawns = board.blackPawns;
        knights = board.blackKnights;
        bishops = board.blackBishops;
        rooks = board.blackRooks;
        queens = board.blackQueens;
        kings = board.blackKing;
        while (pawns != 0){
            int square = Long.numberOfTrailingZeros(pawns);
            score -= pawnTable[square ^ 56];
            pawns &= pawns -1;
        }
        while (knights != 0) {
            int square = Long.numberOfTrailingZeros(knights);
            score -= knightTable[square ^ 56];
            knights &= knights -1;
        }
        while (bishops != 0){
            int square = Long.numberOfTrailingZeros(bishops);
            score -= bishopTable[square ^ 56];
            bishops &= bishops -1;
        }
        while (rooks != 0){
            int square = Long.numberOfTrailingZeros(rooks);
            score -= rookTable[square ^ 56];
            rooks &= rooks -1;
        }
        while (queens != 0){
            int square = Long.numberOfTrailingZeros(queens);
            score -= queenTable[square ^ 56];
            queens &= queens -1;
        }
        while (kings != 0){
            int square = Long.numberOfTrailingZeros(kings);
            score -= kingTable[square ^ 56];
            kings &= kings -1;
        }
        return score;

    }
}
