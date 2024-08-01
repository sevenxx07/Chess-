package cz.cvut.fel.pjv.chessclient.pieces;

import cz.cvut.fel.pjv.chessclient.gamelogic.Board;
import cz.cvut.fel.pjv.chessclient.gamelogic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight class represents the knight piece, and implements methods
 * tied with its movement logic.
 */
public class Knight extends Piece {

    public Knight(int[] initPos, Color clr) {
        super(initPos, clr);

        /* knight can move only in the shape of L*/
        this.moveDirections = new int[][]{{1, 2}, {2, 1}, {-1, 2}, {-2, 1},
                {-2, -1}, {-1, -2}, {1, -2}, {2, -1}};
    }

    private Knight(Knight k) {
        super(k.pos, k.clr);
        this.moveDirections = k.moveDirections;
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

            //knight can be moved only once in this direction
            if (Board.isValidSquare(currX, currY)) {
                Piece occupyingPiece = board[currX][currY];
                Move move = new Move(this.pos, myMove);
                if ((occupyingPiece == null)) {
                    possMoves.add(new Move(this.pos, new int[]{currX, currY}));
                } else if (occupyingPiece.getColor() != this.clr) {
                    possMoves.add(new Move(this.pos, new int[]{currX, currY}));
                }
            }
        }
        return possMoves;
    }

    @Override
    public Piece copyPiece() {
        return new Knight(this);
    }

    /**
     * Generates hash based on the Knight's attributes, used for testing
     *
     * @return hash calculated from the Knight's attributes
     */
    @Override
    public int hashCode() {
        int colorFactor = clr.equals(Color.WHITE) ? 29 : 23;
        return (2 ^ pos[0] * 3 ^ pos[1] * 17 * colorFactor) % Integer.MAX_VALUE;
    }

    /**
     * Compares the given object with this Knight, and determines if they're
     * equal based on the Objects class, and attributes
     *
     * @param o Object compared with this Knight
     * @return true if the Object is a Knight, and its attributes are equal to
     * this Knight's
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Knight)) {
            return false;
        } else {
            Knight k = (Knight) o;
            return this.hashCode() == k.hashCode();
        }
    }

    @Override
    public String toString() {
        return "knight";
    }

}
