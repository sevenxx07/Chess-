package cchessclient.pieces;

import chessclient.gamelogic.Board;
import chessclient.gamelogic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Bishop class represents the bishop chess piece, and implements methods
 * tied with its movement logic.
 */
public class Bishop extends Piece {

    public Bishop(int[] initPos, Color clr) {
        super(initPos, clr);

        /* bishop can move only diagonally*/
        this.moveDirections = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    }

    private Bishop(Bishop b) {
        super(b.pos, b.clr);
        this.moveDirections = b.moveDirections;
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
                if ((occupyingPiece == null)) {
                    possMoves.add(new Move(this.pos, new int[]{currX, currY}));
                    currX += direction[0];
                    currY += direction[1];
                    myMove[0] = currX;
                    myMove[1] = currY;
                } else if (occupyingPiece.getColor() != this.clr) {
                    possMoves.add(new Move(this.pos, new int[]{currX, currY}));
                    break;
                } else {
                    break;
                }
            }
        }
        return possMoves;
    }

    @Override
    public Piece copyPiece() {
        return new Bishop(this);
    }

    /**
     * Generates hash based on the Bishop's attributes, used for testing
     *
     * @return hash calculated from the Bishop's attributes
     */
    @Override
    public int hashCode() {
        int colorFactor = clr.equals(Color.WHITE) ? 29 : 23;
        return (2 ^ pos[0] * 3 ^ pos[1] * colorFactor * 13) % Integer.MAX_VALUE;
    }

    /**
     * Compares the given object with this Bishop, and determines if they're
     * equal based on the Objects class, and attributes
     *
     * @param o Object compared with this Bishop
     * @return true if the Object is a Bishop, and its attributes are equal to
     * this Bishop's
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Bishop)) {
            return false;
        } else {
            Bishop b = (Bishop) o;
            return this.hashCode() == b.hashCode();
        }
    }

    @Override
    public String toString() {
        return "bishop";
    }
}
