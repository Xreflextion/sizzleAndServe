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

/**
 * The View for when the user is changing product prices.
 */
public class ProductPricesView extends JPanel implements ActionListener, PropertyChangeListener {

    private final String viewName = "product prices";
    private final ProductPricesViewModel productPricesViewModel;
    private ProductPricesController productPricesController = null;

    private final JLabel nameLabel;
    private final JLabel basePriceLabel;
    private final JLabel currentPriceLabel;

    private final int DEFAULT_MARGIN = 10;
    private final int MIN_MARGIN = 0;
    private final int MAX_MARGIN = 100;
    private final int MINOR_TICK_SPACING = 5;
    private final int MAJOR_TICK_SPACING = 10;

    private final JLabel marginLabel;
    private final JButton marginReset;
    private final JSlider marginSlider = new JSlider(JSlider.HORIZONTAL, MIN_MARGIN, MAX_MARGIN, DEFAULT_MARGIN);

    public ProductPricesView(ProductPricesViewModel productPricesViewModel) {
        this.productPricesViewModel = productPricesViewModel;
        this.productPricesViewModel.addPropertyChangeListener(this);

        final JLabel titleLabel = new JLabel("Product Prices");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JPanel informationPanel = new JPanel();
        informationPanel.setLayout(new BoxLayout(informationPanel, BoxLayout.Y_AXIS));
        nameLabel = new JLabel("Name: "
                + (productPricesViewModel.getState() != null && productPricesViewModel.getState().getSelectedDishName()
                != null ? productPricesViewModel.getState().getSelectedDishName() : "N/A"));
        informationPanel.add(nameLabel);

        String selectedDishName = productPricesViewModel.getState().getSelectedDishName();
        Recipe selectedRecipe = productPricesViewModel.getState().getRecipes().get(selectedDishName);
        basePriceLabel = new JLabel("Base Price: " 
                + (selectedRecipe != null ? selectedRecipe.getBasePrice() : "N/A"));
        currentPriceLabel = new JLabel("Current Price: " 
                + (productPricesViewModel.getState() != null ? productPricesViewModel.getState().getCurrentPrice() : "N/A"));
        informationPanel.add(basePriceLabel);
        informationPanel.add(currentPriceLabel);

        final JPanel marginPanel = new JPanel();
        marginPanel.setLayout(new BoxLayout(marginPanel, BoxLayout.Y_AXIS));
        marginLabel = new JLabel("Profit Margin: " + marginSlider.getValue() + "%");
        marginPanel.add(marginLabel);
        marginReset = new JButton("Reset");
        marginPanel.add(marginReset);
        marginSlider.setMinorTickSpacing(MINOR_TICK_SPACING);
        marginSlider.setMajorTickSpacing(MAJOR_TICK_SPACING);
        marginSlider.setPaintTicks(true);
        marginSlider.setPaintLabels(true);
        marginPanel.add(marginSlider);

        marginReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                marginSlider.setValue(DEFAULT_MARGIN);
            }
        });
        marginSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                marginLabel.setText("Profit Margin: " + marginSlider.getValue() + "%");
            }
        });

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(informationPanel);
        this.add(marginPanel);
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
        nameLabel.setText("Name: " + state.getSelectedDishName());
        basePriceLabel.setText("Base Price: " + state.getRecipes().get(state.getSelectedDishName()).getBasePrice());
        currentPriceLabel.setText("Current Price: " + state.getCurrentPrice());
    }

    public String getViewName() {
        return viewName;
    }

    public void setProductPricesController(ProductPricesController productPricesController) {
        this.productPricesController = productPricesController;
    }
}