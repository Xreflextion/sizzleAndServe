package usecase.product_prices;

/**
 * Output Data for the Product Prices Use Case.
 */
public class ProductPricesOutputData {

    private final String name;
    private final double newPrice;

    public ProductPricesOutputData(String name, double newPrice) {
        this.name = name;
        this.newPrice = newPrice;
    }

    public String getName() {
        return name;
    }

    public double getNewPrice() {
        return newPrice;
    }
}
