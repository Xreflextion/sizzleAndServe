package interface_adapter.insight;

import use_case.insights_performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.insights_performance_calculation.PerformanceCalculationOutputData;


public class PerformanceCalculationPresenter implements PerformanceCalculationOutputBoundary {

    private final InsightsViewModel viewModel;

    public PerformanceCalculationPresenter(InsightsViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void successView (PerformanceCalculationOutputData outputData) {

        InsightsState state = new InsightsState();

        state.setAverageRating(outputData.getAverageRating());
        state.setReviewCount(outputData.getReviewCount());
        state.setTotalRevenue(outputData.getTotalRevenue());
        state.setTotalExpenses(outputData.getTotalExpenses());
        state.setTotalProfit(outputData.getTotalProfit());
        state.setRevenueTrend(outputData.getRevenueTrend());
        state.setExpensesTrend(outputData.getExpensesTrend());
        state.setProfitTrend(outputData.getProfitTrend());

        state.setNumberofDays(outputData.getReviewCount());

        viewModel.setState(state);

    }

    @Override
    public void failView(String errorMessage){
        InsightsState state = new InsightsState();

        state.setErrorMessage(errorMessage);

        viewModel.setState(state);

    }

}
