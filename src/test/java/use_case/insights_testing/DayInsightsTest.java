package use_case.insights_testing;


import entity.PerDayRecord;
import org.junit.jupiter.api.Test;
import use_case.insights.day_calculation.DayInsightsInteractor;
import use_case.insights.day_calculation.DayInsightsOutputBoundary;
import use_case.insights.day_calculation.DayInsightsOutputData;
import use_case.insights.performance_calculation.DayRecordsDataAccessInterface;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DayInsightsTest {
    private static class TestDAO implements DayRecordsDataAccessInterface {
        private final List<PerDayRecord> records =  new ArrayList<>();

        @Override
        public void saveNewData (PerDayRecord dayRecord) {
            records.add(dayRecord);
        }

        @Override
        public PerDayRecord getDayData (int dayNumber) {
            int index = dayNumber -1;
            if (index < 0 || index >= records.size()) {
                return null;
            } else {
                return records.get(index);
            }
        }

        @Override
        public List<PerDayRecord> getAllData() {
            return new ArrayList<>(records);
        }

        @Override
        public int getNumberOfDays(){
            return records.size();
        }

        @Override
        public void updateDayData(int day, PerDayRecord updatedRecord) {
            int index = day - 1;
            if (index < 0 || index >= records.size()) {
                throw new IllegalArgumentException("Day does not exist");
            } else {
                records.set(index, updatedRecord);
            }
        }

        public void addRecordForDay (int day, PerDayRecord record) {
            while (records.size() < day) {
                records.add(null);
            }
            records.set(day - 1 , record);
        }
    }

    private static class TestPresenter implements DayInsightsOutputBoundary{
        DayInsightsOutputData output;
        String error;
        boolean successCalled;
        boolean failCalled;

        @Override
        public void successView(DayInsightsOutputData outputData) {
            successCalled = true;
            output = outputData;
        }

        @Override
        public void failView(String error) {
            failCalled = true;
            this.error = error;
        }
    }

    @Test
    void testSuccessDayInsights(){
        TestDAO testDAO = new TestDAO();

        PerDayRecord day1 = new PerDayRecord(100.0,40.0,3.0);
        PerDayRecord day2 = new PerDayRecord(200.0,80.0,4.0);

        testDAO.saveNewData(day1);
        testDAO.saveNewData(day2);

        assertEquals(2, testDAO.getNumberOfDays());

        PerDayRecord getData1 =  testDAO.getDayData(1);
        PerDayRecord getData2 =  testDAO.getDayData(2);

        assertNotNull(getData1);
        assertNotNull(getData2);

        assertEquals(100.0, getData1.getRevenue(),1e-6);
        assertEquals(40.0,getData1.getExpenses(),1e-6);
        assertEquals(60.0,getData1.getProfit(),1e-6);

        assertEquals(200.0,getData2.getRevenue(),1e-6);
        assertEquals(80.0,getData2.getExpenses(),1e-6);
        assertEquals(120.0,getData2.getProfit(),1e-6);

        assertNull(testDAO.getDayData(3));
    }

    @Test
    void testSaveAndUpdateDayData(){
        TestDAO testDAO = new TestDAO();

        testDAO.saveNewData(new PerDayRecord(100.0,40.0,3.0));
        testDAO.updateDayData(1, new PerDayRecord(200.0,80.0,4.0));

        PerDayRecord updatedRecord = testDAO.getDayData(1);
        assertNotNull(updatedRecord);
        assertEquals(200.0,updatedRecord.getRevenue(),1e-6);
        assertEquals(80.0,updatedRecord.getExpenses(),1e-6);
        assertEquals(120.0,updatedRecord.getProfit(),1e-6);
        assertEquals(4.0,updatedRecord.getRating(),1e-6);

    }

    @Test
    void testCallSuccessView(){
        TestDAO testDAO = new TestDAO();
        TestPresenter testPresenter = new TestPresenter();

        PerDayRecord record = new PerDayRecord(100.0,40.0,3.0);
        testDAO.addRecordForDay(2,record);

        DayInsightsInteractor interactor = new DayInsightsInteractor(testDAO,testPresenter);

        interactor.calculateDayInsights(2);

        assertTrue(testPresenter.successCalled);
        assertFalse(testPresenter.failCalled);
        assertNotNull(testPresenter.output);

        DayInsightsOutputData outputData = testPresenter.output;

        assertEquals(2,outputData.getDayNumber());
        assertEquals(3.0, outputData.getDayRating(),1e-6);
        assertEquals(100.0, outputData.getDayRevenue(),1e-6);
        assertEquals(40.0,outputData.getDayExpenses(),1e-6);
        assertEquals(60.0,outputData.getDayProfit(),1e-6);

    }

    @Test
    void testCallFailView(){
        TestDAO testDAO = new TestDAO();
        TestPresenter testPresenter = new TestPresenter();

        DayInsightsInteractor interactor = new DayInsightsInteractor(testDAO,testPresenter);

        interactor.calculateDayInsights(123);

        assertTrue(testPresenter.failCalled);
        assertFalse(testPresenter.successCalled);
        assertNull(testPresenter.output);
        assertEquals("No data available yet", testPresenter.error);
    }

}
