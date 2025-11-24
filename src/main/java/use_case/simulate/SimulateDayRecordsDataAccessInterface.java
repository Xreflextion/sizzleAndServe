package use_case.simulate;

import entity.PerDayRecord;

import java.util.List;

public interface SimulateDayRecordsDataAccessInterface {

    void saveNewData(PerDayRecord dayRecord);

    PerDayRecord getDayData(int day); // TODO Remove (testing only)
}
