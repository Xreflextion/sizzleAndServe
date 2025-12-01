package data_access;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.Map;

public class FileHelperObject {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject saveData;
    private final String fileName;

    public FileHelperObject(String fileName) {
        this.fileName = fileName;
    }

    public void loadFromFile() {
        try {
            // Reading from json file and saving into our JsonObject
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            saveData = gson.fromJson(bufferedReader, JsonObject.class);
            if (saveData == null) {
                saveData = new JsonObject();
            }
        } catch (FileNotFoundException e) {
            saveData = new JsonObject();
        }
    }


    public void save(Map<String, JsonObject> jsonObjects) throws IOException {
        try {
            FileWriter writer = new FileWriter(fileName);

            for (String jsonObjectProperty : jsonObjects.keySet()) {
                saveData.add(jsonObjectProperty, jsonObjects.get(jsonObjectProperty));
            }
            // saving to a file
            gson.toJson(saveData, writer);
            writer.close();
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public JsonObject getObjectFromSaveData(String key) {
        if (saveData.keySet().contains(key)) {
            return saveData.getAsJsonObject(key);
        }
        return new JsonObject();
    }

    public void saveArray(String key, JsonArray array) throws IOException {
        // add or replace the array under the given key in the in-memory saveData
        saveData.add(key, array);
        // write the updated saveData back to file
        try (FileWriter writer = new FileWriter(fileName)) {
            gson.toJson(saveData, writer);
        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public JsonArray getArrayFromSaveData(String key) {
        if (saveData.keySet().contains(key)) {
            return saveData.getAsJsonArray(key);
        }
        return new JsonArray();
    }
}
