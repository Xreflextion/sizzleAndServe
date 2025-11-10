package interface_adapter.product_prices;

import interface_adapter.ViewManagerModel;
import use_case.product_prices.ProductPricesOutputBoundary;
import use_case.product_prices.ProductPricesOutputData;

/**
 * The Presenter for the Product Prices Use Case.
 */
public class ProductPricesPresenter implements ProductPricesOutputBoundary{

    private final ProductPricesViewModel productPricesViewModel;
    private final ViewManagerModel viewManagerModel;

    public ProductPricesPresenter(ProductPricesViewModel productPricesViewModel,
                                  ViewManagerModel viewManagerModel) {
        this.productPricesViewModel = productPricesViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void prepareSuccessView(ProductPricesOutputData outputData) {
        productPricesViewModel.setState(new ProductPricesState(productPricesViewModel.getState().getRecipes()));
        productPricesViewModel.getState().setSelectedDishName(outputData.getName());
        productPricesViewModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        productPricesViewModel.setState(new ProductPricesState(productPricesViewModel.getState().getRecipes()));
        productPricesViewModel.getState().setSelectedDishName(null);
        productPricesViewModel.firePropertyChange();
    }
}
