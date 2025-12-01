package interface_adapter.product_prices;

import java.util.Map;

import entity.Recipe;

/**
 * The State information representing the Product Prices view.
 */
public class ProductPricesState {
    private final Map<String, Recipe> recipes;
    private String selectedDishName;
    private double currentPrice;

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

    /**
     * Updates the selected dish name and adjusts the current price based on the provided dish name.
     * @param dishName the name of the dish to set as selected
     */
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
