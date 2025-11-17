package use_case.product_prices;

import entity.Recipe;
import entity.Pantry;

/**
 * The Product Prices Interactor.
 */
public class ProductPricesInteractor implements ProductPricesInputBoundary{
    private final ProductPricesPantryDataAccessInterface userDataAccessObject;
    private final ProductPricesOutputBoundary userPresenter;
    private Pantry pantry;

    public ProductPricesInteractor(ProductPricesPantryDataAccessInterface productPricesPantryDataAccessInterface,
                                   ProductPricesOutputBoundary productPricesOutputBoundary,
                                   Pantry pantry) {
        this.userDataAccessObject = productPricesPantryDataAccessInterface;
        this.userPresenter = productPricesOutputBoundary;
        this.pantry = pantry;
    }

    @Override
    public void execute(ProductPricesInputData productPricesInputData) {
        pantry = userDataAccessObject.getPantry();
        final Recipe recipe = pantry.getRecipe(productPricesInputData.getName());
        recipe.applyMargin(productPricesInputData.getMarginPercentage());

        userDataAccessObject.changePrice(recipe);

        final ProductPricesOutputData productPricesOutputData = new ProductPricesOutputData(recipe.getName(),
                recipe.getCurrentPrice());
        userPresenter.present(productPricesOutputData);
    }
}
