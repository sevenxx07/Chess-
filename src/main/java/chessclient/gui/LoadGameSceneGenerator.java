package chessclient.gui;

import chessclient.utilities.SaveManager;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;

/**
 * LoadGameSceneGenerator is a class which creates a scene, from which the user can
 * load, and delete saved games.
 */
public class LoadGameSceneGenerator implements SceneGenerator {

    @Override
    public Scene generateScene(Stage stage) {
        BorderPane borderPane = new BorderPane();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        HBox hBox = addHBox(stage);
        borderPane.setTop(hBox);
        scrollPane.setContent(gridPane);
        borderPane.setCenter(scrollPane);

        this.generateSavesGUI(gridPane, stage);

        return new Scene(borderPane, 700, 500);
    }

    /* Adds either a table with saved games, or text that there are no saved games into the given GridPane */
    private void generateSavesGUI(GridPane grid, Stage stage) {
        SaveManager saveManager = new SaveManager();
        File[] saves = saveManager.getSaves();

        if (saves.length == 0) {
            grid.add(this.getNoSavesText(), 0, 0);
            return;
        }

        grid.setStyle("-fx-grid-lines-visible: true");
        for (int i = 0; i < saves.length; i++) {
            GridPane row = this.getColumnRow(saves[i], grid, stage);
            grid.add(row, 0, i);
        }
    }

    /* Creates one row in the given GridPane which represents GUI for a particular saved game */
    private GridPane getColumnRow(File save, GridPane grid, Stage stage) {
        GridPane row = new GridPane();
        this.addPercentColumnConstraint(row, 60);
        this.addPercentColumnConstraint(row, 20);
        this.addPercentColumnConstraint(row, 20);

        Text saveName = new Text(save.getName());
        saveName.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        Button delete = new Button("Delete");
        delete.setOnMouseClicked((mouseEvent -> {
            save.delete();
            grid.getChildren().remove(row);
            if (grid.getChildren().size() == 1) {
                grid.add(this.getNoSavesText(), 0, 0);
                grid.setStyle("-fx-grid-lines-visible: false");
            }
        }));
        Button load = new Button("Load");
        load.setOnMouseClicked((mouseEvent -> {
            this.loadGameDialog(grid, stage, save.getName());
        }));

        row.add(saveName, 0, 0);
        GridPane.setMargin(saveName, new Insets(7, 5, 7, 10));
        GridPane.setHalignment(saveName, HPos.CENTER);
        row.add(delete, 1, 0);
        GridPane.setMargin(delete, new Insets(7, 5, 7, 10));
        row.add(load, 2, 0);
        GridPane.setMargin(load, new Insets(7, 5, 7, 10));
        GridPane.setHalignment(load, HPos.CENTER);
        row.setStyle("-fx-grid-lines-visible: true");

        return row;
    }

    /* Clears GridPane with saved games, and renders a dialog which lets the user choose
     * whether to load the game as "player vs player", or "player vs ai" */
    private void loadGameDialog(GridPane grid, Stage stage, String gameName) {
        grid.getChildren().removeAll(grid.getChildren());
        grid.setStyle("-fx-grid-lines-visible: false");

        Text info = new Text("Load game as PVP or PVE?");
        info.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        Button pvp = new Button("PVP");
        pvp.setOnMouseClicked((mouseEvent -> {
            SaveManager sm = new SaveManager();
            ChessSceneGenerator generator = sm.loadSavedGame(gameName, TypeOfGame.LOCAL_MANUAL);
            stage.setScene(generator.generateScene(stage));
        }));
        Button pve = new Button("PVE");
        pve.setOnMouseClicked((mouseEvent -> {
            SaveManager sm = new SaveManager();
            ChessSceneGenerator generator = sm.loadSavedGame(gameName, TypeOfGame.AI);
            stage.setScene(generator.generateScene(stage));
        }));

        GridPane buttons = new GridPane();
        this.addPercentColumnConstraint(buttons, 50);
        this.addPercentColumnConstraint(buttons, 50);
        buttons.add(pvp, 0, 0);
        buttons.add(pve, 1, 0);
        GridPane.setMargin(pvp, new Insets(20, 10, 0, 10));
        GridPane.setMargin(pve, new Insets(20, 10, 0, 10));
        GridPane.setHalignment(pvp, HPos.CENTER);
        GridPane.setHalignment(pve, HPos.CENTER);

        grid.add(info, 0, 0);
        GridPane.setHalignment(info, HPos.CENTER);
        grid.add(buttons, 0, 1);
        GridPane.setHalignment(buttons, HPos.CENTER);
    }

    /* Returns text which is showed when the user has no saved games */
    private Text getNoSavesText() {
        Text errorText = new Text("No saves to be loaded");
        errorText.setStyle("-fx-font: 28 arial; -fx-text-box-border: transparent;");
        return errorText;
    }

    /* Sets the width of the column in the given grid to given percent */
    private void addPercentColumnConstraint(GridPane grid, int percent) {
        ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(percent);
        grid.getColumnConstraints().add(col);
    }

    /* Returns HBox with Back to menu button */
    private HBox addHBox(Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(8, 10, 0, 10));

        Button backButton = new Button("Back to menu");
        backButton.setPrefSize(150, 15);

        backButton.setOnAction((event) -> {
            SceneGenerator sceneGenerator = new MenuSceneGenerator();
            Scene menuScene = sceneGenerator.generateScene(stage);
            stage.setScene(menuScene);
        });

        hbox.getChildren().addAll(backButton);

        return hbox;
    }
}
