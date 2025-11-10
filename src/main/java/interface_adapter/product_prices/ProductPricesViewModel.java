package interface_adapter.product_prices;

import interface_adapter.ViewModel;

import java.util.Collections;

/**
 * The View Model for the Logged In View.
 */
public class ProductPricesViewModel extends ViewModel<ProductPricesState> {

    public ProductPricesViewModel() {
        super("product prices");
        this.setState(new ProductPricesState(Collections.emptyMap()));
    }
}
