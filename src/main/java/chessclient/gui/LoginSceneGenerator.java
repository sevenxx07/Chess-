package chessclient.gui;

import chessclient.network.ChessClient;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * LoginSceneGenerator provides login for user by using the connection
 * with server
 * User has the opportunity to sign up if he doesn't have account yet
 * After successful login user is send to queueSceneGenerator
 */
public class LoginSceneGenerator implements SceneGenerator {

    /**
     * Generating login scene, handling all button clicks (login, sing up, back to menu)
     * Communicating with chess client to find out if the login was successful
     *
     * @param stage from original stage
     * @return created scene
     */
    @Override
    public Scene generateScene(Stage stage) {
        ChessClient chessClient = new ChessClient();

        BorderPane borderPane = new BorderPane();
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setVgap(10);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(10));

        Text loginTxt = new Text("Login");
        loginTxt.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        gridPane.add(loginTxt, 0, 0);

        Label labelUser = new Label("Username");
        gridPane.add(labelUser, 0, 1);

        TextField textUser = new TextField();
        textUser.setPromptText("username");
        gridPane.add(textUser, 1, 1);

        Label labelPassword = new Label("Password");
        gridPane.add(labelPassword, 0, 2);

        PasswordField textPassword = new PasswordField();
        textPassword.setPromptText("password");
        gridPane.add(textPassword, 1, 2);

        Button loginButton = new Button("Login");
        gridPane.add(loginButton, 1, 3);

        Text signUpTxt = new Text("Sign up");
        signUpTxt.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        gridPane.add(signUpTxt, 0, 4);

        Button signUpButton = new Button("Sign up");
        gridPane.add(signUpButton, 1, 5);

        HBox hBox = addHBox(stage);
        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        Scene loginScene = new Scene(borderPane, 700, 500);

        loginButton.setOnAction(event -> {
            String userName = textUser.getText();
            String passWord = textPassword.getText();
            if (chessClient.login(userName, passWord)) {
                SceneGenerator sceneGenerator = new QueueSceneGenerator(chessClient);
                stage.setScene(sceneGenerator.generateScene(stage));
            } else {
                Text loginFail = new Text("Login failed");
                loginFail.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 14));
                loginFail.setFill(Color.RED);
                gridPane.add(loginFail, 2, 3);
            }
        });

        signUpButton.setOnAction(event -> {

            SceneGenerator sceneGenerator = new SignSceneGenerator();
            Scene signScene = sceneGenerator.generateScene(stage);
            stage.setScene(signScene);
        });

        return loginScene;
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
