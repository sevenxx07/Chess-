package cz.cvut.fel.pjv.chessclient.gui;

import cz.cvut.fel.pjv.chessclient.gamelogic.*;
import cz.cvut.fel.pjv.chessclient.network.ChessClient;
import cz.cvut.fel.pjv.chessclient.pieces.Piece;
import cz.cvut.fel.pjv.chessclient.players.GUIPlayer;
import cz.cvut.fel.pjv.chessclient.players.NetworkPlayer;
import cz.cvut.fel.pjv.chessclient.players.Player;
import cz.cvut.fel.pjv.chessclient.players.RandomAI;
import cz.cvut.fel.pjv.chessclient.utilities.GameSave;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * ChessSceneGenerator handles whole game for GUI - starts the whole game,
 * initializes players, creates GUI board and takes care of all mouse events
 * on board. With this passes moves to players and redraws the GUI board
 * Draws the end of the game and server disconnection when it is needed
 */

public class ChessSceneGenerator implements SceneGenerator {

    private BorderPane borderPane;
    private Stage originalStage;
    private GridPane board;
    private ToolBar toolbar;
    private VBox timers;
    private List<Move> moveList;             /*for possible moves in current move for selected piece*/
    private ImageView[][] imageViews;
    private Rectangle[][] rectangles;        /*64 rectangles creating GUI board*/
    private int[] selectedPiece;             /*piece that is going to be moved*/

    private boolean lookingForMove;          /*true after selecting piece*/
    private final Board chessBoard;
    private BoardFiller boardFiller;

    private Player whitePlayer;
    private Player blackPlayer;
    private Game game;
    private final ChessClient client;
    private cz.cvut.fel.pjv.chessclient.pieces.Color color;
    private final TypeOfGame type;
    private boolean whiteOnTurn;             /*for switching sites who is on turn*/
    private boolean networkPlayerIsPlaying;  /*for waiting for networks players move*/

    /**
     * Constructor sets the game rules, initializes players and Game
     *
     * @param client      already initialized client we are using for the game
     * @param colorPlayer color of gui player
     * @param type        type of the game - enum
     * @param board       already initialized board
     */
    public ChessSceneGenerator(ChessClient client, cz.cvut.fel.pjv.chessclient.pieces.Color colorPlayer, TypeOfGame type, Board board) {
        this.client = client;
        this.color = colorPlayer;      /*always white in local game and with AI*/
        this.type = type;
        this.chessBoard = board;
        this.whiteOnTurn = colorPlayer.equals(cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE);
        gameSettings();
    }

    public ChessSceneGenerator(GameSave save, TypeOfGame typeOfGame) {
        this(null, save.turnColor(), typeOfGame, save.deserializeBoard());
        if (typeOfGame.equals(TypeOfGame.AI) && save.turnColor().equals(cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK)) {
            this.color = cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE;
        }
        this.game.setClocks(save.getWhiteClock(), save.getBlackClock());
    }

