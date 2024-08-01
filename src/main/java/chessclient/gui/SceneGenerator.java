package chessclient.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The interface SceneGenerator defines the main method of every
 * scene generator - generate scene
 */
public interface SceneGenerator {

    /**
     * Generating whole scene to be set on existing stage
     *
     * @param stage original stage
     * @return created scene
     */
    Scene generateScene(Stage stage);

}
