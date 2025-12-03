package interface_adapter.insight;

import interface_adapter.ViewManagerModel;
import use_case.insights.day_calculation.DayInsightsOutputBoundary;
import use_case.insights.day_calculation.DayInsightsOutputData;
import view.DrillDownView;

public class DayInsightsPresenter implements DayInsightsOutputBoundary {
    private final InsightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public DayInsightsPresenter(InsightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void successView(DayInsightsOutputData outputData) {
        final InsightsState state = viewModel.getState();

        state.setDayNumber(outputData.getDayNumber());
        state.setDayRating(outputData.getDayRating());
        state.setDayRevenue(outputData.getDayRevenue());
        state.setDayExpenses(outputData.getDayExpenses());
        state.setDayProfits(outputData.getDayProfit());

        viewModel.setState(state);

        System.out.print("Presenter: Switching to Drill Down View");
        viewManagerModel.setState(DrillDownView.VIEW_NAME);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void failView(String errorMessage) {

    }

}
