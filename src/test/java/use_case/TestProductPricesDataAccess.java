package use_case;

import entity.Recipe;
import use_case.product_prices.ProductPricesUserDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

public class TestProductPricesDataAccess implements ProductPricesUserDataAccessInterface {

    private final Map<String, Recipe> recipeStore = new HashMap<>();

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
