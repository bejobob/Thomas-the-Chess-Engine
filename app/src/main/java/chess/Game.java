package chess;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Game {
    private ArrayList<Move> selectedMoves;
    private Map<Board, Integer> positions;
    private Board board;
    private boolean whiteToMove = true;

    public Game(Board board){
        selectedMoves = new ArrayList<>();
        positions = new HashMap<>();
        this.board = board;
    }

    /**
     * Adds the most recent move to the list of made moves
     * @param move the move to be added
     */
    public void addMove(Move move){
        selectedMoves.add(move);
    }

    /**
     * Gets the last move made. This is needed to perform en-passant, which depends on the last move.
     * @return the last move made so we can determine which piece was moved from where to where.
     */
    public Move lastMove(){
        if (selectedMoves.size() == 0) return null;
        else return selectedMoves.get(selectedMoves.size()-1);
    }

    /**
     * Checks the list of all past positions to see if there is threefold repetition present, triggering a draw
     * @return whehter the same position has appeared three times on the board
     */
    public boolean threefoldRepetition(){
        for (Board position : positions.keySet()){
            if (positions.get(position) == 3) return true;
        }
        return false;
    }

    /**
     * Adds the new position to the list of all past positions. Needed to determine if threefold repetition has taken place.
     * @param board the board is the state of the board, or the position
     */
    public void addPosition(Board board){
        positions.put(board, positions.getOrDefault(board, 0) + 1);
    }

    public ArrayList<Move> getSelectedMoves() {
        return selectedMoves;
    }

    public Board getBoard() {
        return board;
    }

    public boolean isWhiteToMove() {
        return whiteToMove;
    }

    public void changeTurn() {
        whiteToMove = !whiteToMove;
    }
}
