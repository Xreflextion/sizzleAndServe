package use_case;

import entity.Pantry;
import entity.Recipe;
import org.junit.jupiter.api.Test;
import use_case.product_prices.ProductPricesInputData;
import use_case.product_prices.ProductPricesInteractor;
import use_case.product_prices.ProductPricesOutputData;

import static org.junit.jupiter.api.Assertions.*;

public class ProductPricesUseCaseTest {

    @Test
    public void testSetMarginUpdatesPrice() {
        TestProductPricesDataAccess testProductPricesDataAccess = new TestProductPricesDataAccess();
        TestOutputBoundary testOutputBoundary = new TestOutputBoundary();

        // Create a pantry and add a recipe
        Recipe pizza = new Recipe("Pizza", 8.99);
        Pantry pantry = new Pantry(pizza, pizza, pizza);

        // Create interactor
        ProductPricesInteractor interactor = new ProductPricesInteractor(testProductPricesDataAccess,
                testOutputBoundary, pantry);
        ProductPricesInputData input = new ProductPricesInputData("Pizza", 20);
        interactor.execute(input);

        // Assert DAO updated
        assertTrue(testProductPricesDataAccess.containsRecipe("Pizza"));
        Recipe savedRecipe = testProductPricesDataAccess.getRecipe("Pizza");
        assertEquals(8.99, savedRecipe.getBasePrice());

        double expectedPrice = Math.round(8.99 * (1 + 20 / 100.0) * 100.0) / 100.0;
        assertEquals(expectedPrice, savedRecipe.getCurrentPrice());

        assertTrue(testOutputBoundary.wasSuccessCalled());
        ProductPricesOutputData outputData = testOutputBoundary.lastSuccessOutput;
        assertEquals("Pizza", outputData.getName());
        assertEquals(expectedPrice, outputData.getNewPrice());
    }

    @Test
    public void testNonexistentDishFails() {
        TestProductPricesDataAccess dataAccess = new TestProductPricesDataAccess();
        TestOutputBoundary output = new TestOutputBoundary();
        Pantry pantry = new Pantry(); // empty pantry
        ProductPricesInteractor interactor = new ProductPricesInteractor(dataAccess, output, pantry);

        // Non-existent recipe
        ProductPricesInputData input = new ProductPricesInputData("Burger", 10);
        interactor.execute(input);

        assertTrue(output.wasFailCalled());
        assertEquals("The dish you are trying to change the price of does not exist in the Pantry.",
                output.lastFailMessage);
    }

    @Test
    public void testNegativeMarginFails() {
        TestProductPricesDataAccess dataAccess = new TestProductPricesDataAccess();
        TestOutputBoundary output = new TestOutputBoundary();
        Recipe pizza = new Recipe("Pizza", 8.99);
        Pantry pantry = new Pantry(pizza, pizza, pizza);

        ProductPricesInteractor interactor = new ProductPricesInteractor(dataAccess, output, pantry);

        // Negative margin
        ProductPricesInputData input = new ProductPricesInputData("Pizza", -5);
        interactor.execute(input);

        // Assert failure was reported
        assertTrue(output.wasFailCalled());
        assertEquals("The price cannot be negative.", output.lastFailMessage);
    }
}

