package view;

import entity.Recipe;
import interface_adapter.product_prices.ProductPricesController;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DishPanel extends JPanel {
    private final String dishName;
    private final Recipe recipe;
    private final ProductPricesController controller;

    private final JLabel imageLabel;
    private final JLabel basePriceLabel;
    private final JLabel currentPriceLabel;
    private final JLabel marginLabel;
    private final JSlider marginSlider;
    private final JButton resetButton;

    // constants
    private static final int DEFAULT_MARGIN = 0;
    private static final int MIN_MARGIN = 0;
    private static final int MAX_MARGIN = 100;

    public DishPanel(String dishName, Recipe recipe, ProductPricesController controller) {
        this.dishName = dishName;
        this.recipe = recipe;
        this.controller = controller;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(dishName));

        basePriceLabel = new JLabel("Base Price: " + (double) recipe.getBasePrice());
        currentPriceLabel = new JLabel("Current Price: " + (double) recipe.getBasePrice()); // placeholder

        // image (placeholder)
        ImageIcon icon = new ImageIcon("src/main/resources/images/sample.jpg");
        Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(scaled));

        marginLabel = new JLabel("Profit Margin: " + DEFAULT_MARGIN + "%");
        marginSlider = new JSlider(MIN_MARGIN, MAX_MARGIN, DEFAULT_MARGIN);
        resetButton = new JButton("Reset");

        marginSlider.addChangeListener((ChangeEvent e) -> {
            int margin = marginSlider.getValue();
            marginLabel.setText("Profit Margin: " + margin + "%");
            controller.execute(dishName, margin);
        });
        resetButton.addActionListener((ActionEvent e) -> {
            marginSlider.setValue(DEFAULT_MARGIN);
            controller.execute(dishName, DEFAULT_MARGIN);
        });

        add(imageLabel);
        add(basePriceLabel);
        add(currentPriceLabel);
        add(marginLabel);
        add(marginSlider);
        add(resetButton);
    }

    /**
     * Update prices when state changes in ProductPricesViewModel.
     */
    public void updatePrices(double currentPrice) {
        currentPriceLabel.setText("Current Price: " + currentPrice);
    }
}