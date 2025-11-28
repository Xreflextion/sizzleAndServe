package data_access;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;

public class FileDataObject {

    public static final String FILE_NAME = "src/main/resources/save_data.json";
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private JsonObject saveData;
    public static void main(String[] args) {
        new FileDataObject().save();
    }

    public FileDataObject() {
        try {
            // Reading from json file and saving into our JsonObject
            FileReader fileReader =  new FileReader(FILE_NAME);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            saveData = gson.fromJson(bufferedReader, JsonObject.class);


//            System.out.println(object.getAsJsonObject("player"));

            System.out.println(saveData.keySet()); // keys in object
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
            // TODO if this is thrown when calling this file in appbuilder/main, we should use default files
        }


    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(FILE_NAME);
            JsonObject player = new JsonObject();

            JsonArray recipes = new JsonArray(3); // Creating json array with set initial length
            JsonObject recipe1 = new JsonObject();
            recipe1.addProperty("name", "name");
            recipe1.addProperty("stock", 5);
            recipe1.addProperty("base_price", 10.0);
            recipe1.addProperty("current_price", 20.0);

            JsonObject recipe2 = new JsonObject();
            recipe1.addProperty("name", "name2");
            recipe1.addProperty("stock", 1);
            recipe1.addProperty("base_price", 12.0);
            recipe1.addProperty("current_price", 18.0);

            JsonObject recipe3 = new JsonObject();
            recipe1.addProperty("name", "name3");
            recipe1.addProperty("stock", 2);
            recipe1.addProperty("base_price", 14.0);
            recipe1.addProperty("current_price", 14.0);

            recipes.add(recipe1);
            recipes.add(recipe2);
            recipes.add(recipe3);

            JsonArray days = new JsonArray(); // creating json array with any length
            JsonObject day1 = new JsonObject();
            day1.addProperty("day_number", 1);
            day1.addProperty("revenue", 20.0);
            day1.addProperty("expenses", 14.0);
            day1.addProperty("rating", 3);
            JsonArray rating1 = new JsonArray();
            rating1.add(1);
            rating1.add(5);
            day1.add("ratings_list", rating1);
            days.add(day1);

            player.addProperty("current_balance", 50); // Adding one value
            player.addProperty("current_day", 20);
            player.addProperty("current_customer_count", 50);
            saveData.add("player", player); // Adding a Json
            System.out.println(saveData.keySet());

            // saving to a file
            gson.toJson(saveData, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
