package interfaceadapter.insight;

import java.util.List;

public class InsightsState {

    private double averageRating =0;
    private int reviewCount = 0;
    private double totalRevenue = 0;
    private double totalExpenses = 0;
    private double totalProfits = 0;
    private List<Double> revenueTrend;
    private List<Double> expensesTrend;
    private List<Double> profitTrend;
    private double dayRevenue = 0;
    private double dayExpenses = 0;
    private double dayProfits = 0;
    private double dayRating = 0;
    private int dayNumber;
    private int numberOfDays;

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

    public double getTotalProfits() {
        return totalProfits;
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

    public double getDayRevenue() {
        return dayRevenue;
    }

    public double getDayExpenses() {
        return dayExpenses;
    }

    public double getDayProfits() {
        return dayProfits;
    }

    public double getDayRating() {
        return dayRating;
    }

    public int getDayNumber() {
        return dayNumber;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
    public void setTotalExpenses(double totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public void setTotalProfits(double totalProfits) {
        this.totalProfits = totalProfits;
    }

    public void setRevenueTrend(List<Double> revenueTrend) {
        this.revenueTrend = revenueTrend;
    }

    public void setExpensesTrend(List<Double> expensesTrend) {
        this.expensesTrend = expensesTrend;
    }

    public void setProfitTrend(List<Double> profitTrend) {
        this.profitTrend = profitTrend;
    }

    public void setDayRevenue(double dayRevenue) {
        this.dayRevenue = dayRevenue;
    }

    public void setDayExpenses(double dayExpenses) {
        this.dayExpenses = dayExpenses;
    }

    public void setDayProfits(double dayProfits) {
        this.dayProfits = dayProfits;
    }

    public void setDayRating(double dayRating) {
        this.dayRating = dayRating;
    }

    public void setDayNumber(int dayNumber) {
        this.dayNumber = dayNumber;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
