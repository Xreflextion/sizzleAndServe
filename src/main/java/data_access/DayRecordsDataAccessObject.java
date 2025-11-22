package data_access;

import entity.PerDayRecord;

import java.util.ArrayList;
import java.util.List;

public class DayRecordsDataAccessObject {

    private List<PerDayRecord> dayRecords = new ArrayList<PerDayRecord>();

    public DayRecordsDataAccessObject() {

    }

    public void saveNewData(PerDayRecord dayRecord){

        if (dayRecord == null){
            throw new NullPointerException("dayRecord is null");
        }
        dayRecords.add(dayRecord);
    }

    public PerDayRecord getDayData(int day){
        if (day < 1 || day > dayRecords.size()){
            return null;
        } else{
            return dayRecords.get(day - 1);
        }

    }

    public List<PerDayRecord> getAllData(){
        return new ArrayList<>(dayRecords);
    }

    public int getNumberofDays(){
        return dayRecords.size();
    }

}

