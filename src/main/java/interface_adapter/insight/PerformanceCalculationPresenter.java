package interface_adapter.insight;

import use_case.insights_performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.insights_performance_calculation.PerformanceCalculationOutputData;
import view.DrillDownView;
import view.InsightsView;
import interface_adapter.ViewManagerModel;


public class PerformanceCalculationPresenter implements PerformanceCalculationOutputBoundary {

    private final InsightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public PerformanceCalculationPresenter(InsightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void successView (PerformanceCalculationOutputData outputData) {

        InsightsState state = new InsightsState();

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

        viewManagerModel.setState(InsightsView.viewName);
        viewManagerModel.firePropertyChange();

    }

    @Override
    public void failView(String errorMessage){
//        InsightsState state = new InsightsState();
//
//        state.setErrorMessage(errorMessage);
//
//        viewModel.setState(state);

    }

}
