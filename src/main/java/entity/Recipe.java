package entity;

/**
 * A simple entity representing a recipe.
 * Recipes have a name, a predetermined base price, a current price, and a total stock
 */
public class Recipe {
    private final String name;
    private final double basePrice;
    private double currentPrice;
    private int stock;

    /**
     * Creates a new recipe with the given non-empty name and positive double basePrice.
     * @param name the name
     * @param basePrice the basePrice
     * @throws IllegalArgumentException if the name is empty or if basePrice is non-positive
     */
    public Recipe(String name, double basePrice) {
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

    public double getBasePrice() {
        return basePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        currentPrice = newPrice;
    }

    public void applyMargin(int marginPercentage) {
        if (marginPercentage < 0) {
            throw new IllegalArgumentException("Price must be positive");
        }
        currentPrice = Math.round(currentPrice * (1 + marginPercentage)) / 100.0; // rounds it to 2 decimal places
    }

    public void resetCurrentPrice() {
        currentPrice = basePrice;
    }
}