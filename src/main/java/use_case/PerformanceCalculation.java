package use_case;

import data_access.DataStorage;
import entities.PerDayRecord;
import java.util.List;
import java.util.ArrayList;

public class PerformanceCalculation {


    private final DataStorage dataStorage;

    public PerformanceCalculation(DataStorage dataStorage){
        this.dataStorage = dataStorage;
    }

    public PerDayRecord daySummary(double sales, double ingredientCost, double employeeWage, double rating){
        double totalRevenue = sales;
        double totalCost = ingredientCost + employeeWage;

        return new PerDayRecord(totalRevenue, totalCost, rating);
    }

    public double calculateTotalRevenue(){
        double totalRevenue = 0;
        List <PerDayRecord> allRecords = dataStorage.getAllData();
        for (int i = 0; i < allRecords.size(); i++) {
            PerDayRecord currRec = allRecords.get(i);
            totalRevenue += currRec.getRevenue();
        }
        return totalRevenue;
    }

    public double calculateTotalExpense(){
        double totalExpenses = 0;
        List <PerDayRecord> allRecords = dataStorage.getAllData();
        for (int i = 0; i < allRecords.size(); i++) {
            PerDayRecord currRec = allRecords.get(i);
            totalExpenses += currRec.getExpenses();
        }
        return totalExpenses;
    }

    public double calculateProfit(){
        double totalProfit = 0;
        List <PerDayRecord> allRecords = dataStorage.getAllData();
        for (int i = 0; i < allRecords.size(); i++) {
            PerDayRecord currRec = allRecords.get(i);
            totalProfit += currRec.getProfit();
        }
        return totalProfit;
    }

    public List<Double> getProfitTrend() {
        List<Double> profitTrend = new ArrayList<>();
        for (int i = 0; i < dataStorage.getAllData().size(); i++) {
            profitTrend.add(dataStorage.getAllData().get(i).getProfit());
        }
        return profitTrend;
    }

    public List<Double> getRevenueTrend() {
        List<Double> revenueTrend = new ArrayList<>();
        for (int i = 0; i < dataStorage.getAllData().size(); i++) {
            revenueTrend.add(dataStorage.getAllData().get(i).getRevenue());
        }
        return revenueTrend;

    }

    public List<Double> getExpensesTrend() {
        List<Double> expensesTrend = new ArrayList<>();
        for (int i = 0; i < dataStorage.getAllData().size(); i++) {
            expensesTrend.add(dataStorage.getAllData().get(i).getExpenses());
        }
        return expensesTrend;
    }


}
