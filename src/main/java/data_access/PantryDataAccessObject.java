package data_access;

import use_case.BuyServing.PantryDataAccessInterface;
import entity.Player;
import entity.Pantry;
import entity.Recipe;
import okhttp3.*;
import org.json.JSONObject;
import org.json.JSONArray;
import java.util.Random;

public class PantryDataAccessObject implements PantryDataAccessInterface {

    private final Pantry pantry;

    public PantryDataAccessObject() {

        this.pantry = new Pantry();
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
                }
                response.close();
            } catch (Exception e) {
                throw new RuntimeException("Failed to fetch dish from API", e);
            }
        }
    }

    @Override
    public Pantry getPantry() {
        return pantry;
    }


    @Override
    public void savePantry(Pantry pantry) {

    }
}
