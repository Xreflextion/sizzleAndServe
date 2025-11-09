package use_case.product_prices;

/**
 * The output boundary for the Product Prices Use Case.
 */
public interface ProductPricesOutputBoundary {
    /**
     * Prepares the success view for the Product Prices Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(ProductPricesOutputData outputData);

    /**
     * Prepares the failure view for the Product Prices Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
