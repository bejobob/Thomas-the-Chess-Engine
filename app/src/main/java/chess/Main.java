/**
 * Main
 * The main engine for Thomas the Chess Engine
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */
package chess;
import java.util.Scanner;
import java.util.ArrayList;


// TODO: Clean up brain.java.
// TODO: Get to work on evaluating positions and moves
// TODO: Move end-of-game stuff from brain.java to Main.java?
// TODO: Finish writing javadoc for all my methods. Good to stay organized!


public class Main {

    int BASE_PAWN_VALUE = 1;
    int BASE_KNIGHT_VALUE = 3;
    int BASE_BISHOP_VALUE = 3;
    int BASE_ROOK_VALUE = 5;
    int BASE_QUEEN_VALUE = 9;
    long whitePawns = 0x000000000000FF00L; // starting position
    long whiteRooks = 0x0000000000000081L; // starting position
    long whiteKnights = 0x0000000000000042L; // starting position
    long whiteBishops = 0x0000000000000024L; // starting position
    long whiteQueens = 0x0000000000000008L; // starting position
    long whiteKing = 0x0000000000000010L; // starting position
    long whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;

    long blackPawns = 0L;
    //long blackPawns = 0x00FF000000000000L; // starting position
    long blackRooks = 0x8100000000000000L; // starting position
    long blackKnights = 0x4200000000000000L; // starting position
    long blackBishops = 0x2400000000000000L; // starting position
    long blackQueens = 0x0800000000000000L; // starting position
    long blackKing = 0x1000000000000000L; // starting position
    long blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;


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

    Board board = new Board(whitePawns, whiteRooks, whiteKnights, whiteBishops, whiteQueens, whiteKing,
            blackPawns, blackRooks, blackKnights, blackBishops, blackQueens, blackKing);
    Game game = new Game(board);
    Scanner input = new Scanner(System.in);
    //ArrayList<Move> movesL = new ArrayList<>();
    //Brain brain = new Brain();

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        Brain.getLegalMoves();
    }
}
