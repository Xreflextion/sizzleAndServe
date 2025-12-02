package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import entity.PerDayRecord;
import use_case.buy_serving.BuyServingDayRecordsDataAccessInterface;
import use_case.insights.performance_calculation.DayRecordsDataAccessInterface;
import use_case.simulate.SimulateDayRecordsDataAccessInterface;

public class DayRecordsDataAccessObject implements BuyServingDayRecordsDataAccessInterface,
                                                   DayRecordsDataAccessInterface,
                                                   SimulateDayRecordsDataAccessInterface {

    public static final double RATING = 3.0;
    private List<PerDayRecord> dayRecords;
    private FileHelperObject fileHelperObject;

    public DayRecordsDataAccessObject(FileHelperObject fileHelperObject) {
        this.fileHelperObject = fileHelperObject;
        final ArrayList<PerDayRecord> newDayRecords = new ArrayList<>();
        final JsonArray daysArray = fileHelperObject.getArrayFromSaveData(Constants.DAY_RECORD_KEY);
        for (JsonElement element: daysArray) {
            final JsonObject day = element.getAsJsonObject();
            double revenue = 0.0;
            double expenses = 0.0;
            double rating = RATING;
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
        if (newDayRecords.size() == 0) {
            newDayRecords.add(new PerDayRecord(0,0,0));
        }
        this.dayRecords = newDayRecords;

    }

    @Override
    public void saveNewData(PerDayRecord dayRecord) {
        if (dayRecord == null) {
            throw new NullPointerException("dayRecord is null");
        }
        dayRecords.add(dayRecord);
        save();
    }

    @Override
    public PerDayRecord getDayData(int day) {
        final PerDayRecord result;
        if (day < 1 || day > dayRecords.size()) {
            result = null;
        }
        else {
            result = dayRecords.get(day - 1);
        }

        return result;
    }

    @Override
    public List<PerDayRecord> getAllData() {
        return new ArrayList<>(dayRecords);
    }

    @Override
    public int getNumberOfDays() {
        return dayRecords.size();
    }

    @Override
    public void updateDayData(int day, PerDayRecord updatedRecord){
        if (updatedRecord == null){
            throw new NullPointerException("updatedRecord is null");
        }
        if (day < 1 || day > dayRecords.size()){
            throw new IllegalArgumentException("Invalid day number");
        }
        dayRecords.set(day - 1, updatedRecord);
        save();
    }

    /**
     * Seralizies the list of daily records.
     * @throws IOException if an I/O error occurs during the file-saving process.
     */                                                 
    public void saveToFile() throws IOException {
        final JsonArray recordArray = new JsonArray();
        final Gson gson = new Gson();
        for (PerDayRecord record : dayRecords) {
            recordArray.add(gson.toJsonTree(record).getAsJsonObject());
        }
        fileHelperObject.saveArray(Constants.DAY_RECORD_KEY, recordArray);
    }

    /**
     * Saves the current state of data to a file through the provided file helper object.
     */
    public void save() {
        if (fileHelperObject != null) {
            try {
                saveToFile();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
