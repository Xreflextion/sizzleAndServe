package view;

import entity.Recipe;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesState;
import interface_adapter.product_prices.ProductPricesViewModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * The View for when the user is changing product prices.
 */
public class ProductPricesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "product prices";
    private final ProductPricesViewModel productPricesViewModel;
    private ProductPricesController productPricesController;

    // store each DishPanel by its dish name
    private final Map<String, DishPanel> dishPanels = new HashMap<>();

    public ProductPricesView(ProductPricesViewModel productPricesViewModel,
                             ProductPricesController productPricesController) {
        this.productPricesViewModel = productPricesViewModel;
        this.productPricesController = productPricesController;
        this.productPricesViewModel.addPropertyChangeListener(this);

        setLayout(new BorderLayout());

        // title
        JLabel titleLabel = new JLabel("Product Prices");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(titleLabel, BorderLayout.NORTH);

        // container for all dishes
        JPanel dishesPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // 3 columns
        ProductPricesState productPricesState = productPricesViewModel.getState();

        // build each DishPanel dynamically
        int index = 0;
        for (String dishName : productPricesState.getRecipes().keySet()) {
            Recipe recipe = productPricesState.getRecipes().get(dishName);
            DishPanel dishPanel = new DishPanel(dishName, recipe, productPricesController, index);
            dishPanels.put(dishName, dishPanel);
            dishesPanel.add(dishPanel);
            index++;
        }

        add(dishesPanel, BorderLayout.CENTER);
    }

    /**
     * React to a button click that results in evt.
     * @param evt the ActionEvent to react to
     */
    public void actionPerformed(ActionEvent evt) {
        System.out.println("Action performed" + evt.getActionCommand());
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final ProductPricesState state = (ProductPricesState) evt.getNewValue();
        setFields(state);
    }

    public void setFields(ProductPricesState state) {
        String selected = state.getSelectedDishName();
        if (selected != null && dishPanels.containsKey(selected)) {
            double currentPrice = state.getCurrentPrice();
            dishPanels.get(selected).updatePrices(currentPrice);
        }
    }

    public String getViewName() {
        return viewName;
    }

    public void setProductPricesController(ProductPricesController productPricesController) {
        this.productPricesController = productPricesController;
    }
}