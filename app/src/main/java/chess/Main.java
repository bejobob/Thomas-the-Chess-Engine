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

    long whitePawns = 0x000000000000FF00L; // starting position
    long whiteRooks = 0x0000000000000081L; // starting position
    long whiteKnights = 0x0000000000000042L; // starting position
    long whiteBishops = 0x0000000000000024L; // starting position
    long whiteQueens = 0x0000000000000008L; // starting position
    long whiteKing = 0x0000000000000010L; // starting position
    long whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;

    long blackPawns = 0x00FF000000000000L; // starting position
    long blackRooks = 0x8100000000000000L; // starting position
    long blackKnights = 0x4200000000000000L; // starting position
    long blackBishops = 0x2400000000000000L; // starting position
    long blackQueens = 0x0800000000000000L; // starting position
    long blackKing = 0x1000000000000000L; // starting position
    long blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;

    Board board = new Board(whitePawns, whiteKnights, whiteBishops, whiteRooks, whiteQueens, whiteKing,
            blackPawns, blackKnights, blackBishops, blackRooks, blackQueens, blackKing);
    Game game = new Game(board);
    Scanner input = new Scanner(System.in);
    //ArrayList<Move> movesL = new ArrayList<>();
    //Brain brain = new Brain();

    public static void main(String[] args) {
        Main app = new Main();
        app.run();
    }

    public void run() {
        while (true){
            System.out.println(alphaBeta(game, 4, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY));
            board.printBoard(board);
            System.out.println(game.getTurn());
            String userInput = input.nextLine();
            int from = fromAlgebraic(userInput.substring(0, 2));
            int to = fromAlgebraic(userInput.substring(2, 4));
            Move toPlay = pickMove(from, to);
            Brain.makeMove(toPlay, game);
            game.changeTurn();
        }
    }
    public int fromAlgebraic(String move){
        int file = move.charAt(0) - 'a';
        int rank = move.charAt(1) - '1';
        return rank * 8 + file;
    }

    public Move pickMove(int from, int to){
        for (Move move : Brain.getLegalMoves(game)){
            if (move.from == from && move.to == to){
                return move;
            }
        }
        return null;
    }

    public float staticEvaluation(Board board){
        float score = 0;
        for (int i = 0; i <= 1; i++){
            for (PieceType pieceType : PieceType.values()){
                long pieces = board.getBitBoard(pieceType, (i == 0)? true : false);
                score += Long.bitCount(pieces)* pieceType.baseValue() * ((i==0)? 1 : -1);

                while (pieces != 0){
                    int square = Long.numberOfTrailingZeros(pieces);
                    score += pieceType.getTable()[square] * ((i==0)? 1 : -1);
                    pieces &= pieces - 1;
                }
            }
        }
        return score;
    }

    public float alphaBeta(Game game, int depth, float alpha, float beta){
        if (depth == 0){ // or if game is over
            return staticEvaluation(game.getBoard());
        }
        if (game.getTurn()){
            float maxEval = Float.NEGATIVE_INFINITY;
            for (Move move : Brain.getLegalMoves(game)){
                System.out.println("White to move: " + (4 - (depth-1)) + move);
                //board.printBoard(board);
                Brain.makeMove(move, game);
                game.changeTurn();
                float eval = alphaBeta(game, depth-1, alpha, beta);
                Brain.unMove(move, game);
                game.changeTurn();
                maxEval = Math.max(maxEval, eval);
                alpha = Math.max(alpha, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return maxEval;
        } else {
            float minEval = Float.POSITIVE_INFINITY;
            for (Move move : Brain.getLegalMoves(game)){
                //System.out.println("Black: " + (4 - (depth-1)) + " " + move);
                //board.printBoard(board);
                Brain.makeMove(move, game);
                game.changeTurn();
                float eval = alphaBeta(game, depth-1, alpha, beta);
                Brain.unMove(move, game);
                game.changeTurn();
                minEval = Math.min(minEval, eval);
                beta = Math.min(beta, eval);
                if (beta <= alpha){
                    break;
                }
            }
            return minEval;
        }
    }
}
