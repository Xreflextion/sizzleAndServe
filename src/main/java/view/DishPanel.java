package view;

import java.awt.Image;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;

import constants.Constants;
import entity.Recipe;
import interface_adapter.product_prices.ProductPricesController;

public class DishPanel extends JPanel {
    public static final int DEFAULT_MARGIN = 0;
    public static final int MIN_MARGIN = 0;
    public static final int MAX_MARGIN = 100;
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;
    public static final int PERCENTAGE = 100;

    private final String dishName;
    private final Recipe recipe;
    private final ProductPricesController controller;

    private final JLabel imageLabel;
    private final JLabel basePriceLabel;
    private final JLabel currentPriceLabel;
    private final JLabel marginLabel;
    private final JSlider marginSlider;
    private final JButton resetButton;

    public DishPanel(String dishName, Recipe recipe, ProductPricesController controller) {
        this.dishName = dishName;
        this.recipe = recipe;
        this.controller = controller;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder(dishName));

        basePriceLabel = new JLabel("Base Price: " + (double) recipe.getBasePrice());
        currentPriceLabel = new JLabel("Current Price: " + recipe.getCurrentPrice());

        final String imagePath = Constants.DIR_PATH
                + dishName.replaceAll(Constants.REGEX_CHARACTERS, Constants.REPLACEMENT_CHARACTER)
                + Constants.FILE_TYPE;
        final ImageIcon icon = new ImageIcon(imagePath);
        final Image scaled = icon.getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);
        imageLabel = new JLabel(new ImageIcon(scaled));

        marginSlider = new JSlider(MIN_MARGIN, MAX_MARGIN, DEFAULT_MARGIN);
        marginSlider.setValue((int) (PERCENTAGE * (recipe.getCurrentPrice() / recipe.getBasePrice() - 1)));
        marginLabel = new JLabel("Profit Margin: " + marginSlider.getValue() + "%");
        resetButton = new JButton("Reset");

        marginSlider.addChangeListener((ChangeEvent event) -> {
            final int margin = marginSlider.getValue();
            marginLabel.setText("Profit Margin: " + margin + "%");
            controller.execute(dishName, margin);
        });
        resetButton.addActionListener((ActionEvent event) -> {
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
     * @param currentPrice the current price of the dish that is being updated.
     */
    public void updatePrices(double currentPrice) {
        currentPriceLabel.setText("Current Price: " + currentPrice);
    }
}
