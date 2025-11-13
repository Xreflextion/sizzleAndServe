package view;

import entity.Recipe;
import entity.Pantry;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesState;
import interface_adapter.product_prices.ProductPricesViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ProductPricesViewTest {
    
    private ProductPricesView productPricesView;
    private ProductPricesViewModel productPricesViewModel;
    private ProductPricesController controller;
    private Map<String, Recipe> testRecipes;
    private Pantry testPantry;
    private Recipe pasta, burger, salad;
    
    @BeforeEach
    void setUp() {
        createDefaultRecipes();
        testPantry = new Pantry(pasta, burger, salad);

        testRecipes = new HashMap<>();
        testRecipes.put(pasta.getName(), pasta);
        testRecipes.put(burger.getName(), burger);
        testRecipes.put(salad.getName(), salad);
        
        // Set up view model with initial state
        productPricesViewModel = new ProductPricesViewModel();
        ProductPricesState initialState = new ProductPricesState(testRecipes);
        initialState.setSelectedDishName("Pasta");
        initialState.setCurrentPrice(calculateCurrentPrice("Pasta", 10));
        productPricesViewModel.setState(initialState);

        productPricesView = new ProductPricesView(productPricesViewModel, controller);
    }
    
    private void createDefaultRecipes() {
        pasta = new Recipe("Pasta", 8.99);
        burger = new Recipe("Burger", 11.99);
        salad = new Recipe("Salad", 7.99);
    }
    
    private double calculateCurrentPrice(String dishName, int marginPercentage) {
        Recipe recipe = testRecipes.get(dishName);
        return Math.round(recipe.getBasePrice() * (1 + marginPercentage / 100.0) * 100.0) / 100.0; // 2 decimal places
    }
    
    @Test
    void testViewInitializationWithTestRecipe() {
        assertEquals("product prices", productPricesView.getViewName());

        // Test that the view displays the selected recipe information
        ProductPricesState productPricesState = productPricesViewModel.getState();
        assertEquals("Pasta", productPricesState.getSelectedDishName());
        assertEquals(8.99, productPricesState.getRecipes().get("Pasta").getBasePrice());

        // Test current price calculation
        double expectedCurrentPrice = calculateCurrentPrice("Pasta", 10);
        assertEquals(expectedCurrentPrice, productPricesState.getCurrentPrice(), 0.01);
    }
    
    @Test
    void testPantryWithThreeRecipes() {
        assertEquals(pasta, testPantry.getRecipe("Pasta"));
        assertEquals(burger, testPantry.getRecipe("Burger"));
        assertEquals(salad, testPantry.getRecipe("Salad"));

        testPantry.addStock("Pasta", 10);
        assertEquals(10, testPantry.getRecipe("Pasta").getStock());
    }
    
    @Test
    void testViewUpdateWithDifferentRecipe() {
        ProductPricesState newState = new ProductPricesState(testRecipes);
        newState.setSelectedDishName("Burger");
        newState.setCurrentPrice(calculateCurrentPrice("Burger", 15));
        
        productPricesView.setFields(newState);

        assertEquals("Burger", newState.getSelectedDishName());
        assertEquals(11.99, newState.getRecipes().get("Burger").getBasePrice());
    }
    
    @Test
    void testCleanArchitectureFlow() {
        ProductPricesState testState = new ProductPricesState(testRecipes);
        testState.setSelectedDishName("Salad");
        testState.setCurrentPrice(calculateCurrentPrice("Salad", 20));
        
        productPricesViewModel.setState(testState);
        
        assertEquals("Salad", productPricesViewModel.getState().getSelectedDishName());
        assertEquals(7.99, productPricesViewModel.getState().getRecipes().get("Salad").getBasePrice());

        assertEquals(salad, testPantry.getRecipe("Salad"));
    }

    @Test
    void testViewActualDisplay() throws InterruptedException {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Test ProductPricesView");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.add(productPricesView);
            testFrame.pack();
            testFrame.setVisible(true);
            testFrame.setLocationRelativeTo(null);
        });

        // Keep the test running so you can see the view
        Thread.sleep(10000); // Shows for 10 seconds

        assertTrue(productPricesView.isDisplayable());
    }
}