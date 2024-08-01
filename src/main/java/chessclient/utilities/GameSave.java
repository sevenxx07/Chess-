package chessclient.utilities;

import chessclient.gamelogic.Board;
import chessclient.gamelogic.ChessClock;
import chessclient.pieces.*;

/**
 * GameSave is a class which stores the current state of the chess game that can be saved and
 * later loaded in order to continue the game. It also takes care of serialization and deserialization
 * of the game board.
 */
public class GameSave {

    private ChessClock whiteClock;
    private ChessClock blackClock;
    private String[][] serializedBoard;
    private String turn;

    /**
     * Constructor used by the Jackson library. You shouldn't initialize GameSave via this
     * constructor.
     */
    public GameSave() {

    }

    /**
     * Creates a GameSave which represents the current state of the game, which can later
     * be loaded, and the game can continue.
     *
     * @param whiteClock paused ChessClock of the white player
     * @param blackClock paused ChessClock of the black player
     * @param b          the current Board
     * @param turn       String which identifies player whose turn it is
     */
    public GameSave(ChessClock whiteClock, ChessClock blackClock, Board b, String turn) {
        this.whiteClock = whiteClock;
        this.blackClock = blackClock;
        this.serializedBoard = this.serializeBoard(b);
        this.turn = turn;
    }

    /* Creates a two dimensional array of Strings, in which each String at given indices represents
     * a Piece at the same indices on the game board */
    private String[][] serializeBoard(Board b) {
        String[][] serializedBoard = new String[Board.BOARD_HEIGHT][Board.BOARD_WIDTH];
        Piece[][] pieces = b.getCurrentBoard();
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                if (pieces[i][j] == null) {
                    serializedBoard[i][j] = null;
                } else {
                    serializedBoard[i][j] = this.serializePiece(pieces[i][j]);
                }
            }
        }
        return serializedBoard;
    }

    /* Creates a String representation of the given Piece and it's attributes */
    private String serializePiece(Piece p) {
        switch (p.toString()) {
            case "king": {
                String hasMoved = ((King) p).hasMoved() ? "t" : "f";
                return p.getColor().toString() + hasMoved + p.toString();
            }
            case "rook": {
                String hasMoved = ((Rook) p).hasMoved() ? "t" : "f";
                return p.getColor().toString() + hasMoved + p.toString();
            }
            case "pawn": {
                String hasMoved = ((Pawn) p).hasMoved() ? "t" : "f";
                String movedTwo = ((Pawn) p).hasMovedTwo() ? "t" : "f";
                return p.getColor().toString() + hasMoved + movedTwo + p.toString();
            }
            default:
                return p.getColor().toString() + p.toString();
        }
    }

    /**
     * Reconstructs a Board object from the serialized board state.
     *
     * @return Board
     */
    public Board deserializeBoard() {
        Piece[][] pieces = new Piece[Board.BOARD_HEIGHT][Board.BOARD_WIDTH];
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                if (this.serializedBoard[i][j] == null) {
                    pieces[i][j] = null;
                } else {
                    pieces[i][j] = this.deserializePiece(this.serializedBoard[i][j], i, j);
                }
            }
        }
        return new Board(pieces);
    }

    /* Reconstructs a Piece from its serialized state */
    private Piece deserializePiece(String s, int row, int col) {
        if (s == null) {
            return null;
        }

        Color color = s.charAt(0) == 'w' ? Color.WHITE : Color.BLACK;
        boolean hasMoved = true;
        boolean movedTwo = true;
        if (s.contains("king") || s.contains("rook")) {
            hasMoved = s.charAt(1) == 't';
            s = s.substring(2);
        } else if (s.contains("pawn")) {
            hasMoved = s.charAt(1) == 't';
            movedTwo = s.charAt(2) == 't';
            s = s.substring(3);
        } else {
            s = s.substring(1);
        }

        switch (s) {
            case "king":
                return new King(new int[]{row, col}, color, hasMoved);
            case "queen":
                return new Queen(new int[]{row, col}, color);
            case "knight":
                return new Knight(new int[]{row, col}, color);
            case "rook":
                return new Rook(new int[]{row, col}, color, hasMoved);
            case "bishop":
                return new Bishop(new int[]{row, col}, color);
            case "pawn":
                return new Pawn(new int[]{row, col}, color, hasMoved, movedTwo);
            default:
                return null;
        }
    }

    /**
     * Returns the Color of the player whose turn it is
     *
     * @return Color of the player whose turn it is
     */
    public Color turnColor() {
        if (this.turn.equals("white")) {
            return Color.WHITE;
        } else {
            return Color.BLACK;
        }
    }

    /**
     * Returns the ChessClock of the white player.
     *
     * @return ChessClock of the white player
     */
    public ChessClock getWhiteClock() {
        return this.whiteClock;
    }

    /**
     * Returns the ChessClock of the black player.
     *
     * @return ChessClock of the black player
     */
    public ChessClock getBlackClock() {
        return this.blackClock;
    }

}
