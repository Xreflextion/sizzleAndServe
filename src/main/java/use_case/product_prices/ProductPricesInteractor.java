package use_case.product_prices;

import entity.Recipe;
import entity.Pantry;

/**
 * The Product Prices Interactor.
 */
public class ProductPricesInteractor implements ProductPricesInputBoundary{
    private final ProductPricesUserDataAccessInterface userDataAccessObject;
    private final ProductPricesOutputBoundary userPresenter;
    private final Pantry pantry;

    public ProductPricesInteractor(ProductPricesUserDataAccessInterface productPricesUserDataAccessInterface,
                                   ProductPricesOutputBoundary productPricesOutputBoundary,
                                   Pantry pantry) {
        this.userDataAccessObject = productPricesUserDataAccessInterface;
        this.userPresenter = productPricesOutputBoundary;
        this.pantry = pantry;
    }

    @Override
    public void execute(ProductPricesInputData productPricesInputData) {
        if (pantry.getRecipe(productPricesInputData.getName()) == null) {
            userPresenter.prepareFailView("The dish you are trying to change the price of" +
                    " does not exist in the Pantry.");
        }
        else if (productPricesInputData.getCurrentPrice() < 0) {
            userPresenter.prepareFailView("The price cannot be negative.");
        }
        else {
            final Recipe recipe = pantry.getRecipe(productPricesInputData.getName());
            recipe.setCurrentPrice(productPricesInputData.getCurrentPrice());

            userDataAccessObject.changePrice(recipe);

            final ProductPricesOutputData productPricesOutputData = new ProductPricesOutputData(recipe.getName());
            userPresenter.prepareSuccessView(productPricesOutputData);
        }
    }
}
