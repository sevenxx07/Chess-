package chessclient.gui;

import chessclient.network.ChessClient;
import chessclient.network.User;
import chessclient.utilities.JsonSerializer;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.List;

/**
 * StatisticsSceneGenerator is a class which creates a scene, in which the user can
 * see top network 10 players.
 */
public class StatisticsSceneGenerator implements SceneGenerator {

    @Override
    public Scene generateScene(Stage stage) {
        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);

        HBox hBox = addHBox(stage);
        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        generateUsersText(gridPane);
        return new Scene(borderPane, 700, 500);
    }

    /* Fills the given grid with rows representing the leaderboard, or an error message */
    private void generateUsersText(GridPane grid) {
        JsonSerializer js = new JsonSerializer();
        ChessClient client = new ChessClient();

        client.startConnection();
        String json = client.getTopPlayers();
        client.stopConnection();

        if (json != null) {
            grid.setStyle("-fx-grid-lines-visible: true");
            List<User> users = js.deserializeUsersList(json);

            for (int i = 0; i < users.size(); i++) {
                GridPane row = this.getColumnRow(users.get(i), i);
                grid.add(row, 0, i);
            }
        } else {
            Text errorText = new Text("No internet connection");
            errorText.setFill(Paint.valueOf("red"));
            errorText.setStyle("-fx-font: 28 arial; -fx-text-box-border: transparent;");
            grid.add(errorText, 0, 0);
        }
    }

    /* Returns a GridPane which represents a table row, in which the user's placement, user name,
    and rating are stored */
    private GridPane getColumnRow(User u, int index) {
        GridPane row = new GridPane();
        this.addPercentColumnConstraint(row, 15);
        this.addPercentColumnConstraint(row, 60);
        this.addPercentColumnConstraint(row, 25);
        row.setBackground(new Background(this.getLeaderboardBackground(index)));

        Text place = new Text(index + 1 + ".");
        Text userText = new Text(u.getName());
        Text rating = new Text(u.getRating() + "%");
        place.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        userText.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        rating.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));

        row.add(place, 0, 0);
        place.setTextAlignment(TextAlignment.CENTER);
        GridPane.setMargin(place, new Insets(7, 5, 7, 10));
        GridPane.setHalignment(place, HPos.CENTER);
        row.add(userText, 1, 0);
        GridPane.setMargin(userText, new Insets(7, 5, 7, 10));
        row.add(rating, 2, 0);
        GridPane.setMargin(rating, new Insets(7, 5, 7, 10));
        GridPane.setHalignment(rating, HPos.CENTER);
        row.setStyle("-fx-grid-lines-visible: true");

        return row;
    }

    /* Gets the BackgroundFill for the given player row, which signifies the placement of the player
     * in the leaderboard */
    private BackgroundFill getLeaderboardBackground(int index) {
        if (index == 0) {
            return new BackgroundFill(Paint.valueOf("#FFD700"), null, null);
        } else if (index == 1) {
            return new BackgroundFill(Paint.valueOf("#AAA9AD"), null, null);
        } else if (index == 2) {
            return new BackgroundFill(Paint.valueOf("#CD7F32"), null, null);
        } else {
            return new BackgroundFill(Paint.valueOf("#FFFFFF"), null, null);
        }
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
