package data_access;

import entity.PerDayRecord;
import use_case.insights_performance_calculation.DayRecordsDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

public class DayRecordsDataAccessObject implements DayRecordsDataAccessInterface {

    private List<PerDayRecord> dayRecords = new ArrayList<PerDayRecord>();

    public DayRecordsDataAccessObject() {

    }

    @Override
    public void saveNewData(PerDayRecord dayRecord){

        if (dayRecord == null){
            throw new NullPointerException("dayRecord is null");
        }
        dayRecords.add(dayRecord);
    }

    @Override
    public PerDayRecord getDayData(int day){
        if (day < 1 || day > dayRecords.size()){
            return null;
        } else{
            return dayRecords.get(day - 1);
        }

    }

    @Override
    public List<PerDayRecord> getAllData(){
        return new ArrayList<>(dayRecords);
    }

    @Override
    public int getNumberOfDays(){
        return dayRecords.size();
    }

}

