package data_access;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import constants.Constants;
import entity.Pantry;
import entity.Recipe;
import use_case.buy_serving.PantryDataAccessInterface;
import use_case.product_prices.ProductPricesPantryDataAccessInterface;
import use_case.simulate.SimulatePantryDataAccessInterface;

public class PantryDataAccessObject implements PantryDataAccessInterface, ProductPricesPantryDataAccessInterface,
        SimulatePantryDataAccessInterface {

    private static final int REQUIRED_RECIPE_COUNT = 3;
    private Pantry pantry;
    private FileHelperObject fileHelperObject;

    public PantryDataAccessObject(Pantry pantry) {
        this.pantry = pantry;
    }

    public PantryDataAccessObject(FileHelperObject fileHelperObject) {

        this.pantry = new Pantry();

        this.fileHelperObject = fileHelperObject;
        final JsonArray recipeArray = fileHelperObject.getArrayFromSaveData(Constants.RECIPE_KEY);
        if (recipeArray.size() == REQUIRED_RECIPE_COUNT) {

            for (JsonElement element: recipeArray) {
                final JsonObject recipeJsonObject = element.getAsJsonObject();
                final String name = recipeJsonObject.get("name").getAsString();
                final int stock = recipeJsonObject.get("stock").getAsInt();
                final int basePrice = recipeJsonObject.get("base_price").getAsInt();
                final double currentPrice = recipeJsonObject.get("current_price").getAsDouble();
                final Recipe recipe = new Recipe(name, basePrice);
                recipe.setStock(stock);
                recipe.setCurrentPrice(currentPrice);
                pantry.getPantry().put(name, recipe);
            }

        }
        else {
            randomizePantry();
        }
    }

    /**
     * Randomizes the pantry with three random dishes fetched from an external API.
     * @throws RuntimeException if meals cannot be fetched from API
     */
    public void randomizePantry() {
        // Fetch three random dishes from API
        final OkHttpClient client = new OkHttpClient();
        final int max = 15;
        final int min = 1;
        for (int i = 0; i < REQUIRED_RECIPE_COUNT; i++) {
            try {
                final Request request = new Request.Builder()
                        .url("https://www.themealdb.com/api/json/v1/1/random.php")
                        .build();
                final Response response = client.newCall(request).execute();
                final String responseBody = response.body().string();
                final JSONObject json = new JSONObject(responseBody);
                final JSONArray meals = json.getJSONArray("meals");
                if (meals.length() > 0) {
                    final JSONObject meal = meals.getJSONObject(0);
                    final String dishName = meal.getString("strMeal");
                    final int price = new Random().nextInt((max - min) + 1) + min;
                    pantry.getPantry().put(dishName, new Recipe(dishName, price));

                    final String mealUrl = meal.getString("strMealThumb");
                    downloadTempImage(mealUrl, dishName);
                }
                response.close();
            }
            catch (IOException | JSONException exception) {
                throw new RuntimeException("Failed to fetch dish from API", exception);
            }
        }
    }

    @Override
    public Pantry getPantry() {
        return pantry;
    }

    /**
     * Return a mapping of dish name to integer where each integer represents the stock of the corresponding dish name.
     * @return the stock of all dishes
     */
    public Map<String, Integer> getStock() {
        final Map<String, Integer> stock = new HashMap<>();
        for (String dishName: pantry.getDishNames()) {
            stock.put(dishName, pantry.getRecipe(dishName).getStock());
        }
        return stock;
    }

    /**
     * Replace the current stock with the stock passed in.
     * @param stock The new stock
     */
    public void saveStock(Map<String, Integer> stock) {
        for (String dishName: stock.keySet()) {
            pantry.getRecipe(dishName).setStock(stock.get(dishName));
        }
        // TODO save
    }

    /**
     * Retrieves the current prices of dishes available in the pantry.
     * @return a map where the keys are dish names (as Strings) and the values are their current prices (as Doubles)
     */
    public Map<String, Double> getCurrentPrices() {
        final Map<String, Double> prices = new HashMap<>();
        for (String dishName: pantry.getDishNames()) {
            prices.put(dishName, pantry.getRecipe(dishName).getCurrentPrice());
        }
        return prices;
    }

    @Override
    public void changePrice(Recipe recipe) {
        pantry.getPantry().put(recipe.getName(), recipe);
        // TODO save
    }

    @Override
    public void savePantry(Pantry newPantry) {
        this.pantry = pantry;
        // TODO save

    }

    /**
     * Downloads a temporary image from the given URL and stores it in a predefined directory.
     * @param imageUrl The URL of the image to download.
     * @param dishName The name of the dish used to generate the filename for the temporary image.
     * @throws IOException If any error occurs during the download or file creation process.
     */
    public static void downloadTempImage(String imageUrl, String dishName) throws IOException {
        final String dirPath = Constants.DIR_PATH;
        final File dir = new File(dirPath);

        final File tempFile = new File(dir, dishName.replaceAll(Constants.REGEX_CHARACTERS,
                Constants.REPLACEMENT_CHARACTER) + Constants.FILE_TYPE);
        final URL url = new URL(imageUrl);
        try (InputStream inputStream = url.openStream()) {
            Files.copy(inputStream, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
