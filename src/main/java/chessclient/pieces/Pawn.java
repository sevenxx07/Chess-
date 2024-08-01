package chessclient.pieces;

import chessclient.gamelogic.Board;
import chessclient.gamelogic.Move;
import chessclient.gamelogic.SpecialMove;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Pawn class represents the pawn piece, and implements methods
 * tied with its movement logic.
 */
public class Pawn extends Piece {

    private boolean movedTwo;                         /* Boolean for enabling en passant */
    private boolean hasMoved;                         /* Boolean for taking care of special behavior */
    private final int[][] captureDirections;          /* Directions in which a pawn can capture an enemy piece*/

    public Pawn(int[] initPos, Color clr) {
        super(initPos, clr);
        this.hasMoved = false;
        this.movedTwo = false;

        if (this.clr.equals(Color.BLACK)) {
            this.captureDirections = new int[][]{{1, -1}, {1, 1}};
            this.moveDirections = new int[][]{{1, 0}};                 /* Black's pawns can only move down */
        } else {
            this.captureDirections = new int[][]{{-1, -1}, {-1, 1}};
            this.moveDirections = new int[][]{{-1, 0}};                /* White's pawns can only move up */
        }
    }

    /**
     * Constructor for recreating a Pawn piece from saved game.
     *
     * @param position position of the Pawn on the board
     * @param clr      the color of the Pawn
     * @param hasMoved hasMoved attribute of the saved Pawn
     * @param movedTwo movedTwo attribute of the saved Pawn
     */
    public Pawn(int[] position, Color clr, boolean hasMoved, boolean movedTwo) {
        this(position, clr);
        this.hasMoved = hasMoved;
        this.movedTwo = movedTwo;
    }

    private Pawn(Pawn p) {
        super(p.pos, p.clr);
        this.moveDirections = p.moveDirections;
        this.hasMoved = p.hasMoved;
        this.movedTwo = p.movedTwo;
        this.captureDirections = p.captureDirections;
    }

    @Override
    public List<Move> getPossibleMoves(Board b) {
        Piece[][] board = b.getCurrentBoard();
        List<Move> possMoves = new ArrayList<>();
        int currX = this.pos[0];
        int currY = this.pos[1];
        int firstMoveCoefficient = hasMoved ? 2 : 3;    /* if the pawn has not moved, it can move two squares */
        int[] myMove = new int[2];
        boolean pathObstructed = false;

        for (int i = 1; i < firstMoveCoefficient; i++) {
            int newX = currX + i * this.moveDirections[0][0];
            int newY = currY + i * this.moveDirections[0][1];
            myMove[0] = newX;
            myMove[1] = newY;
            Move move = new Move(this.pos, myMove);
            if (Board.isValidSquare(newX, newY) && board[newX][newY] == null && !pathObstructed) {
                /* if square is empty add it to possible moves */
                if ((newX == 7 && this.clr.equals(Color.BLACK)) || (newX == 0 && this.clr.equals(Color.WHITE))) {
                    //If pawn has traversed the entire board he promotes into another piece
                    possMoves.add(new Move(this.pos, new int[]{newX, newY}, SpecialMove.PROMOTION));
                } else {
                    //Standard move
                    possMoves.add(new Move(this.pos, new int[]{newX, newY}));
                }
            } else {
                pathObstructed = true;
            }
        }

        List<Move> possCaptures = getPossibleCaptures(board);
        possMoves.addAll(possCaptures);

        return possMoves;
    }

    /**
     * Gets a List of Moves which represent captures a Pawn can make without taking into account
     * whether it puts the King of the same color in danger.
     *
     * @param board two dimensional array of Pieces representing the current board
     * @return List of Moves which represent the possible captures
     */
    protected List<Move> getPossibleCaptures(Piece[][] board) {
        List<Move> possCapt = new ArrayList<>();
        int currX = this.pos[0];
        int currY = this.pos[1];

        for (int[] dir : this.captureDirections) {
            int newX = currX + dir[0];
            int newY = currY + dir[1];
            if (Board.isValidSquare(newX, newY)) {
                Piece occupyingPiece = board[newX][newY];
                Piece enPassantPiece = board[currX][newY];
                if (occupyingPiece != null && !occupyingPiece.getColor().equals(this.clr)) {
                    /* if square is occupied by enemy piece add it to possible captures */
                    if ((newX == 7 && this.clr.equals(Color.BLACK)) || (newX == 0 && this.clr.equals(Color.WHITE))) {
                        //If pawn has traversed the entire board he promotes into another piece
                        possCapt.add(new Move(this.pos, new int[]{newX, newY}, SpecialMove.PROMOTION));
                    } else {
                        //Standard move
                        possCapt.add(new Move(this.pos, new int[]{newX, newY}));
                    }
                } else if (enPassantPiece != null && enPassantPiece.getClass().equals(Pawn.class)
                        && !enPassantPiece.getColor().equals(this.clr) && ((Pawn) enPassantPiece).hasMovedTwo()) {
                    /* if square is a viable en passant move add it to possible captures */
                    possCapt.add(new Move(this.pos, new int[]{newX, newY}, SpecialMove.EN_PASSANT));
                }
            }
        }

        return possCapt;
    }

    /**
     * Changes the current position of this Pawn to its new position on the game board, sets
     * the hasMoved attribute to true, if the Pawn moved two tiles forward, it also sets the
     * movedTwo attribute to true.
     *
     * @param newPos new indices which represent the Pawn's new position on the game board
     */
    @Override
    public void updatePosition(int[] newPos) {
        if (this.pos != null && abs(this.pos[0] - newPos[0]) == 2) {
            this.movedTwo = true;
        }
        super.updatePosition(newPos);
        this.hasMoved = true;
    }

    @Override
    public Piece copyPiece() {
        return new Pawn(this);
    }

    /**
     * Generates hash based on the Pawn's attributes, used for testing
     *
     * @return hash calculated from the Pawn's attributes
     */
    @Override
    public int hashCode() {
        int movedTwoFactor = movedTwo ? 59 : 61;
        int hasMovedFactor = hasMoved ? 31 : 37;
        int colorFactor = clr.equals(Color.WHITE) ? 29 : 23;
        return (2 ^ pos[0] * 3 ^ pos[1] * 19 * colorFactor * hasMovedFactor * movedTwoFactor) % Integer.MAX_VALUE;
    }

    /**
     * Compares the given object with this Pawn, and determines if they're
     * equal based on the Objects class, and attributes
     *
     * @param o Object compared with this Pawn
     * @return true if the Object is a Pawn, and its attributes are equal to
     * this Pawn's
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pawn)) {
            return false;
        } else {
            Pawn p = (Pawn) o;
            return this.hashCode() == p.hashCode();
        }
    }

    /**
     * Returns the movedTwo attribute which determines whether the Pawn
     * can be captured by an en passant move or not.
     *
     * @return movedTwo attribute pivotal for en passant moves
     */
    public boolean hasMovedTwo() {
        return movedTwo;
    }

    /**
     * Returns the hasMoved attribute which determines whether the Pawn
     * can move two squares on its first move.
     *
     * @return hasMoved attribute pivotal for the Pawn's first move
     */
    public boolean hasMoved() {
        return this.hasMoved;
    }

    /**
     * Sets the movedTwo attribute to false.
     */
    public void updateMovedTwo() {
        this.movedTwo = false;
    }

    @Override
    public String toString() {
        return "pawn";
    }
}
