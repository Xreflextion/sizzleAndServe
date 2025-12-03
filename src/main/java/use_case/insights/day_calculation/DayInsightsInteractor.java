package use_case.insights.day_calculation;

import entity.PerDayRecord;
import use_case.insights.performance_calculation.DayRecordsDataAccessInterface;

public class DayInsightsInteractor implements DayInsightsInputBoundary {

    private final DayRecordsDataAccessInterface dataStorage;
    private final DayInsightsOutputBoundary presenter;

    public DayInsightsInteractor(DayRecordsDataAccessInterface dataStorage, DayInsightsOutputBoundary presenter) {
        this.dataStorage = dataStorage;
        this.presenter = presenter;

    }

    @Override
    public void calculateDayInsights(int dayNumber) {
        final PerDayRecord record = dataStorage.getDayData(dayNumber);

        if (record == null) {
            presenter.failView("No data available yet");
        }
        else {
            final DayInsightsOutputData outputData = new DayInsightsOutputData(
                    dayNumber,
                    record.getRating(),
                    record.getRevenue(),
                    record.getExpenses(),
                    record.getProfit()
            );
            presenter.successView(outputData);
        }

    }

}
