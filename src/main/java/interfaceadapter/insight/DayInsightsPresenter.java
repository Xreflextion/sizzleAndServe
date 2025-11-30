package interfaceadapter.insight;

import usecase.insights_day_calculation.DayInsightsOutputBoundary;
import usecase.insights_day_calculation.DayInsightsOutputData;
import view.DrillDownView;
import interfaceadapter.ViewManagerModel;

public class DayInsightsPresenter implements DayInsightsOutputBoundary {
    private final InsightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    public DayInsightsPresenter(InsightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;
    }

    @Override
    public void successView (DayInsightsOutputData outputData){
        InsightsState state = viewModel.getState();

        state.setDayNumber(outputData.getDayNumber());
        state.setDayRating(outputData.getDayRating());
        state.setDayRevenue(outputData.getDayRevenue());
        state.setDayExpenses(outputData.getDayExpenses());
        state.setDayProfits(outputData.getDayProfit());

        viewModel.setState(state);

        viewManagerModel.setState(DrillDownView.VIEW_NAME);
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void failView (String errorMessage){
//        InsightsState state = viewModel.getState();
//
//        state.setErrorMessage(errorMessage);
//
//        viewModel.setState(state);
    }



}
