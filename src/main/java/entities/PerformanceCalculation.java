package Entities;

import java.util.List;
import java.util.ArrayList;

public class PerformanceCalculation {
    private final List<Float> dailyRevenue;
    private final List<Float> dailyExpenses;
    private final List<Float> profitCalculation;

    public PerformanceCalculation(){
        this.dailyRevenue = new ArrayList<>();
        this.dailyExpenses = new ArrayList<>();
        this.profitCalculation = new ArrayList<>();

    }

    public float calculateTotalRevenue(){
        float totalRevenue = 0;
        int r = 0;
        for (int i = 0; i < dailyRevenue.size(); i++) {
            totalRevenue += dailyRevenue.get(i);
        }
        return totalRevenue;
    }

    public float calculateTotalExpense(){
        // need to add to sum up the expenses stored in DataStorage per day --> put into dailyExpenses
        float totalExpenses = 0;
        int r = 0;
        for (int i = 0; i < dailyExpenses.size(); i++) {
            totalExpenses += dailyExpenses.get(i);
        }
        return totalExpenses;
    }

    public float calculateProfit(){
        return calculateTotalRevenue()-calculateTotalExpense();
    }

    public List<Float> getProfitTrend() {
        return new ArrayList<>(profitCalculation);
    }

    public List<Float> getDailyRevenue() {
        return new ArrayList<>(dailyRevenue);

    }

    public List<Float> getDailyExpenses() {
        return new ArrayList<>(dailyExpenses);
    }


}
