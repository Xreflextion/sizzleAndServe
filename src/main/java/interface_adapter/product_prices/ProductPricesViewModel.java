package interface_adapter.product_prices;

import java.util.Map;

import entity.Recipe;
import interface_adapter.ViewModel;

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
