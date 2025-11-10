package entity;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * A simple entity representing a Pantry.
 * A pantry contains a map of recipe names to recipe objects.
 */
public class Pantry {
    private final Map<String, Recipe> recipes;

    /**
     * Creates a new pantry initialized with 3 dishes.
     */
    public Pantry(Recipe dish1, Recipe dish2, Recipe dish3) {
        recipes = new HashMap<>();
        recipes.put(dish1.getName(), dish1);
        recipes.put(dish2.getName(), dish2);
        recipes.put(dish3.getName(), dish3);
    }

    public Recipe getRecipe(String dish) {
        return recipes.get(dish);
    }

    /**
     * Adds the amount of quantity to the stock of the given dish
     * @param dish the name of the dish
     * @param quantity the quantity of the dish
     * @throws IllegalArgumentException if the dish is not found in the pantry
     */
    public boolean addStock(String dish, int quantity) {
        Recipe recipe = getRecipe(dish);
        if (recipe == null) {
            throw new NullPointerException("Dish does not exist in the Pantry");
        }
        recipe.setStock(recipe.getStock() + quantity);
        return true;
    }

    /**
     * Removes the amount of quantity to the stock of the given dish (if there is enough in stock)
     * @param dish the name of the dish
     * @param quantity the quantity of the dish
     * @throws IllegalArgumentException if the dish is not found in the pantry
     */
    public boolean consumeStock(String dish, int quantity) {
        Recipe recipe = getRecipe(dish);
        if (recipe == null) {
            throw new NullPointerException("Dish does not exist in the Pantry");
        }
        if (recipe.getStock() >= quantity) {
            recipe.setStock(recipe.getStock() - quantity);
            return true;
        }
        else {
            return false;
        }
    }

    public List<Recipe> getMenuList() {
        return (List<Recipe>) recipes.values();
    }
}