    private void gameSettings() {
        if (type.equals(TypeOfGame.AI)) {
            this.whitePlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE);
            this.blackPlayer = new RandomAI(chessBoard, cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK);
            this.game = new Game(whitePlayer, blackPlayer, this, chessBoard, this.whiteOnTurn);
            networkPlayerIsPlaying = false;
        }
        if (type.equals(TypeOfGame.LOCAL_AUTO)) {
            this.whitePlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE);
            this.blackPlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK);
            this.game = new Game(whitePlayer, blackPlayer, this, chessBoard, this.whiteOnTurn);
            networkPlayerIsPlaying = false;
        }
        if (type.equals(TypeOfGame.LOCAL_MANUAL)) {
            this.whitePlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE);
            this.blackPlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK);
            this.game = new Game(whitePlayer, blackPlayer, this, chessBoard, this.whiteOnTurn);
            networkPlayerIsPlaying = false;

        }
        if (type.equals(TypeOfGame.NETWORK)) {
            if (this.color.equals(cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK)) {
                this.whitePlayer = new NetworkPlayer(this.client, cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE);
                this.blackPlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK);
                networkPlayerIsPlaying = true;
            } else {
                this.whitePlayer = new GUIPlayer(cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE);
                this.blackPlayer = new NetworkPlayer(this.client, cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK);
                networkPlayerIsPlaying = false;
            }
            this.whiteOnTurn = true;
            this.game = new Game(whitePlayer, blackPlayer, this, chessBoard, true);
        }
    }

    /**
     * Generating chess scene, filling board gridPane according to given board
     * (manually or automatically initialized)
     * Also starting the infinite loop of Game and starting connection with server
     *
     * @param stage original stage
     * @return scene of chess game
     */

    @Override
    public Scene generateScene(Stage stage) {
        originalStage = new Stage();
        originalStage = stage;
        borderPane = new BorderPane();
        initToolbar(stage);
        initBlankBoard();
        initClock();
        borderPane.setTop(toolbar);
        borderPane.setRight(timers);
        boardFiller = new BoardFiller(chessBoard);
        imageViews = new ImageView[8][8];
        imageViews = boardFiller.boardFill();
        loadImageView();
        borderPane.setCenter(board);

        Scene chessScene = new Scene(borderPane, 700, 500);
        stage.setScene(chessScene);

        originalStage.setOnCloseRequest((windowEvent) -> {
            if (client != null) {
                client.stopConnection();
            }
            game.stopGame();
            System.exit(0);
        });

        game.start();
        lookingForMove = false;

        return chessScene;
    }

    private void initToolbar(Stage stage) {
        Button menuButton = new Button("Back to menu");
        menuButton.setOnAction(event -> {
            if (client != null) {
                client.stopConnection();
            }
            game.stopGame();
            SceneGenerator sceneGenerator = new MenuSceneGenerator();
            Scene menuScene = sceneGenerator.generateScene(stage);
            stage.setScene(menuScene);
        });
        Button saveButton = new Button("Save game");
        saveButton.setOnAction(actionEvent -> game.saveGame());
        toolbar = new ToolBar(menuButton, saveButton);
        toolbar.setPrefSize(20, 20);
    }

    private void initClock() {
        Text whiteClock = new Text();
        Text blackClock = new Text();
        StringProperty whiteClockStr = new SimpleStringProperty();
        StringProperty blackClockStr = new SimpleStringProperty();
        game.setClockStringProperties(whiteClockStr, blackClockStr);
        whiteClock.textProperty().bind(whiteClockStr);
        blackClock.textProperty().bind(blackClockStr);

        timers = new VBox(whiteClock, blackClock);
        timers.setPadding(new Insets(35));
    }

    private void initBlankBoard() {
        board = new GridPane();

        board.getRowConstraints().add(new RowConstraints(35));
        board.getColumnConstraints().add(new ColumnConstraints(35));

        for (int i = 0; i < 8; i++) {
            board.getColumnConstraints().add(new ColumnConstraints(50));
            board.getRowConstraints().add(new RowConstraints(50));
        }
        rectangles = new Rectangle[8][8];
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                Rectangle rectangle = new Rectangle();
                rectangle.setWidth(50);
                rectangle.setHeight(50);
                rectangle.setStrokeWidth(1);
                rectangle.setStroke(Color.GRAY);
                rectangles[i - 1][j - 1] = rectangle;
                if ((j % 2 == 0 && i % 2 == 0) || (j % 2 == 1 && i % 2 == 1)) {
                    rectangle.setFill(Color.rgb(241, 217, 181));
                } else {
                    rectangle.setFill(Color.rgb(181, 136, 99));
                }
                board.add(rectangle, i, j);

            }

        }
        board.setOnMousePressed(new EventHandler<>() {
            /**
             * handling mouse events on board, creating move and sending him
             * to player according to all conditions for certain game type
             */
            @Override
            public void handle(MouseEvent event) {
                if (clickIsValid(event.getX(), event.getY())) {
                    int x = ((int) (event.getX() - 35)) / 50;
                    int y = ((int) (event.getY() - 35)) / 50;
                    if (lookingForMove) {
                        Move myMove = new Move(new int[]{selectedPiece[1], selectedPiece[0]}, new int[]{y, x});
                        int myMoveIndex = -1;
                        if ((myMoveIndex = getIndexOfMyMove(myMove, moveList)) != -1) {
                            if (whiteOnTurn && color == cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE) {
                                ((GUIPlayer) whitePlayer).setTurn(moveList.get(myMoveIndex));
                            }
                            if (!whiteOnTurn && color == cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK) {
                                ((GUIPlayer) blackPlayer).setTurn(moveList.get(myMoveIndex));
                            }
                        }
                        rectangles[selectedPiece[0]][selectedPiece[1]].setStroke(Color.GRAY);
                        imageViews[selectedPiece[0]][selectedPiece[1]].setOpacity(1);
                        for (Move m : moveList) {
                            int[] to = m.getTo();
                            if ((to[0] % 2 == 0 && to[1] % 2 == 0) || (to[0] % 2 == 1 && to[1] % 2 == 1)) {
                                rectangles[to[1]][to[0]].setFill(Color.rgb(241, 217, 181));
                            } else {
                                rectangles[to[1]][to[0]].setFill(Color.rgb(181, 136, 99));
                            }
                        }
                        moveList.clear();
                        lookingForMove = false;
                    } else {
                        Piece piece = chessBoard.getPiece(y, x);
                        if (piece != null && ((whiteOnTurn && piece.getColor() == cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE)
                                || (!whiteOnTurn && piece.getColor() == cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK))
                                && !networkPlayerIsPlaying) {
                            selectedPiece = new int[2];
                            selectedPiece[0] = x;
                            selectedPiece[1] = y;
                            rectangles[x][y].setStroke(Color.BLACK);
                            imageViews[x][y].setOpacity(0.6);
                            moveList = piece.getValidMoves(chessBoard);

                            for (Move m : moveList) {
                                int[] to = m.getTo();
                                rectangles[to[1]][to[0]].setFill(Color.rgb(189, 217, 97));
                            }
                            lookingForMove = true;
                        }
                    }

                }
            }
        });
    }

    /**
     * Creates a new Stage on which the player can choose which promotion his Pawn will take.
     *
     * @param player choosing GUIPlayer
     */
    public void choosePromotion(GUIPlayer player) {
        Platform.runLater(() -> {
            ChoosePromotionStage promotionStage = new ChoosePromotionStage(player);
            promotionStage.choosePromotion();
        });
    }

    private int getIndexOfMyMove(Move myMove, List<Move> possibleMoves) {
        for (int i = 0; i < possibleMoves.size(); i++) {
            Move temp = possibleMoves.get(i);
            if (Arrays.equals(myMove.getFrom(), temp.getFrom())
                    && Arrays.equals(myMove.getTo(), temp.getTo())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Redrawing GUI board when Move m is played, switching enabling the pieces
     * according to who is on turn
     *
     * @param m move done by player
     */
    public void redrawBoard(Move m) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                int[] from = m.getFrom();
                int[] to = m.getTo();
                if (chessBoard.getPiece(to[0], to[1]) == null) {
                    board.setRowIndex(imageViews[from[1]][from[0]], to[0] + 1);
                    board.setColumnIndex(imageViews[from[1]][from[0]], to[1] + 1);
                    imageViews = boardFiller.boardFill();
                } else {
                    board.getChildren().clear();
                    initBlankBoard();
                    imageViews = boardFiller.boardFill();
                    loadImageView();
                    borderPane.setCenter(board);

                }
                if (whiteOnTurn) {
                    whiteOnTurn = false;
                    if (type == TypeOfGame.LOCAL_AUTO || type == TypeOfGame.LOCAL_MANUAL || type == TypeOfGame.NETWORK) {
                        color = cz.cvut.fel.pjv.chessclient.pieces.Color.BLACK;
                    }
                } else {
                    whiteOnTurn = true;
                    if (type == TypeOfGame.LOCAL_AUTO || type == TypeOfGame.LOCAL_MANUAL || type == TypeOfGame.NETWORK) {
                        color = cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE;
                    }
                }
                if (type == TypeOfGame.NETWORK) {
                    if (networkPlayerIsPlaying) {
                        networkPlayerIsPlaying = false;
                    } else {
                        networkPlayerIsPlaying = true;
                    }
                }
            }
        });

    }

    /**
     * Drawing who is the winner in new window, then returning back to Menu Scene
     * or in case of AI or LOCAL_MANUAL game giving the opportunity or revenge
     *
     * @param winner of the game
     */
    public void endGame(Player winner) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (client != null) {
                    client.stopConnection();
                }
                game.stopGame();
                BorderPane endGame = new BorderPane();
                endGame.setPadding(new Insets(10));
                Scene endofGame = new Scene(endGame, 300, 100);
                InputStream imageInputStream = ChessApp.class.getResourceAsStream("/images/chessicon.png");
                Image image = new Image(imageInputStream);

                String s;
                if (winner == null) {
                    s = "Draw!";
                } else if (winner.getColor() == cz.cvut.fel.pjv.chessclient.pieces.Color.WHITE) {
                    s = "White won!";
                } else {
                    s = "Black won!";
                }

                Text text = new Text(s);
                text.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 14));
                endGame.setCenter(text);

                HBox hButton = new HBox();
                hButton.setSpacing(5);
                Button menuButton = new Button("Back to menu");
                hButton.getChildren().add(menuButton);
                if (type == TypeOfGame.AI || type == TypeOfGame.LOCAL_AUTO) {
                    Button revenge = new Button("Revenge");
                    hButton.getChildren().add(revenge);
                    revenge.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            Stage endOfGame = (Stage) revenge.getScene().getWindow();
                            endOfGame.close();
                            BoardInitializer initializer = new BoardInitializer();
                            SceneGenerator sceneGenerator = new ChessSceneGenerator(client, color, type, initializer.initializeStandard());
                            Scene chessScene = sceneGenerator.generateScene(originalStage);
                            originalStage.setScene(chessScene);
                        }
                    });
                }
                hButton.setAlignment(Pos.CENTER_RIGHT);

                endGame.setBottom(hButton);
                Stage endOfGame = new Stage();
                menuButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        Stage endOfGame = (Stage) menuButton.getScene().getWindow();
                        endOfGame.close();
                        SceneGenerator sceneGenerator = new MenuSceneGenerator();
                        Scene menuScene = sceneGenerator.generateScene(originalStage);
                        originalStage.setScene(menuScene);
                    }
                });

                endOfGame.getIcons().add(image);
                endOfGame.setTitle("End of game");
                endOfGame.setScene(endofGame);
                endOfGame.show();

            }
        });
    }


    /**
     * Drawing message that network crashed in new window
     * Returns user back to menu
     */
    public void drawNetworkCrashed() {
        if (client != null) {
            client.stopConnection();
        }
        game.stopGame();
        BorderPane networkCrash = new BorderPane();
        networkCrash.setPadding(new Insets(10));
        Scene networkCrashed = new Scene(networkCrash, 270, 100);
        InputStream imageInputStream = ChessApp.class.getResourceAsStream("/images/chessicon.png");
        Image image = new Image(imageInputStream);

        Text text = new Text("Network crashed! Go back to menu");
        text.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 14));
        networkCrash.setCenter(text);

        HBox hButton = new HBox();
        Button exit = new Button("Exit");
        hButton.getChildren().add(exit);
        hButton.setAlignment(Pos.CENTER_RIGHT);

        networkCrash.setBottom(hButton);
        Stage netCrashed = new Stage();
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage netCrashed = (Stage) exit.getScene().getWindow();
                netCrashed.close();
                SceneGenerator sceneGenerator = new MenuSceneGenerator();
                Scene menuScene = sceneGenerator.generateScene(originalStage);
                originalStage.setScene(menuScene);
            }
        });

        netCrashed.getIcons().add(image);
        netCrashed.setTitle("Server problems");
        netCrashed.setScene(networkCrashed);
        netCrashed.show();

    }

    private boolean clickIsValid(double x, double y) {
        return (!(x < 35) && !(y < 35)) && (!(x > 435) && !(y > 435));
    }

    private void loadImageView() {
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                board.add(imageViews[x][y], x + 1, y + 1);
            }
        }
    }


}
