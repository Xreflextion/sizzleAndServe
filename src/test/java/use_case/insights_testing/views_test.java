package use_case.insights_testing;

import interface_adapter.insight.InsightsViewModel;
import interface_adapter.insight.PerformanceCalculationController;
import view.InsightsView;
import interface_adapter.insight.InsightsState;

import use_case.insights_performance_calculation.PerformanceCalculationInputBoundary;

import javax.swing.*;

public class views_test {

    // ---- Fake interactor ----
    // so you can display the page without wiring the whole CA stack.
    private static class FakeInteractor implements PerformanceCalculationInputBoundary {
        private final InsightsViewModel vm;

        FakeInteractor(InsightsViewModel vm) {
            this.vm = vm;
        }

        @Override
        public void calculateInsights() {
            // Simulate output from real use case:
            InsightsState state = new InsightsState();
            state.setTotalRevenue(300);
            state.setTotalExpenses(150);
            state.setTotalProfits(150);
            state.setAverageRating(4.6);

            // Example trends (for drill down buttons)
            state.setNumberOfDays(3);

            // You can also set:
            // state.setRevenueTrend(List.of(100.0, 120.0, 80.0));
            // state.setExpensesTrend(List.of(50.0, 60.0, 40.0));
            // state.setProfitTrend(List.of(50.0, 60.0, 40.0));

            vm.setState(state);
        }
    }

    // ---- Fake controller ----
    private static class FakeController extends PerformanceCalculationController {
        private final PerformanceCalculationInputBoundary interactor;

        public FakeController(PerformanceCalculationInputBoundary interactor) {
            super(interactor);
            this.interactor = interactor;
        }

        // Your view calls controller.displayInsights(),
        // so we need to implement it:
        @Override
        public void displayInsights() {
            interactor.calculateInsights();
        }
    }

    // ---- MAIN METHOD ----
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            // ViewModel
            InsightsViewModel viewModel = new InsightsViewModel();

            // Fake interactor → passes dummy state into viewModel
            FakeInteractor fakeInteractor = new FakeInteractor(viewModel);

            // Fake controller → triggers fake interactor
            FakeController fakeController = new FakeController(fakeInteractor);

            // Build the view
            InsightsView view = new InsightsView(viewModel, fakeController);

            // Show window
            JFrame frame = new JFrame("InsightsView Test Window");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(view);
            frame.setSize(900, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
