package entity;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple entity representing a Pantry.
 * A pantry contains a map of recipe names to recipe objects.
 */
public class Pantry {

    private static final String DISH_NOT_EXIST = "Dish does not exist in the Pantry";
    private final Map<String, Recipe> recipes;

    /**
     * Creates a new pantry initialized with 3 dishes.
     * @param dish1 the first dish object
     * @param dish2 the second dish object
     * @param dish3 the third dish object
     */
    public Pantry(Recipe dish1, Recipe dish2, Recipe dish3) {
        recipes = new HashMap<>();
        recipes.put(dish1.getName(), dish1);
        recipes.put(dish2.getName(), dish2);
        recipes.put(dish3.getName(), dish3);
    }

    public Pantry() {
        recipes = new HashMap<>();
    }

    public Map<String, Recipe> getPantry() {
        return recipes;
    }

    /**
     * Provides the recipe object associated with the dish name.
     * @param dish the name of the dish
     * @return Recipe
     */
    public Recipe getRecipe(String dish) {
        return recipes.get(dish);
    }

    /**
     * Adds the amount of quantity to the stock of the given dish.
     * @param dish the name of the dish
     * @param quantity the quantity of the dish
     * @return boolean
     * @throws NullPointerException if the dish is not found in the pantry
     */
    public boolean addStock(String dish, int quantity) {
        final Recipe recipe = recipes.get(dish);
        if (recipe == null) {
            throw new NullPointerException(DISH_NOT_EXIST);
        }
        recipe.setStock(recipe.getStock() + quantity);
        return true;
    }

    /**
     * Removes the amount of quantity to the stock of the given dish (if there is enough in stock).
     * @param dish the name of the dish
     * @param quantity the quantity of the dish
     * @return boolean
     * @throws NullPointerException if the dish is not found in the pantry
     */
    public boolean consumeStock(String dish, int quantity) {
        final Recipe recipe = getRecipe(dish);
        if (recipe == null) {
            throw new NullPointerException(DISH_NOT_EXIST);
        }
        final boolean result;
        if (recipe.getStock() >= quantity) {
            recipe.setStock(recipe.getStock() - quantity);
            result = true;
        }
        else {
            result = false;
        }
        return result;
    }

    public String[] getDishNames() {
        return recipes.keySet().toArray(new String[0]);
    }
}
