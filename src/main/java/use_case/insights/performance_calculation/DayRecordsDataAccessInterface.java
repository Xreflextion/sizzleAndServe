package use_case.insights.performance_calculation;

import entity.PerDayRecord;
import java.util.List;

/**
 * Data Access Interface for Insights Performance Calculation use case.
 * Provides access to all per-day performance records
 */

public interface DayRecordsDataAccessInterface {

    /**
     * Saves new record for a single day
     *
     * @param dayRecord the PerDayRecord for that day
     */

    void saveNewData(PerDayRecord dayRecord);


    /**
     * Retrieves the record for a specified day
     *
     * @param day is the day number starting 1
     * @return the PerDayRecord of the specified day, or null if that day does not exist
     */
    PerDayRecord getDayData(int day);

    /**
     * Retrieves the records for all days
     *
     * @return list of all PerDayRecord objects
     */
    List<PerDayRecord> getAllData();

    /**
     * Retrieves the number days that has been recorded
     *
     * @return the number of days of saved PerDayRecord objects in list
     */
    int getNumberOfDays();


}
