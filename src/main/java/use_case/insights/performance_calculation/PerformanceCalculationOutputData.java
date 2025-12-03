package use_case.insights.performance_calculation;

import java.util.List;

/**
 * Output Data for Insights Performance Calculation Use Case.
 */
public class PerformanceCalculationOutputData {

    private final double averageRating;
    private final int reviewCount;
    private final double totalRevenue;
    private final double totalExpenses;
    private final double totalProfit;

    private final List<Double> revenueTrend;
    private final List<Double> expensesTrend;
    private final List<Double> profitTrend;

    private final int numberOfDays;

    public PerformanceCalculationOutputData(double averageRating, int reviewCount, double totalRevenue,
                                            double totalExpenses, double totalProfit, List<Double> revenueTrend,
                                            List<Double> expensesTrend, List<Double> profitTrend, int numberOfDays) {
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.totalProfit = totalProfit;
        this.revenueTrend = revenueTrend;
        this.expensesTrend = expensesTrend;
        this.profitTrend = profitTrend;
        this.numberOfDays = numberOfDays;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public List<Double> getRevenueTrend() {
        return revenueTrend;
    }

    public List<Double> getExpensesTrend() {
        return expensesTrend;
    }

    public List<Double> getProfitTrend() {
        return profitTrend;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

}
