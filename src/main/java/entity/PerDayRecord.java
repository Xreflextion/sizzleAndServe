package entity;

public class PerDayRecord {

    private final double revenue;
    private final double expenses;
    private final double profit;
    private final double rating;

    public PerDayRecord(double revenue, double expenses, double rating) {
        this.revenue = revenue;
        this.expenses = expenses;
        this.profit = revenue - expenses;
        this.rating = rating;
    }

    public double getRevenue() {
        return revenue;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getProfit() {
        return profit;
    }

    public double getRating() {
        return rating;
    }

}

