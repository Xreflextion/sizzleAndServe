package use_case.insights.day_calculation;

public interface DayInsightsOutputBoundary {

    /**
     * Called by interactor when there is data in list and insights are successully calculated.
     *
     * @param outputData consists of all calculated Insights, including:
     *                   dayRating,dayRevenue, dayExpenses, and dayProfits
     */
    void successView(DayInsightsOutputData outputData);

    /**
     * Called by interactor when problem arises due to lack of data.
     *
     * @param errorMessage displays explanation of problem
     */
    void failView(String errorMessage);
}
