package chessclient.gui;

import chessclient.gamelogic.BoardInitializer;
import chessclient.network.ChessClient;
import chessclient.pieces.Color;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * QueueSceneGenerator class generates the Scene in which the timer with time
 * spent in queue is shown, and takes care of transition from waiting in queue
 * to playing the game.
 */
public class QueueSceneGenerator implements SceneGenerator {

    private final ChessClient connection;
    private Stage stage;

    /**
     * Default constructor for QueueSceneGenerator
     *
     * @param connection ChessClient through which the logged in user is
     *                   connected to the server
     */
    public QueueSceneGenerator(ChessClient connection) {
        this.connection = connection;
    }

    /**
     * Generates the Scene in which the timer with time spent in queue is shown
     *
     * @param stage Stage onto which the Scene is drawn
     * @return drawn Scene
     */
    @Override
    public Scene generateScene(Stage stage) {
        this.stage = stage;
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);

        this.addIconToGrid(gridPane);

        SimpleStringProperty timer = new SimpleStringProperty();
        addTextToGrid(gridPane, "Waiting for a game...", 1);
        Text queueTimer = addTextToGrid(gridPane, "", 2);
        queueTimer.textProperty().bind(timer);

        QueueUpdater updater = new QueueUpdater(this.connection, timer, this);
        addCancelButtonToGrid(gridPane, connection, updater);

        borderPane.setCenter(gridPane);

        stage.setOnCloseRequest((windowEvent) -> {
            updater.stopUpdater();
            connection.stopConnection();
            System.exit(0);
        });

        updater.start();

        return new Scene(borderPane, 700, 500);
    }

    /* Adds the chess icon to the Scene */
    private void addIconToGrid(GridPane gridPane) {
        InputStream imageInputStream = getClass().getResourceAsStream("/images/chessicon.png");
        ImageView pic = new ImageView(new Image(imageInputStream));
        pic.setFitHeight(40);
        pic.setFitWidth(40);
        gridPane.add(pic, 0, 0);
        GridPane.setHalignment(pic, HPos.CENTER);
    }

    /* Adds the informative text to the Scene */
    private Text addTextToGrid(GridPane gridPane, String textString, int row) {
        Text text = new Text(textString);
        text.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        gridPane.add(text, 0, row);
        GridPane.setHalignment(text, HPos.CENTER);
        return text;
    }

    /* Adds the cancel button to the scene which allows the player to cancel
     * searching for game, and close the socket connection */
    private void addCancelButtonToGrid(GridPane gridPane, ChessClient connection, QueueUpdater updater) {
        Button cancel = new Button("Cancel");
        cancel.setOnMouseClicked(event -> {
            updater.stopUpdater();
            connection.stopConnection();
            this.stage.setOnCloseRequest((windowEvent) -> System.exit(0));
            SceneGenerator sceneGenerator = new MenuSceneGenerator();
            Scene signScene = sceneGenerator.generateScene(this.stage);
            this.stage.setScene(signScene);
        });
        gridPane.add(cancel, 0, 3);
        GridPane.setHalignment(cancel, HPos.CENTER);
    }

    /**
     * Creates a Scene with the chess match itself via ChessSceneGenerator, and
     * renders it to the current Stage.
     */
    public void startGame(String colorStr) {
        Platform.runLater(() -> {
            BoardInitializer initializer = new BoardInitializer();
            Color color = colorStr.equals("white") ? Color.WHITE : Color.BLACK;
            SceneGenerator sceneGenerator = new ChessSceneGenerator(connection, color, TypeOfGame.NETWORK,
                    initializer.initializeStandard());
            stage.setOnCloseRequest((windowEvent) -> {
                connection.stopConnection();
                System.exit(0);
            });
            Scene chessScene = sceneGenerator.generateScene(this.stage);
            stage.setScene(chessScene);
        });
    }
}
