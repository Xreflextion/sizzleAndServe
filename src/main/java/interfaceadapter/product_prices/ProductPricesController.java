package interfaceadapter.product_prices;

import usecase.product_prices.ProductPricesInputBoundary;
import usecase.product_prices.ProductPricesInputData;

/**
 * Controller for the Product Prices Use Case.
 */
public class ProductPricesController {
    private final ProductPricesInputBoundary productPricesUseCaseInteractor;

    public ProductPricesController(ProductPricesInputBoundary productPricesUseCaseInteractor) {
        this.productPricesUseCaseInteractor = productPricesUseCaseInteractor;
    }

    /**
     * Executes the Product Prices Use Case.
     * @param name the dish's name
     * @param marginPercentage the margin percentage to apply to the dish
     */
    public void execute(String name, int marginPercentage) {
        final ProductPricesInputData productPricesInputData = new ProductPricesInputData(name, marginPercentage);

        productPricesUseCaseInteractor.execute(productPricesInputData);
    }
}
