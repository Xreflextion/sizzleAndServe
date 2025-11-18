package app;

import data_access.PantryDataAccessObject;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesPresenter;
import interface_adapter.product_prices.ProductPricesState;
import interface_adapter.product_prices.ProductPricesViewModel;
import view.ProductPricesView;
import use_case.product_prices.ProductPricesInteractor;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ProductPricesAppBuilder {

    public static void main(String[] args) {
        PantryDataAccessObject pantryDataAccessObject = new PantryDataAccessObject();
        ProductPricesViewModel productPricesViewModel = new ProductPricesViewModel();
        ViewManagerModel viewManagerModel = new ViewManagerModel();
        String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();
        int[] basePrices = new int[dishNames.length];
        double[] currentPrices = new double[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            basePrices[i] = pantryDataAccessObject.getPantry().getRecipe(dishNames[i]).getBasePrice();
            currentPrices[i] = basePrices[i];
        }

        Map<String, Recipe> recipes = new HashMap<>();
        for (int i = 0; i < dishNames.length; i++) {
            recipes.put(dishNames[i], new Recipe(dishNames[i], basePrices[i]));
        }

        ProductPricesState productPricesState = new ProductPricesState(recipes);
        productPricesViewModel.setState(productPricesState);
        ProductPricesPresenter productPricesPresenter = new ProductPricesPresenter(productPricesViewModel,
                viewManagerModel);
        ProductPricesInteractor productPricesInteractor = new ProductPricesInteractor(pantryDataAccessObject,
                productPricesPresenter);
        ProductPricesController productPricesController = new ProductPricesController(productPricesInteractor);

        ProductPricesView productPricesView = new ProductPricesView(productPricesViewModel, productPricesController);
        productPricesViewModel.addPropertyChangeListener(productPricesView);

        JFrame frame = new JFrame("Sizzle and Serve");
        frame.setContentPane(productPricesView);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
