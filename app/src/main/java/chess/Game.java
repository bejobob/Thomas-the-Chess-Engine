/**
 * Game
 * Represents the state of the game including the board, the moves made, and whose turn it is.
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
 */

package chess;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Game {
    private ArrayList<Move> selectedMoves;
    private Map<Board, Integer> positions;
    private Board board;
    private boolean whiteToMove = true;

    public Game(Board board) {
        this.board = board;
    }

    public boolean getTurn(){
        return whiteToMove;
    }

    public Board getBoard(){
        return board;
    }

    public void changeTurn(){
        whiteToMove = !whiteToMove;
    }
}
