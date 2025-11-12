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
        ProductPricesState newProductPricesState = new ProductPricesState(productPricesViewModel
                .getState()
                .getRecipes());
        newProductPricesState.setSelectedDishName(outputData.getName());
        newProductPricesState.setCurrentPrice(outputData.getNewPrice());

        productPricesViewModel.setState(newProductPricesState);
        productPricesViewModel.firePropertyChange();

        viewManagerModel.setState(productPricesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String error) {
        ProductPricesState newProductPricesState = new ProductPricesState(productPricesViewModel
                .getState()
                .getRecipes());
        newProductPricesState.setSelectedDishName(productPricesViewModel.getState().getSelectedDishName());
        newProductPricesState.setCurrentPrice(productPricesViewModel.getState().getCurrentPrice());

        productPricesViewModel.setState(newProductPricesState);
        productPricesViewModel.firePropertyChange();

    }
}
