package use_case;

import entity.Pantry;
import entity.Recipe;
import use_case.product_prices.ProductPricesPantryDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class TestProductPricesDataAccess implements ProductPricesPantryDataAccessInterface {

    private final Map<String, Recipe> recipeStore = new HashMap<>();

    @Override
    public Pantry getPantry() {
        return null;
    }

    @Override
    public void changePrice(Recipe recipe) {
        recipeStore.put(recipe.getName(), recipe);
    }

    public Recipe getRecipe(String name) {
        return recipeStore.get(name);
    }

    public boolean containsRecipe(String name) {
        return recipeStore.containsKey(name);
    }
}
