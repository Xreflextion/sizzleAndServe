package use_case.insights.performance_calculation;


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
    public PerformanceCalculationInteractor(DayRecordsDataAccessInterface dataStorage,
                                            PerformanceCalculationOutputBoundary presenter){
        this.dataStorage = dataStorage;
        this.presenter = presenter;
    }


    @Override
    public void calculateInsights(){
        System.out.println("===Insights Calculation DEBUG===");
        System.out.println("Debug: Insights Interactor is running");

        List<PerDayRecord> allRecords = dataStorage.getAllData();
        System.out.println("Number of days records found: " + allRecords.size());

        if(allRecords.isEmpty()){
            System.out.println("WARNING: No data available yet -- cannot calculate insights");
            presenter.failView("No data available yet");
            return;
        }

        double totalRevenue = 0;
        double totalExpenses = 0;
        double totalProfits = 0;
        double sumRating = 0;
        int reviewCount = -1;

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

        double averageRating = Math.round((sumRating/reviewCount)*10.0)/10.0;

        int numberOfDays = allRecords.size();

        System.out.println("===Insights Calculation Results===");
        System.out.println("Total Revenue: " + totalRevenue);
        System.out.println("Total Expenses: " + totalExpenses);
        System.out.println("Total Profits: " + totalProfits);
        System.out.println("Average Rating: " + averageRating);
        System.out.println("Number of Days " + numberOfDays);

        PerformanceCalculationOutputData outputData = new PerformanceCalculationOutputData(
                averageRating,reviewCount, totalRevenue, totalExpenses, totalProfits,revenueTrend,expensesTrend,
                profitTrend, numberOfDays);

        presenter.successView(outputData);
    }


}
