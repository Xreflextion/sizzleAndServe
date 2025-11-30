package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import entity.PerDayRecord;
import use_case.insights.performance_calculation.DayRecordsDataAccessInterface;
import use_case.simulate.SimulateDayRecordsDataAccessInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayRecordsDataAccessObject implements DayRecordsDataAccessInterface, SimulateDayRecordsDataAccessInterface {

    private List<PerDayRecord> dayRecords;
    private FileHelperObject fileHelperObject;

    public DayRecordsDataAccessObject(FileHelperObject fileHelperObject) {
        this.fileHelperObject = fileHelperObject;
        ArrayList<PerDayRecord> newDayRecords = new ArrayList<>();
        JsonArray daysArray = fileHelperObject.getArrayFromSaveData(Constants.DAY_RECORD_KEY);
        for (JsonElement element: daysArray) {
            JsonObject day = element.getAsJsonObject();
            double revenue = 0.0;
            double expenses = 0.0;
            double rating = 3.0;
            if (day.keySet().contains("revenue")) {
                revenue = day.get("revenue").getAsDouble();
            }
            if (day.keySet().contains("expenses")) {
                expenses = day.get("expenses").getAsDouble();
            }
            if (day.keySet().contains("rating")) {
                rating = day.get("rating").getAsDouble();
            }
            newDayRecords.add(new PerDayRecord(revenue, expenses, rating));
        }
        this.dayRecords = newDayRecords;

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

