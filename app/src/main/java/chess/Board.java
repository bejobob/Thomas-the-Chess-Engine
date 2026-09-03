/**
 * Board
 * Represents a chess board with all the pieces on it. Can also calculate how much material each player has left.
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
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

    public Board(long whitePawns, long whiteKnights, long whiteBishops, long whiteRooks, long whiteQueens, long whiteKing,
                 long blackPawns, long blackKnights, long blackBishops, long blackRooks, long blackQueens, long blackKing) {

        this.whitePawns = whitePawns;
        this.whiteKnights = whiteKnights;
        this.whiteBishops = whiteBishops;
        this.whiteRooks = whiteRooks;
        this.whiteQueens = whiteQueens;
        this.whiteKing = whiteKing;

        this.blackPawns = blackPawns;
        this.blackKnights = blackKnights;
        this.blackBishops = blackBishops;
        this.blackRooks = blackRooks;
        this.blackQueens = blackQueens;
        this.blackKing = blackKing;

        whitePieces = whitePawns | whiteKnights | whiteBishops | whiteRooks | whiteQueens | whiteKing;
        blackPieces = blackPawns | blackKnights | blackBishops | blackRooks | blackQueens | blackKing;
    }

    public long getBitBoard(PieceType pieceType, boolean white) {
        switch (pieceType) {
            case PAWN:
                return white ? whitePawns : blackPawns;
            case KNIGHT:
                return white ? whiteKnights : blackKnights;
            case BISHOP:
                return white ? whiteBishops : blackBishops;
            case ROOK:
                return white ? whiteRooks : blackRooks;
            case QUEEN:
                return white ? whiteQueens : blackQueens;
            case KING:
                return white ? whiteKing : blackKing;
            default:
                throw new IllegalArgumentException("Invalid piece type");
        }
    }

    public void setBitBoard(PieceType pieceType, boolean white, long bitBoard){
        switch(pieceType) {
            case PAWN:
                if (white){whitePawns = bitBoard;} else {blackPawns = bitBoard;}
                break;
            case KNIGHT:
                if (white){whiteKnights = bitBoard;} else {blackKnights = bitBoard;}
                break;
            case BISHOP:
                if (white){whiteBishops = bitBoard;} else {blackBishops = bitBoard;}
                break;
            case ROOK:
                if (white){whiteRooks = bitBoard;} else {blackRooks = bitBoard;}
                break;
            case QUEEN:
                if (white){whiteQueens = bitBoard;} else {blackQueens = bitBoard;}
                break;
            case KING:
                if (white){whiteKing = bitBoard;} else {blackKing = bitBoard;}
                break;
            default:
                break;
        }
        whitePieces = whitePawns | whiteKnights | whiteBishops | whiteRooks | whiteQueens | whiteKing;
        blackPieces = blackPawns | blackKnights | blackBishops | blackRooks | blackQueens | blackKing;
    }

    public PieceType getPieceOnSquare(int square){

        if (((1L << square) & (whitePawns | blackPawns)) != 0){
            return PieceType.PAWN;
        } else if (((1L << square) & (whiteKnights | blackKnights)) != 0) {
            return PieceType.KNIGHT;
        } else if (((1L << square) & (whiteBishops | whiteBishops)) != 0) {
            return PieceType.BISHOP;
        } else if (((1L << square) & (whiteRooks | blackRooks)) != 0) {
            return PieceType.ROOK;
        } else if (((1L << square) & (whiteQueens | blackQueens)) != 0) {
            return PieceType.QUEEN;
        } else if (((1L << square) & (whiteKing | blackKing)) != 0) {
            return PieceType.KING;
        } else {
            return null;
        }
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
