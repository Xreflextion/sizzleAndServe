package use_case.product_prices;

/**
 * The output boundary for the Product Prices Use Case.
 */
public interface ProductPricesOutputBoundary {

    /**
     * Prepares the view for the Product Prices Use Case.
     *
     * @param outputData the output data
     */
    void present(ProductPricesOutputData outputData);
}
