package interface_adapter.insight;

import use_case.insights.day_calculation.DayInsightsInputBoundary;

public class DayInsightsController {

    private final DayInsightsInputBoundary interactor;

    public DayInsightsController(DayInsightsInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Triggers usecase to calculate and display insights of specific day.
     * @param dayNumber the specific day to display insights
     */
    public void displayDayInsights(int dayNumber) {
        interactor.calculateDayInsights(dayNumber);
    }
}
