package use_case.insights.day_calculation;

public interface DayInsightsInputBoundary {
    /**
     * Calculates day performance across all saved days.
     *
     * <p>
     * When this method is called, the interactor will:
     * - read the day revenue, expenses, profit, and rating
     * - give result to the presenter
     *
     * @param dayNumber specific day to calculate insights for
     */
    void calculateDayInsights(int dayNumber);
}
