package sizzleAndServe;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

public class Pantry {
    private Map<String, Recipe> recipes;

    public Pantry() {
        recipes = new HashMap<>();
    }

    public Recipe getRecipe(String dish) {
        return recipes.get(dish);
    }

    public boolean addStock(String dish, int quantity) {
        try {
            Recipe recipe = getRecipe(dish);
            if (recipe == null) {
                return false;
            }
            recipe.setStock(recipe.getStock() + quantity);
            return true;
        } catch (Exception e) { // if the dish does not exist in the Pantry
            return false;
        }
    }

    public boolean consumeStock(String dish, int quantity) {
        try {
            Recipe recipe = getRecipe(dish);
            if (recipe == null) {
                return false;
            }
            if (recipe.getStock() >= quantity) {
                recipe.setStock(recipe.getStock() - quantity);
                return true;
            }
            else {
                return false;
            }
        } catch (Exception e) { // if the dish does not exist in the Pantry
            return false;
        }
    }

    public List<Recipe> getMenuList() {
        return (List<Recipe>) recipes.values();
    }
}