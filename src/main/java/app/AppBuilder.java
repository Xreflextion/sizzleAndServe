package app;

import interface_adapter.product_prices.ProductPricesViewModel;
import view.ProductPricesView;

import javax.swing.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();

    private ProductPricesView productPricesView;
    private ProductPricesViewModel productPricesViewModel;

    public AppBuilder addProductPricesView() {
        productPricesViewModel = new ProductPricesViewModel();
        productPricesView = new ProductPricesView(productPricesViewModel, null);
        cardPanel.add(productPricesView, productPricesView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}
