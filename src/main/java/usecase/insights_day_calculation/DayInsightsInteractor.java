package usecase.insights_day_calculation;

import usecase.insights_performance_calculation.DayRecordsDataAccessInterface;
import entity.PerDayRecord;


public class DayInsightsInteractor implements DayInsightsInputBoundary {

    private final DayRecordsDataAccessInterface dataStorage;
    private final DayInsightsOutputBoundary presenter;

    public DayInsightsInteractor(DayRecordsDataAccessInterface dataStorage, DayInsightsOutputBoundary presenter) {
        this.dataStorage = dataStorage;
        this.presenter = presenter;

    }

    @Override
    public void calculateDayInsights(int dayNumber){
        PerDayRecord record = dataStorage.getDayData(dayNumber);

        if (record == null) {
            presenter.failView("No data available yet");
            return;
        }
        DayInsightsOutputData outputData = new DayInsightsOutputData(
                dayNumber,
                record.getRating(),
                record.getRevenue(),
                record.getExpenses(),
                record.getProfit()
        );
        presenter.successView(outputData);

    }


}
