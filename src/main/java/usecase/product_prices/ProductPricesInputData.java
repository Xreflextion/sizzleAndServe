package usecase.product_prices;

/**
 * The input data for the Product Prices Use Case.
 */
public class ProductPricesInputData {

    private final String name;
    private final int marginPercentage;

    public ProductPricesInputData(String name, int marginPercentage) {
        this.name = name;
        this.marginPercentage = marginPercentage;
    }

    public String getName() {
        return name;
    }

    public int getMarginPercentage() {
        return marginPercentage;
    }
}
