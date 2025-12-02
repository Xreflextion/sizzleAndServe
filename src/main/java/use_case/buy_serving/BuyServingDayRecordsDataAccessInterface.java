package use_case.buy_serving;

import entity.PerDayRecord;

/**
 * Data Access Interface for Insights Performance Calculation use case.
 * Provides access to all per-day performance records
 */

public interface BuyServingDayRecordsDataAccessInterface {

    /**
     * Saves new record for a single day.
     * @param dayRecord the PerDayRecord for that day
     */
    void saveNewData(PerDayRecord dayRecord);

    /**
     * Retrieves the record for a specified day.
     * @param day is the day number starting 1
     * @return the PerDayRecord of the specified day, or null if that day does not exist
     */
    PerDayRecord getDayData(int day);

    /**
     * Retrieves the number days that has been recorded.
     *
     * @return the number of days of saved PerDayRecord objects in list
     */
    int getNumberOfDays();

    /**
     * Updates the performance record of an existing day.
     *
     * @param day the day number to update (starting from 1)
     * @param updatedRecord the new PerDayRecord that replaces the existing record
     */
    void updateDayData(int day, PerDayRecord updatedRecord);

}
