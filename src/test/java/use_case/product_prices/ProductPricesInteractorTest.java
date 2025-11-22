package use_case.product_prices;

import data_access.PantryDataAccessObject;
import entity.Pantry;
import entity.Recipe;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductPricesInteractorTest {

    @Test
    void pricesTest() {
        ProductPricesInputData productPricesInputData = new ProductPricesInputData("Pizza", 15);
        ProductPricesPantryDataAccessInterface productPricesRepository = new PantryDataAccessObject();

        Recipe pizza = new Recipe("Pizza", 10);
        Recipe pasta = new Recipe("Pasta", 12);
        Recipe salad = new Recipe("Salad", 8);

        Pantry pantry = new Pantry(pizza, pasta, salad);

        productPricesRepository.changePrice(pizza);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData pantry){
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals(15, pizza.getBasePrice());
                assertEquals(15, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
            }
        };

        ProductPricesInputBoundary productPricesInteractor = new ProductPricesInteractor(productPricesRepository,
                presenter);
        productPricesInteractor.execute(productPricesInputData);
    }
}
