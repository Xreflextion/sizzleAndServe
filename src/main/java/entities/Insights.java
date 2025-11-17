package Entities;

import java.util.List;
import java.util.ArrayList;

public class Insights {

    private float averageRating;
    private int reviewCount;
    private float totalRevenue;
    private float totalExpenses;
    private float totalProfit;
    // private Object trendChart;

    public float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(float averageRating) {
        this.averageRating = averageRating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public float getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(float totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public float getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(float totalExpenses) {
        this.totalExpenses = totalExpenses;
    }

    public float getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(float totalProfit) {
        this.totalProfit = totalProfit;
    }

}
