package cz.cvut.fel.pjv.chessclient.pieces;

import cz.cvut.fel.pjv.chessclient.gamelogic.Board;
import cz.cvut.fel.pjv.chessclient.gamelogic.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Queen class represents the queen chess piece, and implements methods
 * tied with its movement logic.
 */
public class Queen extends Piece {

    public Queen(int[] initPos, Color clr) {
        super(initPos, clr);

        /* queen can move diagonally, horizontally or vertically*/
        this.moveDirections = new int[][]{{1, 1}, {1, -1}, {-1, 1},
                {-1, -1}, {1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    }

    private Queen(Queen q) {
        super(q.pos, q.clr);
        this.moveDirections = q.moveDirections;
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
        return new Queen(this);
    }

    /**
     * Generates hash based on the Queen's attributes, used for testing
     *
     * @return hash calculated from the Queen's attributes
     */
    @Override
    public int hashCode() {
        int colorFactor = clr.equals(Color.WHITE) ? 29 : 23;
        return (2 ^ pos[0] * 3 ^ pos[1] * 7 * colorFactor) % Integer.MAX_VALUE;
    }

    /**
     * Compares the given object with this Queen, and determines if they're
     * equal based on the Objects class, and attributes
     *
     * @param o Object compared with this Queen
     * @return true if the Object is a Queen, and its attributes are equal to
     * this Queen's
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Queen)) {
            return false;
        } else {
            Queen q = (Queen) o;
            return this.hashCode() == q.hashCode();
        }
    }

    @Override
    public String toString() {
        return "queen";
    }

}
