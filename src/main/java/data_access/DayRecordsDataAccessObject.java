package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import constants.Constants;
import java.io.IOException;
import entity.PerDayRecord;
import use_case.insights_performance_calculation.DayRecordsDataAccessInterface;
import use_case.simulate.SimulateDayRecordsDataAccessInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DayRecordsDataAccessObject implements DayRecordsDataAccessInterface, SimulateDayRecordsDataAccessInterface {

    private List<PerDayRecord> dayRecords;
    private FileHelperObject fileHelperObject;

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

    public void saveToFile() throws IOException {
        JsonArray array = new JsonArray();
        Gson gson = new Gson();
        for (PerDayRecord record : dayRecords) {
            array.add(gson.toJsonTree(record).getAsJsonObject());
        }
        fileHelperObject.saveArray(Constants.DAY_RECORD_KEY, array);
    }

}
