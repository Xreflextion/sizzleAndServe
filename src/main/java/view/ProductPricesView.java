package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesState;
import interface_adapter.product_prices.ProductPricesViewModel;

/**
 * The View for when the user is changing product prices.
 */
public class ProductPricesView extends JPanel implements ActionListener, PropertyChangeListener {

    public static final int FONT_SIZE = 18;
    public static final int ROWS = 1;
    public static final int COLS = 3;
    public static final int HORIZONTAL_GAP = 10;
    public static final int VERTICAL_GAP = 10;
    public static final String VIEW_NAME = "product prices";
    private final ProductPricesViewModel productPricesViewModel;
    private final ViewManagerModel viewManagerModel;

    // store each DishPanel by its dish name
    private final Map<String, DishPanel> dishPanels = new HashMap<>();

    public ProductPricesView(ProductPricesViewModel productPricesViewModel,
                             ProductPricesController productPricesController,
                             ViewManagerModel viewManagerModel) {
        this.productPricesViewModel = productPricesViewModel;
        this.productPricesViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        setLayout(new BorderLayout());

        // title
        final JLabel titleLabel = new JLabel("Product Prices");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, FONT_SIZE));
        add(titleLabel, BorderLayout.NORTH);

        // container for all dishes
        final JPanel dishesPanel = new JPanel(new GridLayout(ROWS, COLS, HORIZONTAL_GAP, VERTICAL_GAP));
        final ProductPricesState productPricesState = productPricesViewModel.getState();

        // build each DishPanel dynamically
        for (String dishName : productPricesState.getRecipes().keySet()) {
            final Recipe recipe = productPricesState.getRecipes().get(dishName);
            final DishPanel dishPanel = new DishPanel(dishName, recipe, productPricesController);
            dishPanels.put(dishName, dishPanel);
            dishesPanel.add(dishPanel);
        }

        add(dishesPanel, BorderLayout.CENTER);

        final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton backButton = new JButton("Office");

        backButton.addActionListener(evt -> {
            this.viewManagerModel.setState(OfficeViewModel.VIEW_NAME);
            this.viewManagerModel.firePropertyChange();
        });

        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    /**
     * React to a button click that results in evt.
     *
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

    /**
     * Updates the fields in the current view based on the provided state information.
     *
     * @param state the ProductPricesState object containing the current selected dish name and its price.
     */
    public void setFields(ProductPricesState state) {
        final String selected = state.getSelectedDishName();
        if (selected != null && dishPanels.containsKey(selected)) {
            final double currentPrice = state.getCurrentPrice();
            dishPanels.get(selected).updatePrices(currentPrice);
        }
    }

    public String getViewName() {
        return VIEW_NAME;
    }
}
