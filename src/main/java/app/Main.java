package app;

import javax.swing.JFrame;

/**
 * Sizzle and Serve allows users to make business decisions, simulate, and evaluate the decisions they've made.
 */
public class Main {
    /**
     * The main entry point of the project.
     * @param args commandline arguments to be ignored.
     */
    public static void main(String[] args) {
        final AppBuilder appBuilder = new AppBuilder();
        final JFrame application = appBuilder
                .addOfficeView()
                .addManageWageViewAndUseCase()
                .addProductPricesView()
                .addBuyServingViewAndUseCase()
                .addReviewViewAndUseCase()
                .addSimulateUseCase()
                .build();

        application.pack();
        application.setLocationRelativeTo(null);
        application.setVisible(true);
    }
}
