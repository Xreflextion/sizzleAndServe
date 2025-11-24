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

        Recipe pizza = new Recipe("Pizza", 10);
        Recipe pasta = new Recipe("Pasta", 12);
        Recipe salad = new Recipe("Salad", 8);

        Pantry pantry = new Pantry(pizza, pasta, salad);

        ProductPricesPantryDataAccessInterface productPricesRepository = new PantryDataAccessObject(pantry);

        productPricesRepository.changePrice(pizza);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData pantry){
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals(10, pizza.getBasePrice());
                assertEquals(10, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
                assertEquals(11.5, pizza.getCurrentPrice());
                assertEquals(11.5, productPricesRepository.getPantry().getRecipe(pizza.getName()).getCurrentPrice());
            }
        };

        ProductPricesInputBoundary productPricesInteractor = new ProductPricesInteractor(productPricesRepository,
                presenter);
        productPricesInteractor.execute(productPricesInputData);
    }
}