package chessclient.pieces;

import chessclient.gamelogic.Board;
import chessclient.gamelogic.Move;
import chessclient.gamelogic.Ruleset;

import java.util.ArrayList;
import java.util.List;

/**
 * Piece is the parent class for the classes representing individual chess pieces. It also
 * defines basic behavior of these classes.
 */
public abstract class Piece {

    protected int[] pos;
    protected Color clr;
    protected int[][] moveDirections;

    /**
     * Creates a new Piece on the initial position on the game board, and of the given color
     *
     * @param initPos indices in the game board which represent the initial position of the piece
     *                on the board
     * @param clr     Color enum representing the color of the piece
     */
    public Piece(int[] initPos, Color clr) {
        this.pos = initPos;
        this.clr = clr;
    }

    /**
     * Creates a new object with the same attributes and the same class as the given Piece.
     *
     * @return copy of the given Piece.
     */
    public abstract Piece copyPiece();

    /**
     * Returns a list of the possible moves of the given piece which respect the piece's movement
     * logic, but not the validity in the terms of the move resulting in the king of the same color
     * being checked.
     *
     * @param b Board, on which the Piece is placed
     * @return List of Moves which the Piece could make given its movement logic
     */
    public abstract List<Move> getPossibleMoves(Board b);

    /**
     * Returns a list of valid moves the given piece can make with regards to the piece's
     * movement logic, and with regards to the move resulting in the king of the same color
     * being checked.
     *
     * @param b Board, on which the Piece is placed
     * @return List of Moves which the Piece could make given its movement logic, and which
     * do not result in the king of the same color being checked
     */
    public List<Move> getValidMoves(Board b) {
        ArrayList<Move> possMoves = new ArrayList<>(getPossibleMoves(b));
        ArrayList<Move> validMoves = new ArrayList<>();
        for (Move m : possMoves) {
            if (Ruleset.isValidMove(b, m)) {
                validMoves.add(m);
            }
        }
        return validMoves;
    }

    /**
     * Changes the current position of the Piece to its new position on the game board.
     *
     * @param newPos new indices which represent the Piece's new position on the game board
     */
    public void updatePosition(int[] newPos) {
        this.pos = newPos;
    }

    /**
     * Return this Piece's color
     *
     * @return Color of the piece
     */
    public Color getColor() {
        return this.clr;
    }

    /**
     * return this Piece's position
     *
     * @return indices into the game board, on which the piece is currently placed
     */
    public int[] getPosition() {
        return pos;
    }
}
