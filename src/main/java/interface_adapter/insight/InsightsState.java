package interface_adapter.insight;

import java.util.List;

public class InsightsState {

    private double averageRating;
    private int reviewCount;
    private double totalRevenue;
    private double totalExpenses;
    private double totalProfits;
    private List<Double> revenueTrend;
    private List<Double> expensesTrend;
    private List<Double> profitTrend;
    private double dayRevenue;
    private double dayExpenses;
    private double dayProfits;
    private double dayRating;
    private int dayNumber;
    private int numberOfDays;

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public double getTotalProfits() {
        return totalProfits;
    }

    public void setTotalProfits(double totalProfits) {
        this.totalProfits = totalProfits;
    }

    public List<Double> getRevenueTrend() {
        return revenueTrend;
    }

    public void setRevenueTrend(List<Double> revenueTrend) {
        this.revenueTrend = revenueTrend;
    }

    public List<Double> getExpensesTrend() {
        return expensesTrend;
    }

    public void setExpensesTrend(List<Double> expensesTrend) {
        this.expensesTrend = expensesTrend;
    }

    public List<Double> getProfitTrend() {
        return profitTrend;
    }

    public void setProfitTrend(List<Double> profitTrend) {
        this.profitTrend = profitTrend;
    }

    public double getDayRevenue() {
        return dayRevenue;
    }

    public void setDayRevenue(double dayRevenue) {
        this.dayRevenue = dayRevenue;
    }

    public double getDayExpenses() {
        return dayExpenses;
    }

    public void setDayExpenses(double dayExpenses) {
        this.dayExpenses = dayExpenses;
    }

    public double getDayProfits() {
        return dayProfits;
    }

    public void setDayProfits(double dayProfits) {
        this.dayProfits = dayProfits;
    }

    public double getDayRating() {
        return dayRating;
    }

    public void setDayRating(double dayRating) {
        this.dayRating = dayRating;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
