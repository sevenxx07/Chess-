package chessclient.gamelogic;

import chessclient.pieces.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Board class stores the current state of the chess game board, as well as
 * Lists of the Pieces of the two players. WHITE player ALWAYS STARTS on the bottom
 * of the board and BLACK starts on the top of the board.
 */
public class Board {

    public static final int BOARD_WIDTH = 8;
    public static final int BOARD_HEIGHT = 8;

    private final Piece[][] currBoard;       /* Stores the current state of the game board */
    private final List<Piece> whPieces;       /* List of the white chess pieces */
    private final List<Piece> blPieces;      /* List of the black chess pieces */

    /**
     * Creates an empty Board which can be filled with addPieceToBoard method of this class.
     */
    public Board() {
        this.currBoard = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
        this.whPieces = new ArrayList<>();
        this.blPieces = new ArrayList<>();
    }

    /**
     * Creates a Board object encapsulating the two dimensional array of Pieces given to it.
     *
     * @param board two dimensional array of Pieces which represents a board
     */
    public Board(Piece[][] board) {
        this.currBoard = board;
        this.whPieces = new ArrayList<>();
        this.blPieces = new ArrayList<>();
        this.fillListsWithPieces();
    }

    /**
     * Creates a copy of the Board given as an argument
     *
     * @param original Board which is to be copied
     */
    public Board(Board original) {
        this.currBoard = new Piece[BOARD_HEIGHT][BOARD_WIDTH];
        this.whPieces = new ArrayList<>();
        this.blPieces = new ArrayList<>();

        for (int i = 0; i < BOARD_WIDTH; i++) {
            for (int j = 0; j < BOARD_HEIGHT; j++) {
                if (original.currBoard[j][i] == null) {
                    this.currBoard[j][i] = null;
                } else {
                    Piece p = original.currBoard[j][i];
                    this.currBoard[j][i] = p.copyPiece();
                    if (p.getColor().equals(Color.BLACK)) {
                        blPieces.add(this.currBoard[j][i]);
                    } else {
                        whPieces.add(this.currBoard[j][i]);
                    }
                }
            }
        }
    }

    /**
     * Updates the current board according to the move played by one of the players
     *
     * @param move Move played by the player
     */
    public void playMove(Move move) {
        int[] from = move.getFrom();
        int[] to = move.getTo();
        SpecialMove specialMove = move.getSpecialMove();
        Piece movedPiece = this.currBoard[from[0]][from[1]];
        Piece capturedPiece = this.currBoard[to[0]][to[1]];

        if (specialMove != null && specialMove.equals(SpecialMove.EN_PASSANT)) {
            //Take care of en passant special behavior
            capturedPiece = this.currBoard[from[0]][to[1]];
            this.currBoard[from[0]][to[1]] = null;
        } else if (specialMove != null && specialMove.equals(SpecialMove.CASTLE)) {
            //move king onto its position
            int rookTo = to[1] > from[1] ? 5 : 3;
            int rookFrom = to[1] > from[1] ? 7 : 0;
            Rook movedRook = (Rook) this.currBoard[from[0]][rookFrom];

            this.currBoard[from[0]][rookFrom] = null;
            this.currBoard[to[0]][rookTo] = movedRook;
            movedRook.updatePosition(new int[]{from[0], rookTo});
        } else if (specialMove != null && specialMove.equals(SpecialMove.PROMOTION)) {
            //Promote the pawn into the new piece
            if (movedPiece.getColor().equals(Color.BLACK)) {
                this.blPieces.remove(movedPiece);
                movedPiece = resolvePromotion(move, Color.BLACK);
            } else {
                this.whPieces.remove(movedPiece);
                movedPiece = resolvePromotion(move, Color.WHITE);
            }
            this.addPieceToBoard(to[0], to[1], movedPiece);
        }

        //Remove captured piece from its color's array list
        if (capturedPiece != null) {
            if (capturedPiece.getColor().equals(Color.BLACK)) {
                this.blPieces.remove(capturedPiece);
            } else {
                this.whPieces.remove(capturedPiece);
            }
        }

        this.updatePawns(); //update the pawns on the board which can't be taken by en passant after this turn

        this.currBoard[to[0]][to[1]] = movedPiece;
        this.currBoard[from[0]][from[1]] = null;
        if (movedPiece != null) {
            movedPiece.updatePosition(to);
        }
    }

    /**
     * Checks if square with given coordinates is a valid square on the game board
     *
     * @param x row index of the square
     * @param y column index of the square
     * @return true if the two indices are within bounds of the game board, else false
     */
    public static boolean isValidSquare(int x, int y) {
        return (x >= 0 && x <= 7 && y >= 0 && y <= 7);
    }

    /**
     * Places the given Piece onto its position on the game board, and adds it into
     * a List of Pieces of the same color
     *
     * @param x index of the row of the placed Piece
     * @param y index of the column of the placed Piece
     * @param p Piece added onto the Board
     */
    public void addPieceToBoard(int x, int y, Piece p) {
        this.currBoard[x][y] = p;
        if (p != null) {
            if (p.getColor().equals(Color.BLACK)) {
                this.blPieces.add(p);
            } else {
                this.whPieces.add(p);
            }
        }
    }

    private void updatePawns() {
        List<Piece> allPieces = new ArrayList<>();
        allPieces.addAll(this.blPieces);
        allPieces.addAll(this.whPieces);
        for (Piece p : allPieces) {
            if (p instanceof Pawn) {
                ((Pawn) p).updateMovedTwo();
            }
        }
    }

    private void fillListsWithPieces() {
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                if (this.currBoard[i][j] != null) {
                    if (this.currBoard[i][j].getColor().equals(Color.BLACK)) {
                        this.blPieces.add(this.currBoard[i][j]);
                    } else {
                        this.whPieces.add(this.currBoard[i][j]);
                    }
                }
            }
        }
    }

    private Piece resolvePromotion(Move move, Color color) {
        int[] initPos = move.getTo();
        String promotionId = move.getPromotionIdentifier();
        switch (promotionId) {
            case "bishop":
                return new Bishop(initPos, color);
            case "knight":
                return new Knight(initPos, color);
            case "rook":
                return new Rook(initPos, color);
            default:
                return new Queen(initPos, color);
        }
    }

    /**
     * Returns pieces of the white player
     *
     * @return List<Pieces> containing pieces of the white player
     */
    public List<Piece> getWhPieces() {
        return whPieces;
    }

    /**
     * Returns pieces of the black player
     *
     * @return List<Pieces> containing pieces of the black player
     */
    public List<Piece> getBlPieces() {
        return blPieces;
    }

    /**
     * Returns piece on the current board at given coordinates.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @return piece on given coordinates
     */
    public Piece getPiece(int x, int y) {
        return this.currBoard[x][y];
    }

    /**
     * Returns two dimensional array of Pieces which represents the current state
     * of the game board
     *
     * @return Piece[][] representing the current board
     */
    public Piece[][] getCurrentBoard() {
        return this.currBoard;
    }

}
