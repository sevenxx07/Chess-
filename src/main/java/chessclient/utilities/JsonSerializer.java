package chessclient.utilities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import chessclient.network.Turn;
import chessclient.network.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provides conversion from java Objects to json Strings, and
 * from json Strings back to selected java Objects.
 */
public class JsonSerializer {

    private static final Logger logger = Logger.getLogger(JsonSerializer.class.getName());
    private final ObjectMapper mapper;

    /**
     * Initializes the Objects used by the jackson library.
     */
    public JsonSerializer() {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    /**
     * Transforms given java Object into a json String.
     *
     * @param o Object to be serialized
     * @return json String with the serialized object
     */
    public String getObjectInJson(Object o) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Couldn't serialize given object", e);
            return null;
        }
    }

    /**
     * Deserializes List of Users from json.
     *
     * @param s json representing serialized List of Users
     * @return List of Users retrieved from json
     */
    public List<User> deserializeUsersList(String s) {
        try {
            return new ArrayList<>(Arrays.asList(mapper.readValue(s, User[].class)));
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Couldn't deserialize user", e);
            return null;
        }
    }

    /**
     * Deserializes player turn from json to Turn object.
     *
     * @param s serialized Turn object in json
     * @return deserialized Turn, null when exception occurs
     */
    public Turn deserializeTurn(String s) {
        try {
            return mapper.readValue(s, Turn.class);
        } catch (JsonProcessingException e) {
            logger.log(Level.SEVERE, "Couldn't deserialize turn", e);
            return null;
        }
    }

}
