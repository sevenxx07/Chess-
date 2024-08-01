package cz.cvut.fel.pjv.chessclient.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.InputStream;

public class ChessApp extends Application {
    Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("Chess");

        InputStream imageInputStream = ChessApp.class.getResourceAsStream("/images/chessicon.png");
        Image image = new Image(imageInputStream);
        window.getIcons().add(image);

        SceneGenerator sceneGenerator = new MenuSceneGenerator();
        Scene chessScene = sceneGenerator.generateScene(window);


        window.setScene(chessScene);
        window.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
