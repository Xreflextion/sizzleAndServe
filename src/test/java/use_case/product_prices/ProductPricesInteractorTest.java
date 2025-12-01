package use_case.product_prices;

import data_access.PantryDataAccessObject;
import entity.Pantry;
import entity.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductPricesInteractorTest {

    private ProductPricesPantryDataAccessInterface productPricesRepository;
    private ProductPricesInputBoundary productPricesInteractor;
    private Recipe pizza;
    private Recipe pasta;
    private Recipe salad;

    @BeforeEach
    void setUp() {
        pizza = new Recipe("Pizza", 10);
        pasta = new Recipe("Pasta", 12);
        salad = new Recipe("Salad", 8);
        Pantry pantry = new Pantry(pizza, pasta, salad);
        productPricesRepository = new PantryDataAccessObject(pantry);
    }

    @Test
    void pricesTest() {
        ProductPricesInputData productPricesInputData = new ProductPricesInputData("Pizza", 15);
        productPricesRepository.changePrice(pizza);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData){
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals("Pizza", outputData.getName());
                assertEquals(10, pizza.getBasePrice());
                assertEquals(10, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
                assertEquals(11.5, pizza.getCurrentPrice());
                assertEquals(11.5, outputData.getNewPrice());
                assertEquals(11.5, productPricesRepository.getPantry().getRecipe(pizza.getName()).getCurrentPrice());
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter);
        productPricesInteractor.execute(productPricesInputData);
    }

    @Test
    void zeroMarginTest() {
        ProductPricesInputData inputData = new ProductPricesInputData("Pizza", 0);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals("Pizza", outputData.getName());
                assertEquals(10, pizza.getBasePrice());
                assertEquals(10, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
                assertEquals(10.0, pizza.getCurrentPrice());
                assertEquals(10.0, outputData.getNewPrice());
                assertEquals(10.0, productPricesRepository.getPantry().getRecipe(pizza.getName()).getCurrentPrice());
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter);
        productPricesInteractor.execute(inputData);
    }

    @Test
    void negativeMarginTest() {
        ProductPricesInputData inputData = new ProductPricesInputData("Pizza", -20);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                fail("Should not reach presenter when exception is thrown");
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter);
        
        // Test that a negative margin input throws IllegalArgumentException
        assertThrows(IllegalArgumentException.class, () -> {
            productPricesInteractor.execute(inputData);
        });
    }

    @Test
    void largeMarginTest() {
        ProductPricesInputData inputData = new ProductPricesInputData("Pizza", 100);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals("Pizza", outputData.getName());
                assertEquals(10, pizza.getBasePrice());
                assertEquals(10, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
                assertEquals(20.0, pizza.getCurrentPrice());
                assertEquals(20.0, outputData.getNewPrice());
                assertEquals(20.0, productPricesRepository.getPantry().getRecipe(pizza.getName()).getCurrentPrice());
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter);
        productPricesInteractor.execute(inputData);
    }

    @Test
    void multipleMarginChangesTest() {
        // First margin change
        ProductPricesInputData inputData1 = new ProductPricesInputData("Salad", 50);

        ProductPricesOutputBoundary presenter1 = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Salad", salad.getName());
                assertEquals("Salad", productPricesRepository.getPantry().getRecipe(salad.getName()).getName());
                assertEquals("Salad", outputData.getName());
                assertEquals(8, salad.getBasePrice());
                assertEquals(8, productPricesRepository.getPantry().getRecipe(salad.getName()).getBasePrice());
                assertEquals(12.0, salad.getCurrentPrice());
                assertEquals(12.0, outputData.getNewPrice());
                assertEquals(12.0, productPricesRepository.getPantry().getRecipe(salad.getName()).getCurrentPrice());
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter1);
        productPricesInteractor.execute(inputData1);

        // Second margin change on the same recipe (ensuring calculations are from basePrice and not currentPrice)
        ProductPricesInputData inputData2 = new ProductPricesInputData("Salad", 100);

        ProductPricesOutputBoundary presenter2 = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Salad", salad.getName());
                assertEquals("Salad", productPricesRepository.getPantry().getRecipe(salad.getName()).getName());
                assertEquals("Salad", outputData.getName());
                assertEquals(8, salad.getBasePrice());
                assertEquals(8, productPricesRepository.getPantry().getRecipe(salad.getName()).getBasePrice());
                assertEquals(16.0, salad.getCurrentPrice());
                assertEquals(16.0, outputData.getNewPrice());
                assertEquals(16.0, productPricesRepository.getPantry().getRecipe(salad.getName()).getCurrentPrice());
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter2);
        productPricesInteractor.execute(inputData2);
    }

    @Test
    void fractionalMarginTest() {
        ProductPricesInputData inputData = new ProductPricesInputData("Pizza", 33);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals("Pizza", outputData.getName());
                assertEquals(10, pizza.getBasePrice());
                assertEquals(10, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
                assertEquals(13.3, pizza.getCurrentPrice(), 0.1);
                assertEquals(13.3, outputData.getNewPrice(),0.1);
                assertEquals(13.3, productPricesRepository.getPantry().getRecipe(pizza.getName()).getCurrentPrice(),
                        0.1);
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter);
        productPricesInteractor.execute(inputData);
    }

    @Test
    void allRecipesIndependentTest() {
        // Test that all three recipes can be updated independently without affecting each other
        ProductPricesInputData pizzaInput = new ProductPricesInputData("Pizza", 20);
        ProductPricesInputData pastaInput = new ProductPricesInputData("Pasta", 30);
        ProductPricesInputData saladInput = new ProductPricesInputData("Salad", 40);

        // Update pizza
        ProductPricesOutputBoundary pizzaPresenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Pizza", pizza.getName());
                assertEquals("Pizza", productPricesRepository.getPantry().getRecipe(pizza.getName()).getName());
                assertEquals("Pizza", outputData.getName());
                assertEquals(10, pizza.getBasePrice());
                assertEquals(10, productPricesRepository.getPantry().getRecipe(pizza.getName()).getBasePrice());
                assertEquals(12.0, pizza.getCurrentPrice());
                assertEquals(12.0, outputData.getNewPrice());
                assertEquals(12.0, productPricesRepository.getPantry().getRecipe(pizza.getName()).getCurrentPrice());
            }
        };
        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, pizzaPresenter);
        productPricesInteractor.execute(pizzaInput);

        // Update pasta
        ProductPricesOutputBoundary pastaPresenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Pasta", pasta.getName());
                assertEquals("Pasta", productPricesRepository.getPantry().getRecipe(pasta.getName()).getName());
                assertEquals("Pasta", outputData.getName());
                assertEquals(12, pasta.getBasePrice());
                assertEquals(12, productPricesRepository.getPantry().getRecipe(pasta.getName()).getBasePrice());
                assertEquals(15.6, pasta.getCurrentPrice());
                assertEquals(15.6, outputData.getNewPrice());
                assertEquals(15.6, productPricesRepository.getPantry().getRecipe(pasta.getName()).getCurrentPrice());
            }
        };
        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, pastaPresenter);
        productPricesInteractor.execute(pastaInput);

        // Update salad
        ProductPricesOutputBoundary saladPresenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                assertEquals("Salad", salad.getName());
                assertEquals("Salad", productPricesRepository.getPantry().getRecipe(salad.getName()).getName());
                assertEquals("Salad", outputData.getName());
                assertEquals(8, salad.getBasePrice());
                assertEquals(8, productPricesRepository.getPantry().getRecipe(salad.getName()).getBasePrice());
                assertEquals(11.2, salad.getCurrentPrice());
                assertEquals(11.2, outputData.getNewPrice());
                assertEquals(11.2, productPricesRepository.getPantry().getRecipe(salad.getName()).getCurrentPrice());
            }
        };
        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, saladPresenter);
        productPricesInteractor.execute(saladInput);
    }

    @Test
    void nonExistentRecipeTest() {
        ProductPricesInputData inputData = new ProductPricesInputData("Burger", 15);

        ProductPricesOutputBoundary presenter = new ProductPricesOutputBoundary() {
            @Override
            public void present(ProductPricesOutputData outputData) {
                fail("Should not reach presenter when recipe doesn't exist");
            }
        };

        productPricesInteractor = new ProductPricesInteractor(productPricesRepository, presenter);
        
        // This should throw an exception when trying to get a non-existent recipe
        assertThrows(NullPointerException.class, () -> {
            productPricesInteractor.execute(inputData);
        });
    }
}