package entity;

/**
 * A simple entity representing a recipe.
 * Recipes have a name, a predetermined base price, a current price, and a total stock
 */
public class Recipe {

    private static final String PRICE_NOT_POSITIVE = "Price must be positive";
    private static final String STOCK_NOT_NON_NEGATIVE = "Stock must be non-negative";
    private static final String EMPTY_NAME = "Name cannot be empty";
    private static final double ONE_HUNDRED = 100.0;
    private final String name;
    private final int basePrice;
    private double currentPrice;
    private int stock;

    /**
     * Creates a new recipe with the given non-empty name and positive double basePrice.
     *
     * @param name      the name
     * @param basePrice the basePrice
     * @throws IllegalArgumentException if the name is empty or if basePrice is non-positive
     */
    public Recipe(String name, int basePrice) {
        if ("".equals(name)) {
            throw new IllegalArgumentException(EMPTY_NAME);
        }
        if (basePrice <= 0) {
            throw new IllegalArgumentException(PRICE_NOT_POSITIVE);
        }
        this.name = name;
        this.basePrice = basePrice;
        this.currentPrice = basePrice;
        this.stock = 0;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    /**
     * Sets the stock price.
     *
     * @param quantity the quantity of the dish
     * @throws IllegalArgumentException if the quantity is negative
     */
    public void setStock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException(STOCK_NOT_NON_NEGATIVE);
        }
        this.stock = quantity;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    /**
     * Modifies the current price of the dish.
     *
     * @param newPrice the new value the dish will adjust to
     * @throws IllegalArgumentException if the price is negative
     */
    public void setCurrentPrice(double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException(PRICE_NOT_POSITIVE);
        }
        currentPrice = newPrice;
    }

    /**
     * Applies the margin to the base price.
     *
     * @param marginPercentage the percentage that will be applied
     * @throws IllegalArgumentException if the price is negative
     */
    public void applyMargin(int marginPercentage) {
        if (marginPercentage < 0) {
            throw new IllegalArgumentException(PRICE_NOT_POSITIVE);
        }
        currentPrice = Math.round(basePrice * (1 + marginPercentage / ONE_HUNDRED) * ONE_HUNDRED) / ONE_HUNDRED;
    }

    /**
     * Resets the current price of the dish to its base price.
     */
    public void resetCurrentPrice() {
        currentPrice = basePrice;
    }

}
