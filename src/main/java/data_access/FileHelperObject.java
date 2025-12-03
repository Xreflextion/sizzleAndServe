package data_access;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class FileHelperObject {

    private final String fileName;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject saveData;

    public FileHelperObject(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Loads data from a file specified during the instantiation of the object.
     */
    public void loadFromFile() {
        try {
            // Reading from json file and saving into our JsonObject
            final FileReader fileReader = new FileReader(fileName);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);
            saveData = gson.fromJson(bufferedReader, JsonObject.class);
            if (saveData == null) {
                saveData = new JsonObject();
            }
        }
        catch (FileNotFoundException exception) {
            saveData = new JsonObject();
        }
    }

    /**
     * Saves a map of JSON objects to a file.
     *
     * @param jsonObjects a map containing string keys and corresponding JSON object values to be saved
     * @throws IOException if an I/O error occurs during the file writing process
     */
    public void save(Map<String, JsonObject> jsonObjects) throws IOException {
        try {
            final FileWriter writer = new FileWriter(fileName);

            for (String jsonObjectProperty : jsonObjects.keySet()) {
                saveData.add(jsonObjectProperty, jsonObjects.get(jsonObjectProperty));
            }
            // saving to a file
            gson.toJson(saveData, writer);
            writer.close();
        }
        catch (IOException exception) {
            throw new IOException(exception);
        }
    }

    /**
     * Retrieves a JsonObject from the in-memory saveData using the specified key.
     * If the key is not present in the saveData, a new empty JsonObject is returned.
     *
     * @param key the key used to retrieve the JsonObject from the saveData
     * @return the JsonObject corresponding to the given key, or a new empty JsonObject if the key does not exist
     */
    public JsonObject getObjectFromSaveData(String key) {
        final JsonObject result;
        if (saveData.keySet().contains(key)) {
            result = saveData.getAsJsonObject(key);
        }
        else {
            result = new JsonObject();
        }
        return result;
    }

    /**
     * Saves a JsonArray to the in-memory saveData under the specified key and writes the updated
     * saveData to a file.
     *
     * @param key   the key used to store the JsonArray in the in-memory saveData
     * @param array the JsonArray to be saved
     * @throws IOException if an I/O error occurs during the file writing process
     */
    public void saveArray(String key, JsonArray array) throws IOException {
        // add or replace the array under the given key in the in-memory saveData
        saveData.add(key, array);
        // write the updated saveData back to file
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(saveData, writer);
        }
        catch (IOException exception) {
            throw new IOException(exception);
        }
    }

    /**
     * Retrieves a JsonArray from the in-memory saveData using the specified key.
     * If the key does not exist in the saveData, an empty JsonArray is returned.
     *
     * @param key the key used to retrieve the JsonArray from the saveData
     * @return the JsonArray corresponding to the given key, or an empty JsonArray if the key does not exist
     */
    public JsonArray getArrayFromSaveData(String key) {
        final JsonArray result;
        if (saveData.keySet().contains(key)) {
            result = saveData.getAsJsonArray(key);
        }
        else {
            result = new JsonArray();
        }
        return result;
    }
}
