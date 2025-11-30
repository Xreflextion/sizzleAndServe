package usecase.insights_performance_calculation;

/**
 * Output Boundary for Insights Performance Calculation
 */
public interface PerformanceCalculationOutputBoundary {

    /**
     * Called by interactor when there is data in list and insights are successully calculated
     * @param outputData consists of all calculated Insights, including:
     *                   numberOfDays, averageRating, reviewCount,
     *                   totalRevenue, totalExpenses, totalProfits,
     *                   revenueTrend, expensesTrend, and profitTrend
     */
    void successView (PerformanceCalculationOutputData outputData);

    /**
     * Called by interactor when problem arises due to lack of data
     * @param errorMessage displays explanation of problem
     */
    void failView (String errorMessage);

}
