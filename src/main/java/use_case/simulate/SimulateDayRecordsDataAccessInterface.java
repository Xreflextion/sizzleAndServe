package use_case.simulate;

import entity.PerDayRecord;

/**
 * DayRecords DAO interface for the Simulate Use Case.
 */
public interface SimulateDayRecordsDataAccessInterface {

    /**
     * Save a day record.
     * @param dayRecord the day record
     */
    void saveNewData(PerDayRecord dayRecord);

    /**
     * Retrieves the record for a specified day.
     *
     * @param day is the day number starting 1
     * @return the PerDayRecord of the specified day, or null if that day does not exist
     */
    PerDayRecord getDayData(int day);

    /**
     * Updates the performance record of an existing day.
     *
     * @param day the day number to update (starting from 1)
     * @param updatedRecord the new PerDayRecord that replaces the existing record
     */
    void updateDayData(int day, PerDayRecord updatedRecord);

}
