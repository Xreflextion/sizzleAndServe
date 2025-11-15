package entity;

import java.util.*;

public class Pantry {

    private final Map<String, Recipe> recipes = new HashMap<>();

    public Map<String, Recipe> getRecipes() {
        return recipes;
    }

    public Recipe getRecipe(String dish) {
        return recipes.get(dish);
    }

    public boolean addStock(String dish, int quantity) {
        Recipe recipe = recipes.get(dish);
        if (recipe == null) {
            return false;
        }
        recipe.setStock(recipe.getStock() + quantity);
        return true;
    }

    public boolean consumeStock(String dish, int quantity) {
        Recipe recipe = recipes.get(dish);
        if (recipe == null) {
            return false;
        }
        int current = recipe.getStock();
        if (current < quantity) {
            return false;
        }
        recipe.setStock(current - quantity);
        return true;
    }

    public List<Recipe> getMenuList() {
        return new ArrayList<>(recipes.values());
    }

    public String[] getDishNames() {
        return recipes.keySet().toArray(new String[0]);
    }
}
