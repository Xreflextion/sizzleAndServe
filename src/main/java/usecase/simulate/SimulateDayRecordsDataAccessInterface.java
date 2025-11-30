package usecase.simulate;

import entity.PerDayRecord;

public interface SimulateDayRecordsDataAccessInterface {

    void saveNewData(PerDayRecord dayRecord);

    PerDayRecord getDayData(int day); // TODO Remove (testing only)
}
