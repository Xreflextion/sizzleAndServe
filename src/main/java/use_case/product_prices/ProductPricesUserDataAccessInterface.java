package use_case.product_prices;

import entity.Recipe;

/**
 * The DAO interface for the Product Prices Use Case.
 */
public interface ProductPricesUserDataAccessInterface {

    /**
     * Updates the system to record this product's price.
     * @param recipe the recipe object for the dish whose price is to be updated
     */
    void changePrice(Recipe recipe);
}
