package interface_adapter.product_prices;

import entity.Recipe;
import java.util.Map;

/**
 * The State information representing the Product Prices view.
 */
public class ProductPricesState {
    private final Map<String, Recipe> recipes;
    private String selectedDishName;  // Track which dish is being modified
    private double currentPrice;     // Track the price being edited

    public ProductPricesState(Map<String, Recipe> recipes) {
        this.recipes = recipes;
        this.selectedDishName = null;
        this.currentPrice = 0;
    }

    public Map<String, Recipe> getRecipes() {
        return recipes;
    }

    public String getSelectedDishName() {
        return selectedDishName;
    }

    public void setSelectedDishName(String dishName) {
        this.selectedDishName = dishName;
        if (dishName != null && recipes.containsKey(dishName)) {
            this.currentPrice = recipes.get(dishName).getCurrentPrice();
        }
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double price) {
        this.currentPrice = price;
    }
}