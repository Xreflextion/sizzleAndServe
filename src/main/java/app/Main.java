package app;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        AppBuilder appBuilder = new AppBuilder();
        JFrame application = appBuilder
                .addOfficeView()
                .addProductPricesView()
                .addBuyServingViewAndUseCase()
                .addReviewViewAndUseCase()
                .addManageWageViewAndUseCase()
                .addSimulateUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}