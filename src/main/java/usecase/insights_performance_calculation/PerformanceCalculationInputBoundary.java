package usecase.insights_performance_calculation;

public interface PerformanceCalculationInputBoundary {

    /**
     * Calculates the performance across all saved days
     *
     * When this method is called, the interactor will:
     *  - read all PerDayRecords objects from DAO
     *  - sum the total revenue, total expenses, and total profit
     *  - compute the average rating across all days based on review count
     *  - give result to the presenter
     */
    void calculateInsights();

}
