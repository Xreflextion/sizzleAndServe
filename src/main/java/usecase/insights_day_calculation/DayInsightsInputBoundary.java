package usecase.insights_day_calculation;

public interface DayInsightsInputBoundary {
    /**
     * Calculates day performance across all saved days
     *
     * When this method is called, the interactor will:
     * - read the day revenue, expenses, profit, and rating
     * give result to the presenter
     */
    void calculateDayInsights(int dayNumber);
}
