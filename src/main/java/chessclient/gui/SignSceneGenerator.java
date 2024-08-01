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
 * SignSceneGenerator provides registration of new user by using chess client
 */
public class SignSceneGenerator implements SceneGenerator {

    /**
     * Generating whole registration scene with the opportunity to go back
     * to menu, according to communication with server printing out if the
     * registration was successful or not
     *
     * @param stage original stage
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

        Text signTxt = new Text("Sign up");
        signTxt.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 20));
        gridPane.add(signTxt, 0, 0);

        Label labelNewUser = new Label("Username");
        gridPane.add(labelNewUser, 0, 1);

        TextField textNewUser = new TextField();
        textNewUser.setPromptText("unique username");
        gridPane.add(textNewUser, 1, 1);

        Label labelNewPassword = new Label("Password");
        gridPane.add(labelNewPassword, 0, 2);

        PasswordField textNewPassword = new PasswordField();
        textNewPassword.setPromptText("password");
        gridPane.add(textNewPassword, 1, 2);

        Label labelConfPassword = new Label("Password confirmation");
        gridPane.add(labelConfPassword, 0, 3);

        PasswordField textConfPassword = new PasswordField();
        textConfPassword.setPromptText("password confirmation");
        gridPane.add(textConfPassword, 1, 3);

        Button signButton = new Button("Sign up");
        gridPane.add(signButton, 1, 4);

        HBox hBox = addHBox(stage);
        borderPane.setTop(hBox);
        borderPane.setCenter(gridPane);

        Scene signScene = new Scene(borderPane, 700, 500);

        signButton.setOnAction(event -> {
            String username = textNewUser.getText();
            String newPassword = textNewPassword.getText();
            String confPassword = textConfPassword.getText();
            if (newPassword.equals(confPassword)) {
                if (chessClient.register(username, newPassword)) {
                    SceneGenerator sceneGenerator = new LoginSceneGenerator();
                    Scene loginScene = sceneGenerator.generateScene(stage);
                    stage.setScene(loginScene);
                } else {
                    Text registrationFail = new Text("Registration failed");
                    registrationFail.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 14));
                    registrationFail.setFill(Color.RED);
                    gridPane.add(registrationFail, 1, 5);
                }
            } else {
                Text passConfPass = new Text("Passwords are not same");
                passConfPass.setFont(Font.font("Times New Roman", FontWeight.LIGHT, 14));
                passConfPass.setFill(Color.RED);
                gridPane.add(passConfPass, 2, 3);
                textConfPassword.setText("");
                textNewPassword.setText("");

            }
        });

        return signScene;
    }

    private HBox addHBox(Stage stage) {
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(8, 10, 0, 10));

        Button loginButton = new Button("Back to login");
        loginButton.setPrefSize(90, 15);

        loginButton.setOnAction(event -> {
            SceneGenerator sceneGenerator = new LoginSceneGenerator();
            Scene loginScene = sceneGenerator.generateScene(stage);
            stage.setScene(loginScene);
        });

        hbox.getChildren().addAll(loginButton);

        return hbox;
    }

}
