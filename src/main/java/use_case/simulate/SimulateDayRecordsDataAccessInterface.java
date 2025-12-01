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

}
