/**
 * Board
 * Represents a chess board with all the pieces on it. Can also calculate how much material each player has left.
 * @author Benjamin Kealey
 * @version 2026/08/26
 */

package chess;

public class Board {
    public long whitePawns;
    public long whiteRooks;
    public long whiteKnights;
    public long whiteBishops;
    public long whiteQueens;
    public long whiteKing;
    public long blackPawns;
    public long blackRooks;
    public long blackKnights;
    public long blackBishops;
    public long blackQueens;
    public long blackKing;
    public long whitePieces;
    public long blackPieces;

    public boolean wO_O = true;
    public boolean wO_O_O = true;
    public boolean bO_O = true;
    public boolean bO_O_O = true;

    public Board(long whitePawns, long whiteRooks, long whiteKnights, long whiteBishops, long whiteQueens, long whiteKing,
                 long blackPawns, long blackRooks, long blackKnights, long blackBishops, long blackQueens, long blackKing) {
        this.whitePawns = whitePawns;
        this.whiteRooks = whiteRooks;
        this.whiteKnights = whiteKnights;
        this.whiteBishops = whiteBishops;
        this.whiteQueens = whiteQueens;
        this.whiteKing = whiteKing;
        this.blackPawns = blackPawns;
        this.blackRooks = blackRooks;
        this.blackKnights = blackKnights;
        this.blackBishops = blackBishops;
        this.blackQueens = blackQueens;
        this.blackKing = blackKing;
        whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;
        blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;
    }

    public long getBitboard(PieceType pieceType, boolean white){
        if (pieceType.equals(PieceType.PAWN)){
            return white? whitePawns : blackPawns;
        } else if (pieceType.equals(PieceType.KNIGHT)){
            return white? whiteKnights : blackKnights;
        } else if (pieceType.equals(PieceType.BISHOP)){
            return white? whiteBishops : blackBishops;
        } else if (pieceType.equals(PieceType.ROOK)){
            return white? whiteRooks : blackRooks;
        } else if (pieceType.equals(PieceType.QUEEN)){
            return white? whiteQueens : blackQueens;
        } else if (pieceType.equals(PieceType.KING)) {
            return white? whiteKing : blackKing;
        }
        return 0L;
    }

    public void setBitboard(PieceType pieceType, Long val, boolean white){
        switch (pieceType) {
            case PAWN:
                if (white){whitePawns = val;} else {blackPawns = val;}
                break;
            case KNIGHT:
                if (white){whiteKnights = val;} else {blackKnights = val;}
                break;
            case BISHOP:
                if (white){whiteBishops = val;} else {blackBishops = val;}
                break;
            case ROOK:
                if (white){whiteRooks = val;} else {blackRooks = val;}
                break;
            case QUEEN:
                if (white){whiteQueens = val;} else {blackQueens = val;}
                break;
            case KING:
                if (white){whiteKing = val;} else {blackKing = val;}
                break;
            default:
                break;
        }
        
        whitePieces = whitePawns | whiteRooks | whiteKnights | whiteBishops | whiteQueens | whiteKing;
        blackPieces = blackPawns | blackRooks | blackKnights | blackBishops | blackQueens | blackKing;
    }

    public boolean equals(Board otherBoard){
        if (this.whitePawns != otherBoard.whitePawns) return false;
        if (this.blackPawns != otherBoard.whitePawns) return false;
        if (this.whiteKnights != otherBoard.whiteKnights) return false;
        if (this.blackKnights != otherBoard.whiteKnights) return false;
        if (this.whiteBishops != otherBoard.whiteBishops) return false;
        if (this.blackBishops != otherBoard.blackBishops) return false;
        if (this.whiteRooks != otherBoard.whiteRooks) return false;
        if (this.blackRooks != otherBoard.blackRooks) return false;
        if (this.whiteQueens != otherBoard.whiteQueens) return false;
        if (this.blackQueens != otherBoard.whiteQueens) return false;
        if (this.whiteKing != otherBoard.whiteKing) return false;
        if (this.blackKing != otherBoard.blackKing) return false;

        return true;
    }

    public void printBoard(Board board) {
    System.out.println("  +-----------------+");

    for (int rank = 7; rank >= 0; rank--) {
        System.out.print((rank + 1) + " | ");

        for (int file = 0; file < 8; file++) {
            int square = rank * 8 + file;
            long bit = 1L << square;

            char piece = '.';

            // White pieces
            if ((board.whitePawns & bit) != 0) {
                piece = 'P';
            } else if ((board.whiteKnights & bit) != 0) {
                piece = 'N';
            } else if ((board.whiteBishops & bit) != 0) {
                piece = 'B';
            } else if ((board.whiteRooks & bit) != 0) {
                piece = 'R';
            } else if ((board.whiteQueens & bit) != 0) {
                piece = 'Q';
            } else if ((board.whiteKing & bit) != 0) {
                piece = 'K';

            // Black pieces
            } else if ((board.blackPawns & bit) != 0) {
                piece = 'p';
            } else if ((board.blackKnights & bit) != 0) {
                piece = 'n';
            } else if ((board.blackBishops & bit) != 0) {
                piece = 'b';
            } else if ((board.blackRooks & bit) != 0) {
                piece = 'r';
            } else if ((board.blackQueens & bit) != 0) {
                piece = 'q';
            } else if ((board.blackKing & bit) != 0) {
                piece = 'k';
            }

            System.out.print(piece + " ");
        }

        System.out.println("|");
    }

    System.out.println("  +-----------------+");
    System.out.println("    a b c d e f g h");
}
}
