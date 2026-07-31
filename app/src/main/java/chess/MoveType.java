/**
 * MoveType
 * An ENUM to help identify what type of move a move is
 * @author Benjamin Kealey
 * @version 2026/07/30
 */

package chess;

public enum MoveType {
    MOVE,
    CAPTURE,
    SHORTCASTLE,
    LONGCASTLE,
    PROMOTION,
    CAPTURE_PROMOTION
}
