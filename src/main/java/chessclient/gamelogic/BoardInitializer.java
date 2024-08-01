package chessclient.gamelogic;

import chessclient.pieces.*;

/**
 * BoardInitializer initializes the board by adding pieces to given board
 */
public class BoardInitializer {

    private final Board board;

    public BoardInitializer() {
        this.board = new Board();
    }

    /**
     * Initializing standard placement of pieces on chess board for start of the game
     *
     * @return full board
     */
    public Board initializeStandard() {
        for (int i = 0; i < Board.BOARD_WIDTH; i++) {
            Pawn blPawn = new Pawn(new int[]{1, i}, Color.BLACK);
            board.addPieceToBoard(1, i, blPawn);

            Pawn whPawn = new Pawn(new int[]{6, i}, Color.WHITE);
            board.addPieceToBoard(6, i, whPawn);
        }

        Color tempColor = Color.BLACK;
        int tempX = 0;
        //Place rooks on the board
        for (int i = 0; i < 2; i++) {
            Rook leftRook = new Rook(new int[]{tempX, 0}, tempColor);
            board.addPieceToBoard(tempX, 0, leftRook);
            Rook righRook = new Rook(new int[]{tempX, 7}, tempColor);
            board.addPieceToBoard(tempX, 7, righRook);
            tempX = 7;
            tempColor = Color.WHITE;
        }

        //Place knights on the board
        for (int i = 0; i < 2; i++) {
            Knight leftKnight = new Knight(new int[]{tempX, 1}, tempColor);
            board.addPieceToBoard(tempX, 1, leftKnight);
            Knight rightKnight = new Knight(new int[]{tempX, 6}, tempColor);
            board.addPieceToBoard(tempX, 6, rightKnight);
            tempX = 0;
            tempColor = Color.BLACK;
        }

        //Place bishops on the board
        for (int i = 0; i < 2; i++) {
            Bishop leftBishop = new Bishop(new int[]{tempX, 2}, tempColor);
            board.addPieceToBoard(tempX, 2, leftBishop);
            Bishop rightBishop = new Bishop(new int[]{tempX, 5}, tempColor);
            board.addPieceToBoard(tempX, 5, rightBishop);
            tempX = 7;
            tempColor = Color.WHITE;
        }

        //Place queens on the board
        Queen whQueen = new Queen(new int[]{tempX, 3}, tempColor);
        board.addPieceToBoard(tempX, 3, whQueen);
        tempX = 0;
        tempColor = Color.BLACK;
        Queen blQueen = new Queen(new int[]{tempX, 3}, tempColor);
        board.addPieceToBoard(tempX, 3, blQueen);

        //Place kings on the board
        King blKing = new King(new int[]{tempX, 4}, tempColor);
        board.addPieceToBoard(tempX, 4, blKing);
        tempX = 7;
        tempColor = Color.WHITE;
        King whKing = new King(new int[]{tempX, 4}, tempColor);
        board.addPieceToBoard(tempX, 4, whKing);

        for (int i = 2; i < Board.BOARD_WIDTH - 2; i++) {
            for (int j = 0; j < Board.BOARD_HEIGHT; j++) {
                board.addPieceToBoard(i, j, null);
            }
        }

        return board;
    }

    /**
     * Initializing board without pieces
     *
     * @return empty board
     */
    public Board initializeEmpty() {
        for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
            for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                board.addPieceToBoard(i, j, null);
            }
        }

        return board;
    }
}
