package chessclient.utilities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import chessclient.gui.ChessSceneGenerator;
import chessclient.gui.TypeOfGame;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SaveManager is a class which takes care of writing GameSaves into json files, and reading
 * GameSaves from previously created json files. It uses the Jackson library for serialization,
 * and deserialization of the GameSaves.
 */
public class SaveManager {

    private static final Logger logger = Logger.getLogger(SaveManager.class.getName());
    private final ObjectMapper mapper;
    private final String savesDirPath;

    /**
     * Default constructor for the class SaveManager, it uses the default ObjectMapper
     * and saves directory settings.
     */
    public SaveManager() {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        savesDirPath = "src/main/resources/saves/";
    }

    /**
     * Creates a new file in the saves directory, and writes the serialized GameSave into
     * this file. The name of the file is the current timestamp. If a file with such name
     * exists, it is overwritten.
     *
     * @param save GameSave to be written into the new file.
     */
    public void saveGame(GameSave save) {
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy'-'HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Europe/Prague"));
        String currentTime = formatter.format(System.currentTimeMillis());
        File outFile = new File(savesDirPath + currentTime);
        try {
            outFile.createNewFile();
            mapper.writerWithDefaultPrettyPrinter().writeValue(outFile, save);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't create save", e);
        }
    }

    /**
     * Loads a GameSave saved in a file with the given save name, and returns a ChessSceneGenerator,
     * which creates a Game from the GameSave, and renders GUI for it.
     *
     * @param saveName   name of the file of the saved game
     * @param typeOfGame TypeOfGame which determines the players participating in the game
     * @return ChessSceneGenerator, which creates a Game from the GameSave, and renders GUI for it.
     */
    public ChessSceneGenerator loadSavedGame(String saveName, TypeOfGame typeOfGame) {
        File outFile = new File(savesDirPath + saveName);
        try {
            GameSave gameSave = mapper.readValue(outFile, GameSave.class);
            return new ChessSceneGenerator(gameSave, typeOfGame);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't load save", e);
        }
        return null;
    }

    /**
     * Returns an array of files which are located in the saves directory.
     *
     * @return array of files located in the saves directory
     */
    public File[] getSaves() {
        File saves = new File(savesDirPath);
        return saves.listFiles();
    }
}
