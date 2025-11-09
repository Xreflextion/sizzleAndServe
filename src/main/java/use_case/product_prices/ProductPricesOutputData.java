package use_case.product_prices;

/**
 * Output Data for the Product Prices Use Case.
 */
public class ProductPricesOutputData {

    private final String name;

    public ProductPricesOutputData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
