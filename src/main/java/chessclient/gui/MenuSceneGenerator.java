package chessclient.gui;

import chessclient.gamelogic.Board;
import chessclient.gamelogic.BoardInitializer;
import chessclient.pieces.Color;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;

/**
 * MenuSceneGenerator provides a guidepost for user. He can choose
 * Network game, local game, global statistics or quiting the game.
 * Network game send user to login
 * Local game to another choice of Player vs Player, Player vs AI or game simulation
 */
public class MenuSceneGenerator implements SceneGenerator {

    /**
     * Generating scene and handling all button clicks, setting up
     * the local game and generating new window with local game simulation
     *
     * @param stage original stage
     * @return created scene
     */
    @Override
    public Scene generateScene(Stage stage) {
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10));

        Text menuTxt = new Text("Menu");
        menuTxt.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        gridPane.add(menuTxt, 0, 0);
        GridPane.setHalignment(menuTxt, HPos.CENTER);

        Button networkGameButton = new Button("Network game");
        gridPane.add(networkGameButton, 0, 1);
        GridPane.setHalignment(networkGameButton, HPos.CENTER);

        Button localGameButton = new Button("Local game");
        gridPane.add(localGameButton, 0, 2);
        GridPane.setHalignment(localGameButton, HPos.CENTER);

        Button statsButton = new Button("Statistics");
        gridPane.add(statsButton, 0, 3);
        GridPane.setHalignment(statsButton, HPos.CENTER);

        Button quitButton = new Button("Quit");
        gridPane.add(quitButton, 0, 4);
        GridPane.setHalignment(quitButton, HPos.CENTER);

        networkGameButton.setOnAction(event -> {
            SceneGenerator sceneGenerator = new LoginSceneGenerator();
            Scene loginScene = sceneGenerator.generateScene(stage);
            stage.setScene(loginScene);
        });

        localGameButton.setOnAction(event -> {
            BorderPane borderPane = new BorderPane();

            GridPane gridPane2 = new GridPane();
            gridPane2.setAlignment(Pos.CENTER);
            gridPane2.setVgap(10);
            gridPane2.setHgap(10);

            Text localGameTxt = new Text("Local game");
            localGameTxt.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
            gridPane2.add(localGameTxt, 0, 0);
            GridPane.setHalignment(localGameTxt, HPos.CENTER);

            Button oneVsOneButton = new Button("Player vs player");
            gridPane2.add(oneVsOneButton, 0, 1);
            GridPane.setHalignment(oneVsOneButton, HPos.CENTER);
            oneVsOneButton.setOnAction(event1 -> {
                BoardInitializer initializer = new BoardInitializer();
                SceneGenerator sceneGenerator = new ChessSceneGenerator(null, Color.WHITE,
                        TypeOfGame.LOCAL_AUTO, initializer.initializeStandard());
                Scene loginScene = sceneGenerator.generateScene(stage);
                stage.setScene(loginScene);
            });

            Button loadGameButton = new Button("Load game");
            gridPane2.add(loadGameButton, 0, 2);
            GridPane.setHalignment(loadGameButton, HPos.CENTER);
            loadGameButton.setOnAction(event12 -> {
                SceneGenerator sceneGenerator = new LoadGameSceneGenerator();
                Scene loadGameScene = sceneGenerator.generateScene(stage);
                stage.setScene(loadGameScene);
            });

            Button oneVsAIButton = new Button("Player vs AI");
            gridPane2.add(oneVsAIButton, 0, 3);
            GridPane.setHalignment(oneVsAIButton, HPos.CENTER);
            oneVsAIButton.setOnAction(event13 -> {
                BoardInitializer initializer = new BoardInitializer();
                SceneGenerator sceneGenerator = new ChessSceneGenerator(null, Color.WHITE, TypeOfGame.AI,
                        initializer.initializeStandard());
                Scene loginScene = sceneGenerator.generateScene(stage);
                stage.setScene(loginScene);
            });

            Button simulationButton = new Button("Simulate game");
            gridPane2.add(simulationButton, 0, 4);
            GridPane.setHalignment(simulationButton, HPos.CENTER);
            simulationButton.setOnAction(event14 -> {
                BoardInitializer initializer = new BoardInitializer();
                Board board = initializer.initializeEmpty();
                SimulationBorderPane simulationBorderPane = new SimulationBorderPane();
                BorderPane simulation = simulationBorderPane.generatePane(board);
                Button startButton = new Button("Start");
                Button resetButton = new Button("Reset");
                ToolBar toolbar = new ToolBar(startButton, resetButton);
                toolbar.setPrefSize(20, 20);
                simulation.setTop(toolbar);
                Scene simulationScene = new Scene(simulation, 700, 500);
                Stage simulationStage = new Stage();
                InputStream imageInputStream = ChessApp.class.getResourceAsStream("/images/chessicon.png");
                Image image = new Image(imageInputStream);
                simulationStage.getIcons().add(image);
                simulationStage.setScene(simulationScene);
                simulationStage.show();
                startButton.setOnAction(event141 -> {
                    Stage simulationStage1 = (Stage) startButton.getScene().getWindow();
                    simulationStage1.close();
                    SceneGenerator sceneGenerator = new ChessSceneGenerator(null, Color.WHITE, TypeOfGame.LOCAL_MANUAL, board);
                    Scene chessScene = sceneGenerator.generateScene(stage);
                    stage.setScene(chessScene);
                });
                resetButton.setOnAction(actionEvent -> {
                    Stage simulationStage12 = (Stage) startButton.getScene().getWindow();
                    simulationStage12.close();
                    BorderPane simulation1 = simulationBorderPane.generatePane(board);
                    Scene simulationScene1 = new Scene(simulation1, 700, 500);
                    simulation1.setTop(toolbar);
                    Stage simulationStage2 = new Stage();
                    InputStream imageInputStream1 = ChessApp.class.getResourceAsStream("/images/chessicon.png");
                    Image image1 = new Image(imageInputStream1);
                    for (int i = 0; i < Board.BOARD_HEIGHT; i++) {
                        for (int j = 0; j < Board.BOARD_WIDTH; j++) {
                            board.addPieceToBoard(i, j, null);
                        }
                    }
                    simulationStage2.getIcons().add(image1);
                    simulationStage2.setScene(simulationScene1);
                    simulationStage2.show();
                });

            });

            HBox hBox = addHBox(stage);
            borderPane.setTop(hBox);
            borderPane.setCenter(gridPane2);
            Scene localGameScene = new Scene(borderPane, 700, 500);
            stage.setScene(localGameScene);

        });

        statsButton.setOnAction(event -> {
            SceneGenerator sceneGenerator = new StatisticsSceneGenerator();
            Scene statisticsScene = sceneGenerator.generateScene(stage);
            stage.setScene(statisticsScene);
        });

        quitButton.setOnAction(event -> {
            Stage stage1 = (Stage) quitButton.getScene().getWindow();
            stage1.close();
        });

        return new Scene(gridPane, 700, 500);
    }

    private HBox addHBox(Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(8, 10, 0, 10));

        Button menuButton = new Button("Back to menu");
        menuButton.setPrefSize(90, 15);

        menuButton.setOnAction(event -> {
            SceneGenerator sceneGenerator = new MenuSceneGenerator();
            Scene menuScene = sceneGenerator.generateScene(stage);
            stage.setScene(menuScene);
        });

        hbox.getChildren().addAll(menuButton);

        return hbox;
    }

}
