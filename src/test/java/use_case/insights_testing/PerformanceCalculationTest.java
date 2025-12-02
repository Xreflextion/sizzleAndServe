package use_case.insights_testing;

import entity.PerDayRecord;
import org.junit.jupiter.api.Test;
import use_case.insights.day_calculation.DayInsightsOutputBoundary;
import use_case.insights.day_calculation.DayInsightsOutputData;
import use_case.insights.performance_calculation.DayRecordsDataAccessInterface;
import use_case.insights.performance_calculation.PerformanceCalculationInteractor;
import use_case.insights.performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.insights.performance_calculation.PerformanceCalculationOutputData;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class PerformanceCalculationTest {

    private static class TestDAO implements DayRecordsDataAccessInterface {

        private final List<PerDayRecord> records = new ArrayList<>();

        @Override
        public void saveNewData(PerDayRecord dayRecord) {
            records.add(dayRecord);
        }

        @Override
        public PerDayRecord getDayData(int dayNumber) {
            int index = dayNumber - 1;
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
        public int getNumberOfDays() {
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
    }

    private static class TestPresenter implements PerformanceCalculationOutputBoundary {
        PerformanceCalculationOutputData output;
        String error;
        boolean successCalled = false;
        boolean failCalled = false;

        @Override
        public void successView(PerformanceCalculationOutputData outputData) {
            successCalled = true;
            this.output = outputData;
        }

        @Override
        public void failView(String error) {
            failCalled = true;
            this.error = error;
        }
    }

    @Test
    void testFailView() {
        TestDAO testDAO = new TestDAO();
        TestPresenter testPresenter = new TestPresenter();

        PerformanceCalculationInteractor interactor = new PerformanceCalculationInteractor(testDAO, testPresenter);

        interactor.calculateInsights();

        assertTrue(testPresenter.failCalled);
        assertFalse(testPresenter.successCalled);
        assertEquals("No data available yet", testPresenter.error);
        assertNull(testPresenter.output);

    }

    @Test
    void testSuccessView() {
        TestDAO testDAO = new TestDAO();
        TestPresenter testPresenter = new TestPresenter();

        testDAO.saveNewData(new PerDayRecord(100.0, 40.0, 3.0));
        testDAO.saveNewData(new PerDayRecord(200.0, 80.0, 4.0));
        testDAO.saveNewData(new PerDayRecord(150.0, 75.0, 3.8));

        PerformanceCalculationInteractor interactor = new PerformanceCalculationInteractor(testDAO, testPresenter);

        interactor.calculateInsights();

        assertTrue(testPresenter.successCalled);
        assertFalse(testPresenter.failCalled);
        assertNotNull(testPresenter.output);

        PerformanceCalculationOutputData outputData = testPresenter.output;

        assertEquals(450.0, outputData.getTotalRevenue(), 1e-6);
        assertEquals(195.0, outputData.getTotalExpenses(), 1e-6);
        assertEquals(255.0, outputData.getTotalProfit(), 1e-6);

        assertEquals(3, outputData.getRevenueTrend().size());
        assertEquals(3, outputData.getExpensesTrend().size());
        assertEquals(3, outputData.getProfitTrend().size());

        assertEquals(100.0, outputData.getRevenueTrend().get(0), 1e-6);
        assertEquals(200.0, outputData.getRevenueTrend().get(1), 1e-6);
        assertEquals(150.0, outputData.getRevenueTrend().get(2), 1e-6);

        assertEquals(40.0, outputData.getExpensesTrend().get(0), 1e-6);
        assertEquals(80.0, outputData.getExpensesTrend().get(1), 1e-6);
        assertEquals(75.0, outputData.getExpensesTrend().get(2), 1e-6);

        assertEquals(60.0, outputData.getProfitTrend().get(0), 1e-6);
        assertEquals(120.0, outputData.getProfitTrend().get(1), 1e-6);
        assertEquals(75.0, outputData.getProfitTrend().get(2), 1e-6);

        assertEquals(3, outputData.getNumberOfDays());

        }


    @Test
    void testOutputDataGetters(){
        double averageRating = 4.3;
        int reviewCount = 3;
        double totalRevenue = 450.0;
        double totalExpenses = 195.0;
        double totalProfits = 255.0;

        List<Double> revenueTrend = List.of(100.0, 200.0, 150.0);
        List<Double> expensesTrend = List.of(35.0, 100.0, 60.0);
        List<Double> profitTrend = List.of(65.0, 100.0, 90.0);
        int numberOfDays = 3;

        PerformanceCalculationOutputData data = new PerformanceCalculationOutputData(
                averageRating,
                reviewCount,
                totalRevenue,
                totalExpenses,
                totalProfits,
                revenueTrend,
                expensesTrend,
                profitTrend,
                numberOfDays

        );

        assertEquals(averageRating, data.getAverageRating(),1e-6);
        assertEquals(reviewCount, data.getReviewCount());

        assertEquals(totalRevenue, data.getTotalRevenue(), 1e-6);
        assertEquals(totalExpenses, data.getTotalExpenses(), 1e-6);
        assertEquals(totalProfits, data.getTotalProfit(), 1e-6);

        assertEquals(revenueTrend, data.getRevenueTrend());
        assertEquals(expensesTrend, data.getExpensesTrend());
        assertEquals(profitTrend, data.getProfitTrend());

        assertEquals(numberOfDays, data.getNumberOfDays());


    }
    }



