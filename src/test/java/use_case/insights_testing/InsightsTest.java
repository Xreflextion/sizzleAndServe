//package use_case.insights_testing;
//
//import interface_adapter.ViewManagerModel;
//import interface_adapter.insight.InsightsState;
//import interface_adapter.insight.InsightsViewModel;
//import interface_adapter.insight.PerformanceCalculationController;
//import use_case.insights.day_calculation.DayInsightsInputBoundary;
//import use_case.insights.performance_calculation.PerformanceCalculationInputBoundary;
//import view.InsightsView;
//
//import javax.swing.*;
//
///**
// * Manual UI test harness for InsightsView.
// *
// * Run this class' main() from IntelliJ to open a window that shows
// * InsightsView populated with dummy data.
// */
//public class InsightsViewTest {
//
//    /**
//     * Fake interactor for the Performance Calculation use case.
//     * It just stuffs some dummy data into the InsightsViewModel.
//     */
//    private static class TestPerformanceInteractor implements PerformanceCalculationInputBoundary {
//        private final InsightsViewModel viewModel;
//
//        TestPerformanceInteractor(InsightsViewModel viewModel) {
//            this.viewModel = viewModel;
//        }
//
//        @Override
//        public void calculateInsights() {
//            InsightsState state = new InsightsState();
//
//            // Dummy numbers so you can visually check that the UI updates
//            state.setTotalRevenue(300.0);
//            state.setTotalExpenses(150.0);
//            state.setTotalProfits(150.0);
//            state.setAverageRating(4.6);
//            state.setNumberOfDays(3);
//
//            viewModel.setState(state);
//            viewModel.firePropertyChange();  // IMPORTANT: notify the view
//        }
//    }
//
//    /**
//     * Fake interactor for the Day Insights use case.
//     * Does nothing; it just satisfies the controller's constructor.
//     */
//    private static class FakeDayInsightsInteractor implements DayInsightsInputBoundary {
//
//        @Override
//        public void calculateDayInsights(int dayNumber) {
//            // No-op for this manual test
//        }
//    }
//
//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//
//            // --- View models & view manager ---
//            InsightsViewModel insightsViewModel = new InsightsViewModel();
//            ViewManagerModel viewManagerModel = new ViewManagerModel();
//
//            // --- Fake interactors ---
//            PerformanceCalculationInputBoundary perfInteractor =
//                    new TestPerformanceInteractor(insightsViewModel);
//
//            DayInsightsInputBoundary dayInteractor =
//                    new FakeDayInsightsInteractor();
//
//            // --- Controller wired with fake interactors ---
//
//            PerformanceCalculationController controller =
//                    new PerformanceCalculationController(
//                            perfInteractor,
//                            dayInteractor,
//                            insightsViewModel,  // or viewManagerModel first, depending on your ctor
//                            viewManagerModel
//                    );
//
//            // Preload dummy data so the view is already populated on open
//            controller.displayInsights();
//
//            // --- Build the Insights view ---
//            // If your InsightsView constructor has a different signature
//            // (e.g. needs ViewManagerModel too), adjust this line accordingly.
//            InsightsView view = new InsightsView(
//                    insightsViewModel,
//                    viewManagerModel,
//                    controller
//            );
//
//            // --- Show window ---
//            JFrame frame = new JFrame("InsightsView Test Window");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setContentPane(view);
//            frame.setSize(900, 600);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
//}
