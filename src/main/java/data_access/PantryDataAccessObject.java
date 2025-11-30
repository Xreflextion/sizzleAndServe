package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import use_case.buy_serving.PantryDataAccessInterface;
import entity.Pantry;
import entity.Recipe;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONArray;
import use_case.product_prices.ProductPricesPantryDataAccessInterface;
import constants.Constants;
import use_case.simulate.SimulatePantryDataAccessInterface;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.*;
import java.io.IOException;

public class PantryDataAccessObject implements PantryDataAccessInterface, ProductPricesPantryDataAccessInterface, SimulatePantryDataAccessInterface {

    private Pantry pantry;
    private FileHelperObject fileHelperObject;

    public PantryDataAccessObject(FileHelperObject fileHelperObject) {

        this.pantry = new Pantry();

        this.fileHelperObject = fileHelperObject;
        JsonArray recipeArray = fileHelperObject.getArrayFromSaveData(Constants.RECIPE_KEY);
        if (recipeArray.size() == 3) {

            for (JsonElement element: recipeArray) {
                JsonObject recipeJsonObject = element.getAsJsonObject();
                String name = recipeJsonObject.get("name").getAsString();
                int stock = recipeJsonObject.get("stock").getAsInt();
                int basePrice = recipeJsonObject.get("base_price").getAsInt();
                double currentPrice = recipeJsonObject.get("current_price").getAsDouble();
                Recipe recipe = new Recipe(name, basePrice);
                recipe.setStock(stock);
                recipe.setCurrentPrice(currentPrice);
                pantry.getPantry().put(name, recipe);
            }

        } else {
            randomizePantry();
        }



    }

    public void randomizePantry() {
        // Fetch three random dishes from API
        OkHttpClient client = new OkHttpClient();
        int max = 15;
        int min = 1;
        int numDishes = 3;
        for (int i = 0; i < numDishes; i++) {
            try {
                Request request = new Request.Builder()
                        .url("https://www.themealdb.com/api/json/v1/1/random.php")
                        .build();
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                JSONObject json = new JSONObject(responseBody);
                JSONArray meals = json.getJSONArray("meals");
                if (meals.length() > 0) {
                    JSONObject meal = meals.getJSONObject(0);
                    String dishName = meal.getString("strMeal");
                    int price = new Random().nextInt((max - min) + 1) + min;
                    pantry.getPantry().put(dishName, new Recipe(dishName, price));

                    String mealURL = meal.getString("strMealThumb");
                    downloadTempImage(mealURL, dishName);
                }
                response.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch dish from API", e);
            }
        }
    }

    public PantryDataAccessObject(Pantry pantry) {
        this.pantry = pantry;
    }

    @Override
    public Pantry getPantry() {
        return pantry;
    }

     /**
     * Return a mapping of dish name to integer where
     * each integer represents the stock of the corresponding dish name
     */
    public Map<String, Integer> getStock() {
        Map<String, Integer> stock = new HashMap<>();
        for (String dishName: pantry.getDishNames()) {
            stock.put(dishName, pantry.getRecipe(dishName).getStock());
        }
        return stock;
    }

    /**
     * Replace the current stock with the stock passed in
     * @param stock The new stock
     */
    public void saveStock(Map<String, Integer> stock) {
        for (String dishName: stock.keySet()) {
            pantry.getRecipe(dishName).setStock(stock.get(dishName));
        }
        save();
    }

    public Map<String, Double> getCurrentPrices() {
        Map<String, Double> prices = new HashMap<>();
        for (String dishName: pantry.getDishNames()) {
            prices.put(dishName, pantry.getRecipe(dishName).getCurrentPrice());
        }
        return prices;
    }


    @Override
    public void changePrice(Recipe recipe) {
        pantry.getPantry().put(recipe.getName(), recipe);
        save();
    }

    @Override
    public void savePantry(Pantry pantry) {
        this.pantry = pantry;
        save();
    }

    public static File downloadTempImage(String imageURL, String dishName) throws Exception {
        String dirPath = Constants.DIR_PATH;
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File tempFile = new File(dir, dishName.replaceAll(Constants.REGEX_CHARACTERS,
                Constants.REPLACEMENT_CHARACTER) + Constants.FILE_TYPE);
        URL url = new URL(imageURL);
        try (InputStream inputStream = url.openStream()) {
            Files.copy(inputStream, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }

        return tempFile;
    }

    public void saveToFile() throws IOException {
        JsonArray pantryArray = new JsonArray();
        for (String dishName : pantry.getDishNames()) {
            Recipe recipe = pantry.getRecipe(dishName);
            JsonObject obj = new JsonObject();
            obj.addProperty("name", recipe.getName());
            obj.addProperty("stock", recipe.getStock());
            obj.addProperty("base_price", recipe.getBasePrice());
            obj.addProperty("current_price", recipe.getCurrentPrice());
            pantryArray.add(obj);
        }
        fileHelperObject.saveArray(Constants.RECIPE_KEY, pantryArray);
    }

    public void save() {
        try {
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
