package cz.cvut.fel.pjv.chessclient.pieces;

import cz.cvut.fel.pjv.chessclient.gamelogic.Board;
import cz.cvut.fel.pjv.chessclient.gamelogic.Move;
import cz.cvut.fel.pjv.chessclient.gamelogic.Ruleset;
import cz.cvut.fel.pjv.chessclient.gamelogic.SpecialMove;

import java.util.ArrayList;
import java.util.List;

/**
 * King class represents the king piece, and implements methods
 * tied with its movement logic.
 */
public class King extends Piece {

    private boolean hasMoved;

    public King(int[] initPos, Color clr) {
        super(initPos, clr);
        this.hasMoved = false;

        this.moveDirections = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
    }

    /**
     * Constructor for recreating a King piece from saved game.
     *
     * @param position position of the King on the board
     * @param clr      the color of the King
     * @param hasMoved hasMoved attribute of the saved King
     */
    public King(int[] position, Color clr, boolean hasMoved) {
        this(position, clr);
        this.hasMoved = hasMoved;
    }

    private King(King k) {
        super(k.pos, k.clr);
        this.moveDirections = k.moveDirections;
        this.hasMoved = k.hasMoved;
    }


    @Override
    public List<Move> getPossibleMoves(Board b) {
        List<Move> possMoves = this.getPossibleNormalMoves(b);

        possMoves.addAll(this.getCastle(b));

        return possMoves;
    }

    /**
     * Generates a List containing all the possible Moves of this King except for castles.
     *
     * @param b the current Board
     * @return List containing all possible Moves of this King except for castles
     */
    public List<Move> getPossibleNormalMoves(Board b) {
        Piece[][] board = b.getCurrentBoard();
        List<Move> possMoves = new ArrayList<>();

        int startX = this.pos[0];
        int startY = this.pos[1];

        for (int[] dir : this.moveDirections) {
            int newX = startX + dir[0];
            int newY = startY + dir[1];

            if (Board.isValidSquare(newX, newY)) {
                Piece occupyingPiece = board[newX][newY];
                if (occupyingPiece == null || !occupyingPiece.getColor().equals(this.clr)) {
                    Move possMove = new Move(this.pos, new int[]{newX, newY});
                    possMoves.add(possMove);
                }
            }
        }
        return possMoves;
    }

    /* Checks if the King can make a castle move with either of the Rooks of the same color,
     * and returns a List containing the castle moves if so */
    private List<Move> getCastle(Board b) {
        Piece[][] board = b.getCurrentBoard();
        List<Move> castle = new ArrayList<>();

        if (this.hasMoved) {
            return castle;
        }

        Piece r1 = board[this.pos[0]][0];
        if (isViableCastle(r1, b)) {
            castle.add(new Move(this.pos, new int[]{this.pos[0], 2}, SpecialMove.CASTLE));
        }

        Piece r2 = board[this.pos[0]][7];
        if (isViableCastle(r2, b)) {
            castle.add(new Move(this.pos, new int[]{this.pos[0], 6}, SpecialMove.CASTLE));
        }

        return castle;
    }

    /* Checks if the King can make a castle move with the given Rook */
    private boolean isViableCastle(Piece r, Board b) {
        Piece[][] board = b.getCurrentBoard();

        if (r != null && r.getClass().equals(Rook.class) && !((Rook) r).hasMoved()) {
            List<int[]> traversedSq = new ArrayList<>();
            int castleDirection = r.getPosition()[1] > this.pos[1] ? 2 : -2;
            int yStart = Math.max(this.pos[1], this.pos[1] + castleDirection);
            int yEnd = Math.min(this.pos[1], this.pos[1] + castleDirection);
            for (int i = yStart; i >= yEnd; i--) {
                if (board[this.pos[0]][i] != null && !(board[this.pos[0]][i] instanceof King)) {
                    return false;
                } else {
                    //Check if the square traversed by king isn't threatened by enemy pieces
                    int[] traversedSquare = new int[]{this.pos[0], i};
                    if (!Ruleset.isValidMove(b, new Move(this.pos, traversedSquare))) {
                        return false;
                    }
                }
            }
            return true;
        }

        return false;
    }


    @Override
    public Piece copyPiece() {
        return new King(this);
    }

    /**
     * Changes the current position of this King to its new position on the game board, and
     * sets the hasMoved attribute to true.
     *
     * @param newPos new indices which represent the King's new position on the game board
     */
    @Override
    public void updatePosition(int[] newPos) {
        super.updatePosition(newPos);
        this.hasMoved = true;
    }

    /**
     * Generates hash based on the King's attributes, used for testing
     *
     * @return hash calculated from the King's attributes
     */
    @Override
    public int hashCode() {
        int hasMovedFactor = hasMoved ? 31 : 37;
        int colorFactor = clr.equals(Color.WHITE) ? 29 : 23;
        return (2 ^ pos[0] * 3 ^ pos[1] * 5 * colorFactor * hasMovedFactor) % Integer.MAX_VALUE;
    }

    /**
     * Compares the given object with this King, and determines if they're
     * equal based on the Objects class, and attributes
     *
     * @param o Object compared with this King
     * @return true if the Object is a King, and its attributes are equal to
     * this King's
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof King)) {
            return false;
        } else {
            King k = (King) o;
            return this.hashCode() == k.hashCode();
        }
    }

    /**
     * Returns hasMoved attribute which determines whether a King can make
     * the castle move with one of the Rooks of the same color.
     *
     * @return hasMoved attribute of the King
     */
    public boolean hasMoved() {
        return this.hasMoved;
    }

    @Override
    public String toString() {
        return "king";
    }

}
