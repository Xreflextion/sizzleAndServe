package interfaceadapter.product_prices;

import entity.Recipe;
import interfaceadapter.ViewModel;

import java.util.Map;

/**
 * The View Model for the Product Prices View.
 */
public class ProductPricesViewModel extends ViewModel<ProductPricesState> {

    public static final String VIEW_NAME = "product prices";

    public ProductPricesViewModel(Map<String, Recipe> recipes) {
        super(VIEW_NAME);
        this.setState(new ProductPricesState(recipes));
    }
}
