package entity;

public class Recipe {
    private String name;
    private final int basePrice;
    private int currentPrice;
    private int stock;

    public Recipe(String name, int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.currentPrice = basePrice;
        this.stock = 0;
    }

    public String getName() {
        return name;
    }

    public void setStock(int quantity){
        stock = quantity;
    }

    public int getStock() {
        return stock;
    }

    public int getPrice() {
        return currentPrice;
    }

    public void setPrice(int newPrice) {
        currentPrice = newPrice;
    }

    public void resetPrice() {
        currentPrice = basePrice;
    }
}