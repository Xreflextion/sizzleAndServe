package interface_adapter.insight;

import interface_adapter.ViewManagerModel;
import use_case.insights.performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.insights.performance_calculation.PerformanceCalculationOutputData;

public class PerformanceCalculationPresenter implements PerformanceCalculationOutputBoundary {

    private final InsightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public PerformanceCalculationPresenter(InsightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void successView(PerformanceCalculationOutputData outputData) {

        final InsightsState state = new InsightsState();

        state.setAverageRating(outputData.getAverageRating());
        state.setReviewCount(outputData.getReviewCount());
        state.setTotalRevenue(outputData.getTotalRevenue());
        state.setTotalExpenses(outputData.getTotalExpenses());
        state.setTotalProfits(outputData.getTotalProfit());
        state.setRevenueTrend(outputData.getRevenueTrend());
        state.setExpensesTrend(outputData.getExpensesTrend());
        state.setProfitTrend(outputData.getProfitTrend());

        state.setNumberOfDays(outputData.getNumberOfDays());

        viewModel.setState(state);

        viewManagerModel.setState(InsightsViewModel.VIEW_NAME);
        viewManagerModel.firePropertyChange();

    }

    @Override
    public void failView(String errorMessage) {

    }

}
