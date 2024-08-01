package chessclient.pieces;

import chessclient.gamelogic.Board;
import chessclient.gamelogic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Rook class represents the rook piece, and implements methods
 * tied with its movement logic.
 */
public class Rook extends Piece {

    private boolean hasMoved;               /* Boolean for taking care of special behavior */

    public Rook(int[] initPos, Color clr) {
        super(initPos, clr);
        this.hasMoved = false;

        /* Rook can move only horizontally or vertically */
        this.moveDirections = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    }

    /**
     * Constructor for recreating a Rook piece from saved game.
     *
     * @param position position of the Rook on the board
     * @param clr      the color of the Rook
     * @param hasMoved hasMoved attribute of the saved Rook
     */
    public Rook(int[] position, Color clr, boolean hasMoved) {
        this(position, clr);
        this.hasMoved = hasMoved;
    }

    private Rook(Rook r) {
        super(r.pos, r.clr);
        this.moveDirections = r.moveDirections;
        this.hasMoved = r.hasMoved;
    }

    @Override
    public List<Move> getPossibleMoves(Board b) {
        Piece[][] board = b.getCurrentBoard();
        List<Move> possMoves = new ArrayList<>();

        for (int[] direction : this.moveDirections) {
            int currX = this.pos[0] + direction[0];
            int currY = this.pos[1] + direction[1];
            int[] myMove = new int[2];
            myMove[0] = currX;
            myMove[1] = currY;

            while (Board.isValidSquare(currX, currY)) {
                Piece occupyingPiece = board[currX][currY];
                Move move = new Move(this.pos, myMove);
                if (occupyingPiece == null) {
                    /* Rook can move onto an empty square and continue in the given direction */
                    possMoves.add(new Move(this.pos, new int[]{currX, currY}));
                    currX += direction[0];
                    currY += direction[1];
                    myMove[0] = currX;
                    myMove[1] = currY;
                } else if (!occupyingPiece.getColor().equals(this.clr)) {
                    /* Rook can capture enemy piece, but can't move further in the given direction */
                    possMoves.add(new Move(this.pos, new int[]{currX, currY}));
                    break;
                } else {
                    /* Rook can't move in the given direction past a piece with the same color, nor capture it*/
                    break;
                }
            }
        }
        return possMoves;
    }

    @Override
    public Piece copyPiece() {
        return new Rook(this);
    }

    /**
     * Changes the current position of this Rook to its new position on the game board, and
     * sets the hasMoved attribute to true.
     *
     * @param newPos new indices which represent the Rook's new position on the game board
     */
    @Override
    public void updatePosition(int[] newPos) {
        super.updatePosition(newPos);
        this.hasMoved = true;
    }

    /**
     * Generates hash based on the Rook's attributes, used for testing
     *
     * @return hash calculated from the Rook's attributes
     */
    @Override
    public int hashCode() {
        int hasMovedFactor = hasMoved ? 31 : 37;
        int colorFactor = clr.equals(Color.WHITE) ? 29 : 23;
        return (2 ^ pos[0] * 3 ^ pos[1] * 11 * colorFactor * hasMovedFactor) % Integer.MAX_VALUE;
    }

    /**
     * Compares the given object with this Rook, and determines if they're
     * equal based on the Objects class, and attributes
     *
     * @param o Object compared with this Rook
     * @return true if the Object is a Rook, and its attributes are equal to
     * this Rook's
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Rook)) {
            return false;
        } else {
            Rook r = (Rook) o;
            return this.hashCode() == r.hashCode();
        }
    }

    @Override
    public String toString() {
        return "rook";
    }

    /**
     * Returns hasMoved attribute which determines whether a King can make
     * the castle move with this Rook.
     *
     * @return hasMoved attribute of the Rook
     */
    public boolean hasMoved() {
        return hasMoved;
    }

}
