package use_case.product_prices;

/**
 * The input data for the Product Prices Use Case.
 */
public class ProductPricesInputData {

    private final String name;
    private final int currentPrice;

    public ProductPricesInputData(String name, int currentPrice) {
        this.name = name;
        this.currentPrice = currentPrice;
    }

    String getName() {
        return name;
    }

    int getCurrentPrice() {
        return currentPrice;
    }
}
