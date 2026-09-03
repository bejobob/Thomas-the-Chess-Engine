/**
 * MoveType
 * An ENUM to help identify what type of move a move is
 * @author Benjamin Kealey
 * @version 2026/09/02 - Rework branch
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
