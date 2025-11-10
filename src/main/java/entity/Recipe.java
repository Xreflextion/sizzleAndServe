package entity;

/**
 * A simple entity representing a recipe.
 * Recipes have a name, a predetermined base price, a current price, and a total stock
 */
public class Recipe {
    private final String name;
    private final int basePrice;
    private int currentPrice;
    private int stock;

    /**
     * Creates a new recipe with the given non-empty name and positive integer basePrice.
     * @param name the name
     * @param basePrice the basePrice
     * @throws IllegalArgumentException if the name is empty or if basePrice is non-positive
     */
    public Recipe(String name, int basePrice) {
        if ("".equals(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        this.name = name;
        this.basePrice = basePrice;
        this.currentPrice = basePrice;
        this.stock = 0;
    }

    public String getName() {
        return name;
    }

    public void setStock(int quantity){
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be non-negative");
        }
        stock = quantity;
    }

    public int getStock() {
        return stock;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int newPrice) {
        if (newPrice <= 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        currentPrice = newPrice;
    }

    public void resetCurrentPrice() {
        currentPrice = basePrice;
    }
}