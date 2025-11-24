package use_case.simulate;

import entity.PerDayRecord;

public interface SimulateDayRecordsDataAccessInterface {

    void saveNewData(PerDayRecord dayRecord);
}
