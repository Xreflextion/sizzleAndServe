package data_access;

import entities.PerDayRecord;

import java.util.ArrayList;
import java.util.List;

public class DataStorage {

    private List<PerDayRecord> dayRecords = new ArrayList<PerDayRecord>();
    private int currentDay = 1;
    // private int pastDays = 0;

    public DataStorage() {

    }

    public void saveNewData(PerDayRecord dayRecord){

        if (dayRecord == null){
            throw new NullPointerException("dayRecord is null");
        }
        dayRecords.add(dayRecord);
        currentDay = dayRecords.size() + 1;
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


}

