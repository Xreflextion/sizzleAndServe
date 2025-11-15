package entity;

public class Recipe {

    private final String name;
    private final int basePrice;
    private int currentPrice;
    private int stock;

    public Recipe(String name, int basePrice) {
        this.name = name;
        this.basePrice = basePrice;
        this.currentPrice = basePrice;
        this.stock = 0;
    }

    public String getName() {return name;}

    public int getStock() {return stock;}

    public void setStock(int quantity) {this.stock = quantity;}

    public int getCost(){return basePrice;}

}
