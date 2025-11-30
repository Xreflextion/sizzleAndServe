package interface_adapter.insight;

import use_case.insights.day_calculation.DayInsightsInputBoundary;

public class DayInsightsController {

    private final DayInsightsInputBoundary interactor;

    public DayInsightsController(DayInsightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void displayDayInsights(int dayNumber){
        interactor.calculateDayInsights(dayNumber);
    }
}
