package use_case.product_prices;

/**
 * The Product Prices Use Case.
 */
public interface ProductPricesInputBoundary {

    /**
     * Execute the Product Prices Use Case.
     * @param productPricesInputData the input data for this use case
     */
    void execute(ProductPricesInputData productPricesInputData);
}
