package use_case.insights.performance_calculation;

import java.util.ArrayList;
import java.util.List;

import entity.PerDayRecord;

public class PerformanceCalculationInteractor implements PerformanceCalculationInputBoundary {

    private final DayRecordsDataAccessInterface dataStorage;
    private final PerformanceCalculationOutputBoundary presenter;

    /**
     * Creates new PerformanceCalculationInteractor using the data storage and presenter.
     * @param dataStorage the DAO stores all PerDayRecords
     * @param presenter   recieves the calculatedInsights
     */
    public PerformanceCalculationInteractor(DayRecordsDataAccessInterface dataStorage,
                                            PerformanceCalculationOutputBoundary presenter) {
        this.dataStorage = dataStorage;
        this.presenter = presenter;
    }

    @Override
    public void calculateInsights() {

        final List<PerDayRecord> allRecords = dataStorage.getAllData();
        System.out.println("Number of days records found: " + allRecords.size());

        if (allRecords.isEmpty()) {
            presenter.failView("No data available yet");
        }
        else {

            double totalRevenue = 0;
            double totalExpenses = 0;
            double totalProfits = 0;
            double sumRating = 0;
            int reviewCount = -1;

            final List<Double> revenueTrend = new ArrayList<>();
            final List<Double> expensesTrend = new ArrayList<>();
            final List<Double> profitTrend = new ArrayList<>();

            for (PerDayRecord record : allRecords) {
                final double dayRevenue = record.getRevenue();
                final double dayExpenses = record.getExpenses();
                final double dayProfit = record.getProfit();
                final double dayRating = record.getRating();

                totalRevenue += dayRevenue;
                totalExpenses += dayExpenses;
                totalProfits += dayProfit;
                sumRating += dayRating;
                reviewCount++;

                revenueTrend.add(dayRevenue);
                expensesTrend.add(dayExpenses);
                profitTrend.add(dayProfit);
            }

            final double averageRating = Math.round((sumRating / reviewCount) * 10.0) / 10.0;

            final int numberOfDays = allRecords.size();

            final PerformanceCalculationOutputData outputData = new PerformanceCalculationOutputData(
                    averageRating, reviewCount, totalRevenue, totalExpenses, totalProfits, revenueTrend, expensesTrend,
                    profitTrend, numberOfDays);

            presenter.successView(outputData);
        }
    }

}
