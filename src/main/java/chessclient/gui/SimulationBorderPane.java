package chessclient.gui;

import chessclient.gamelogic.Board;
import chessclient.pieces.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Simulation BoarderPane provides manual initialization of board by user
 * with using GUI board
 */
public class SimulationBorderPane {

    private boolean lookingForMove;
    private ImageView[][] imageViews;
    private int[] selectedPiece;
    private Rectangle[][] rectangles;

    /**
     * Generating GUI board with all chess pieces next to it, user
     * can manually add the piece to GUI board which is than connected
     * with board and the piece is added there
     *
     * @param chessBoard our board
     * @return boarderPane with 2D array of images of pieces
     */

    public BorderPane generatePane(Board chessBoard) {
        BorderPane borderPane = new BorderPane();
        GridPane board = new GridPane();
        board.getRowConstraints().add(new RowConstraints(35));
        board.getColumnConstraints().add(new ColumnConstraints(35));

        for (int i = 0; i < 8; i++) {
            board.getColumnConstraints().add(new ColumnConstraints(50));
            board.getRowConstraints().add(new RowConstraints(50));
        }
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(50);
                rectangle.setHeight(50);
                rectangle.setStrokeWidth(0);
                if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1)) {
                    rectangle.setFill(Color.rgb(241, 217, 181));
                } else {
                    rectangle.setFill(Color.rgb(181, 136, 99));
                }
                board.add(rectangle, i, j);
            }
        }

        imageViews = new ImageView[4][8];
        Image Brook = new Image("/images/brook.png");
        ImageView brook = new ImageView(Brook);
        imageViews[0][0] = brook;
        Image Brook2 = new Image("/images/brook.png");
        ImageView brook2 = new ImageView(Brook2);
        imageViews[0][1] = brook2;
        Image Bknight = new Image("/images/bknight.png");
        ImageView bknight = new ImageView(Bknight);
        imageViews[0][2] = bknight;
        Image Bknight2 = new Image("/images/bknight.png");
        ImageView bknight2 = new ImageView(Bknight2);
        imageViews[0][3] = bknight2;
        Image Bbishop = new Image("/images/bbishop.png");
        ImageView bbishop = new ImageView(Bbishop);
        imageViews[0][4] = bbishop;
        Image Bbishop2 = new Image("/images/bbishop.png");
        ImageView bbishop2 = new ImageView(Bbishop2);
        imageViews[0][5] = bbishop2;
        Image Bking = new Image("/images/bking.png");
        ImageView bking = new ImageView(Bking);
        imageViews[0][6] = bking;
        Image Bqueen = new Image("/images/bqueen.png");
        ImageView bqueen = new ImageView(Bqueen);
        imageViews[0][7] = bqueen;
        Image Bpawn = new Image("/images/bpawn.png");
        ImageView bpawn = new ImageView(Bpawn);
        imageViews[1][0] = bpawn;
        Image Bpawn2 = new Image("/images/bpawn.png");
        ImageView bpawn2 = new ImageView(Bpawn2);
        imageViews[1][1] = bpawn2;
        Image Bpawn3 = new Image("/images/bpawn.png");
        ImageView bpawn3 = new ImageView(Bpawn3);
        imageViews[1][2] = bpawn3;
        Image Bpawn4 = new Image("/images/bpawn.png");
        ImageView bpawn4 = new ImageView(Bpawn4);
        imageViews[1][3] = bpawn4;
        Image Bpawn5 = new Image("/images/bpawn.png");
        ImageView bpawn5 = new ImageView(Bpawn5);
        imageViews[1][4] = bpawn5;
        Image Bpawn6 = new Image("/images/bpawn.png");
        ImageView bpawn6 = new ImageView(Bpawn6);
        imageViews[1][5] = bpawn6;
        Image Bpawn7 = new Image("/images/bpawn.png");
        ImageView bpawn7 = new ImageView(Bpawn7);
        imageViews[1][6] = bpawn7;
        Image Bpawn8 = new Image("/images/bpawn.png");
        ImageView bpawn8 = new ImageView(Bpawn8);
        imageViews[1][7] = bpawn8;

        Image Wrook = new Image("/images/wrook.png");
        ImageView wrook = new ImageView(Wrook);
        imageViews[2][0] = wrook;
        Image Wrook2 = new Image("/images/wrook.png");
        ImageView wrook2 = new ImageView(Wrook2);
        imageViews[2][1] = wrook2;
        Image Wknight = new Image("/images/wknight.png");
        ImageView wknight = new ImageView(Wknight);
        imageViews[2][2] = wknight;
        Image Wknight2 = new Image("/images/wknight.png");
        ImageView wknight2 = new ImageView(Wknight2);
        imageViews[2][3] = wknight2;
        Image Wbishop = new Image("/images/wbishop.png");
        ImageView wbishop = new ImageView(Wbishop);
        imageViews[2][4] = wbishop;
        Image Wbishop2 = new Image("/images/wbishop.png");
        ImageView wbishop2 = new ImageView(Wbishop2);
        imageViews[2][5] = wbishop2;
        Image Wking = new Image("/images/wking.png");
        ImageView wking = new ImageView(Wking);
        imageViews[2][6] = wking;
        Image Wqueen = new Image("/images/wqueen.png");
        ImageView wqueen = new ImageView(Wqueen);
        imageViews[2][7] = wqueen;
        Image Wpawn = new Image("/images/wpawn.png");
        ImageView wpawn = new ImageView(Wpawn);
        imageViews[3][0] = wpawn;
        Image Wpawn2 = new Image("/images/wpawn.png");
        ImageView wpawn2 = new ImageView(Wpawn2);
        imageViews[3][1] = wpawn2;
        Image Wpawn3 = new Image("/images/wpawn.png");
        ImageView wpawn3 = new ImageView(Wpawn3);
        imageViews[3][2] = wpawn3;
        Image Wpawn4 = new Image("/images/wpawn.png");
        ImageView wpawn4 = new ImageView(Wpawn4);
        imageViews[3][3] = wpawn4;
        Image Wpawn5 = new Image("/images/wpawn.png");
        ImageView wpawn5 = new ImageView(Wpawn5);
        imageViews[3][4] = wpawn5;
        Image Wpawn6 = new Image("/images/wpawn.png");
        ImageView wpawn6 = new ImageView(Wpawn6);
        imageViews[3][5] = wpawn6;
        Image Wpawn7 = new Image("/images/wpawn.png");
        ImageView wpawn7 = new ImageView(Wpawn7);
        imageViews[3][6] = wpawn7;
        Image Wpawn8 = new Image("/images/wpawn.png");
        ImageView wpawn8 = new ImageView(Wpawn8);
        imageViews[3][7] = wpawn8;

        rectangles = new Rectangle[4][8];
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 8; y++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(50);
                rectangle.setHeight(50);
                rectangles[x][y] = rectangle;
                rectangle.setFill(Color.TRANSPARENT);
                board.add(rectangle, x + 10, y + 1);
                imageViews[x][y].setFitWidth(47);
                imageViews[x][y].setFitHeight(47);
                board.add(imageViews[x][y], x + 10, y + 1);
            }
        }
        borderPane.setCenter(board);

        lookingForMove = false;
        board.setOnMousePressed(event -> {
            if (clickIsOnBoard(event.getX(), event.getY())) {
                if (lookingForMove) {
                    int x = ((int) (event.getX() - 35)) / 50;
                    int y = ((int) (event.getY() - 35)) / 50;
                    board.setRowIndex(imageViews[selectedPiece[0]][selectedPiece[1]], y + 1);
                    board.setColumnIndex(imageViews[selectedPiece[0]][selectedPiece[1]], x + 1);
                    addToChessBoard(chessBoard, selectedPiece[0], selectedPiece[1], x, y);
                    rectangles[selectedPiece[0]][selectedPiece[1]].setStroke(Color.TRANSPARENT);
                    lookingForMove = false;
                }
            }
            if (clickIsOnPieces(event.getX(), event.getY())) {
                int x = ((int) (event.getX() - 435)) / 50;
                int y = ((int) (event.getY() - 35)) / 50;
                selectedPiece = new int[2];
                selectedPiece[0] = x;
                selectedPiece[1] = y;
                rectangles[x][y].setStroke(Color.GRAY);
                lookingForMove = true;
            }

        });

        return borderPane;

    }

    private boolean clickIsOnBoard(double x, double y) {
        return (!(x < 35) && !(y < 35)) && (!(x > 435) && !(y > 435));
    }

    private boolean clickIsOnPieces(double x, double y) {
        return (!(x < 435) && !(y < 35)) && (!(x > 635) && !(y > 435));
    }

    private void addToChessBoard(Board chessboard, int x, int y, int posX, int posY) {
        cz.cvut.fel.pjv.chessclient.pieces.Color color;
        if (x == 0 || x == 1) {
            color = cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK;
        } else {
            color = cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE;
        }
        int[] position = {posY, posX};
        Piece piece;

        if (x == 1 || x == 3) {
            piece = new Pawn(position, color);
        } else if (y == 0 || y == 1) {
            piece = new Rook(position, color);
        } else if (y == 2 || y == 3) {
            piece = new Knight(position, color);
        } else if (y == 4 || y == 5) {
            piece = new Bishop(position, color);
        } else if (y == 6) {
            piece = new King(position, color);
        } else {
            piece = new Queen(position, color);
        }
        chessboard.addPieceToBoard(posY, posX, piece);

    }
    
}
