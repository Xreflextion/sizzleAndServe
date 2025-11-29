package use_case.insights_performance_calculation;


import entity.PerDayRecord;
import java.util.List;
import java.util.ArrayList;

public class PerformanceCalculationInteractor implements PerformanceCalculationInputBoundary{

    private final DayRecordsDataAccessInterface dataStorage;
    private final PerformanceCalculationOutputBoundary presenter;

    /**
     *
     * @param dataStorage the DAO stores all PerDayRecords
     * @param presenter recieves the calculatedInsights
     */
    public PerformanceCalculationInteractor(DayRecordsDataAccessInterface dataStorage, PerformanceCalculationOutputBoundary presenter){
        this.dataStorage = dataStorage;
        this.presenter = presenter;
    }

    public PerDayRecord daySummary(double sales, double ingredientCost, double employeeWage, double rating){
        double totalRevenue = sales;
        double totalCost = ingredientCost + employeeWage;

        return new PerDayRecord(totalRevenue, totalCost, rating);
    }

    @Override
    public void calculateInsights(){
        List<PerDayRecord> allRecords = dataStorage.getAllData();

        if(allRecords.isEmpty()){
            presenter.failView("No data available yet");
            return;
        }

        double totalRevenue = 0;
        double totalExpenses = 0;
        double totalProfits = 0;
        double sumRating = 0;
        int reviewCount = 0;

        List<Double> revenueTrend = new ArrayList<>();
        List<Double> expensesTrend = new ArrayList<>();
        List<Double> profitTrend = new ArrayList<>();

        for(PerDayRecord record : allRecords){
            double dayRevenue = record.getRevenue();
            double dayExpenses = record.getExpenses();
            double dayProfit = record.getProfit();
            double dayRating = record.getRating();

            totalRevenue += dayRevenue;
            totalExpenses += dayExpenses;
            totalProfits += dayProfit;
            sumRating += dayRating;
            reviewCount++;

            revenueTrend.add(dayRevenue);
            expensesTrend.add(dayExpenses);
            profitTrend.add(dayProfit);
        }

        double averageRating = sumRating/reviewCount;

        int numberOfDays = allRecords.size();

        PerformanceCalculationOutputData outputData = new PerformanceCalculationOutputData(
                averageRating,reviewCount, totalRevenue, totalExpenses, totalProfits,revenueTrend,expensesTrend, profitTrend, numberOfDays);

        presenter.successView(outputData);
    }



//    public double calculateTotalRevenue(){
//        double totalRevenue = 0;
//        List <PerDayRecord> allRecords = dataStorage.getAllData();
//        for (int i = 0; i < allRecords.size(); i++) {
//            PerDayRecord currRec = allRecords.get(i);
//            totalRevenue += currRec.getRevenue();
//        }
//        return totalRevenue;
//    }
//
//    public double calculateTotalExpense(){
//        double totalExpenses = 0;
//        List <PerDayRecord> allRecords = dataStorage.getAllData();
//        for (int i = 0; i < allRecords.size(); i++) {
//            PerDayRecord currRec = allRecords.get(i);
//            totalExpenses += currRec.getExpenses();
//        }
//        return totalExpenses;
//    }
//
//    public double calculateProfit(){
//        double totalProfit = 0;
//        List <PerDayRecord> allRecords = dataStorage.getAllData();
//        for (int i = 0; i < allRecords.size(); i++) {
//            PerDayRecord currRec = allRecords.get(i);
//            totalProfit += currRec.getProfit();
//        }
//        return totalProfit;
//    }
//
//    public List<Double> getProfitTrend() {
//        List<Double> profitTrend = new ArrayList<>();
//        for (int i = 0; i < dataStorage.getAllData().size(); i++) {
//            profitTrend.add(dataStorage.getAllData().get(i).getProfit());
//        }
//        return profitTrend;
//    }
//
//    public List<Double> getRevenueTrend() {
//        List<Double> revenueTrend = new ArrayList<>();
//        for (int i = 0; i < dataStorage.getAllData().size(); i++) {
//            revenueTrend.add(dataStorage.getAllData().get(i).getRevenue());
//        }
//        return revenueTrend;
//
//    }
//
//    public List<Double> getExpensesTrend() {
//        List<Double> expensesTrend = new ArrayList<>();
//        for (int i = 0; i < dataStorage.getAllData().size(); i++) {
//            expensesTrend.add(dataStorage.getAllData().get(i).getExpenses());
//        }
//        return expensesTrend;
//    }


}
