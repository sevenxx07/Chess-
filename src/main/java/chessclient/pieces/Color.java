package chessclient.pieces;

/**
 * Enum Color represents the two colors of the chess pieces - black, and white.
 */
public enum Color {
    BLACK,
    WHITE;

    /**
     * Returns String representation for the enum options
     *
     * @return String "b" for Color.BLACK, or "w" Color.WHITE
     */
    @Override
    public String toString() {
        return this.equals(Color.BLACK) ? "b" : "w";
    }
}
