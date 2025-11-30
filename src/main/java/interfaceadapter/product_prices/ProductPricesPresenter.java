package interfaceadapter.product_prices;

import interfaceadapter.ViewManagerModel;
import usecase.product_prices.ProductPricesOutputBoundary;
import usecase.product_prices.ProductPricesOutputData;

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
    public void present(ProductPricesOutputData outputData) {
        ProductPricesState newProductPricesState = productPricesViewModel.getState();
        newProductPricesState.setSelectedDishName(outputData.getName());
        newProductPricesState.setCurrentPrice(outputData.getNewPrice());

        productPricesViewModel.setState(newProductPricesState);
        productPricesViewModel.firePropertyChange();

        viewManagerModel.setState(productPricesViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }
}
