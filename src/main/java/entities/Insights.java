package entities;

public class Insights {

    private double averageRating;
    private int reviewCount;
    private double totalRevenue;
    private double totalExpenses;
    private double totalProfit;
    // private Object trendChart;

    public Insights(double averageRating, int reviewCount, double totalRevenue, double totalExpenses, double totalProfit) {
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.totalRevenue = totalRevenue;
        this.totalExpenses = totalExpenses;
        this.totalProfit = totalProfit;
    }

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

//    public void setTotalExpenses(double totalExpenses) {
//        this.totalExpenses = totalExpenses;
//    }

    public double getTotalProfit() {
        return totalProfit;
    }

//    public void setTotalProfit(double totalProfit) {
//        this.totalProfit = totalProfit;
//    }

}
