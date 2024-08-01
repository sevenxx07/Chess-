package cz.cvut.fel.pjv.chessclient.gamelogic;


/**
 * Move stores information about the starting and terminal position of
 * the moved Piece, as well as the special character of the Move e.g.
 * castle, en passant, or promotion.
 */
public class Move {
    private int[] from;
    private int[] to;
    private SpecialMove specialMove;
    private String promotionIdentifier;

    /**
     * Constructor used by the Jackson library. You shouldn't manually initialize
     * Move via this constructor.
     */
    public Move() {

    }

    /**
     * Creates an ordinary Move which holds only information about the starting
     * and terminal position of the moved Piece.
     *
     * @param from starting position of the moved Piece
     * @param to   terminal position of the moved Piece
     */
    public Move(int[] from, int[] to) {
        this.from = from;
        this.to = to;
        this.specialMove = null;
        this.promotionIdentifier = null;
    }

    /**
     * Creates a special Move which stores information about the starting and terminal
     * position of the moved Piece, as well as the special character of the Move.
     *
     * @param from    starting position of the moved Piece
     * @param to      terminal position of the moved Piece
     * @param special special character of the Move
     */
    public Move(int[] from, int[] to, SpecialMove special) {
        this.from = from;
        this.to = to;
        this.specialMove = special;
        this.promotionIdentifier = null;
    }

    /**
     * Creates a hash code from the Move's attributes, used for testing.
     *
     * @return hash code of the Move
     */
    @Override
    public int hashCode() {
        int smFactor;
        if (specialMove == null) {
            smFactor = 41;
        } else if (specialMove.equals(SpecialMove.CASTLE)) {
            smFactor = 43;
        } else if (specialMove.equals(SpecialMove.EN_PASSANT)) {
            smFactor = 47;
        } else {
            smFactor = 53;
        }
        return (2 ^ from[0] * 3 ^ from[1] * 5 ^ to[0] * 7 ^ to[1] * smFactor) % Integer.MAX_VALUE;
    }

    /**
     * Compares this Move object with another Object, and determines whether they equal
     *
     * @param o Object which is compared with this Move
     * @return true if the given objects is a Move and has the same attributes as this Move,
     * else false
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Move)) {
            return false;
        } else {
            Move m = (Move) o;
            return this.hashCode() == m.hashCode();
        }
    }

    /**
     * Sets the identifier of the promotion special move, which determines
     * into what Piece does the Pawn promote
     *
     * @param promotionIdentifier the first letter of the Piece into which
     *                            the Pawn is promoted
     */
    public void setPromotionIdentifier(String promotionIdentifier) {
        this.promotionIdentifier = promotionIdentifier;
    }

    /**
     * Returns the identifier of the promotion special move, which determines
     * into what Piece does the Pawn promote
     *
     * @return identifier of the promotion special move
     */
    public String getPromotionIdentifier() {
        return this.promotionIdentifier;
    }

    /**
     * Indices of the starting position of the moved Piece on the game board
     *
     * @return starting position of the moved piece
     */
    public int[] getFrom() {
        return from;
    }

    /**
     * Indices of the terminal position of the moved Piece on the game board
     *
     * @return terminal position of the moved piece
     */
    public int[] getTo() {
        return to;
    }

    /**
     * Returns SpecialMove enum which determines the special character of the Move
     *
     * @return SpecialMove which dictates the special character of the Move
     */
    public SpecialMove getSpecialMove() {
        return specialMove;
    }
}
