package data_access;

import use_case.BuyServing.BuyServingDataAccessInterface;
import entity.Player;
import entity.Pantry;
import entity.Recipe;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
import java.util.Random;

public class BuyServingDataAccessObject implements BuyServingDataAccessInterface {

    private final Player player;
    private final Pantry pantry;

    public BuyServingDataAccessObject() {
        this.player = new Player("Name", 100);
        this.pantry = new Pantry();

        // Fetch three random dishes from API
        OkHttpClient client = new OkHttpClient();
        Random rand = new Random();
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
                    int price = rand.nextInt(15); // Random price below 15
                    pantry.getRecipes().put(dishName, new Recipe(dishName, price));
                }
                response.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch dish from API", e);
            }
        }
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public Pantry getPantry() {
        return pantry;
    }

    @Override
    public void savePlayer(Player player) {

    }

    @Override
    public void savePantry(Pantry pantry) {

    }
}
