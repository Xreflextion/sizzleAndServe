package use_case.simulate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import entity.Employee;
import entity.Pantry;
import entity.PerDayRecord;
import entity.Player;
import entity.Recipe;
import entity.Review;

public class SimulateInteractorTest {
    private static final String UNEXPECTED_SUCCESS_MESSAGE = "Use case success is unexpected: ";
    private static final String UNEXPECTED_FAILURE_MESSAGE = "Use case failure is unexpected: ";

    private static final int TEST_PLAYER_BALANCE = 100;
    private static final int TEST_CUSTOMER_COUNT = 5;
    private static final int TEST_CURRENT_DAY = 1;

    private static final int TEST_BASE_PRICE = 5;
    private static final String TEST_RECIPE_ONE_NAME = "One";
    private static final String TEST_RECIPE_TWO_NAME = "Two";
    private static final String TEST_RECIPE_THREE_NAME = "Three";

    private static final int BASE_COOK_WAGE = 50;
    private static final int BASE_WAITER_WAGE = 50;
    private static final int MIN_WAGE = 30;
    private static final int COOK_WAGE_NO_REDUCTION = (int) (MIN_WAGE + 10
            * (SimulateInteractor.COOK_EFFECT_REDUCTION - 1.0) / 0.2);
    private static final int WAITER_WAGE_NO_REDUCTION = (int) (MIN_WAGE + 10
            * (SimulateInteractor.WAITER_EFFECT_REDUCTION - 1.0) / 0.2);

    private SimulatePantryDataAccessInterface generatePantryDataAccessObject() {
        final Recipe recipe1 = new Recipe(TEST_RECIPE_ONE_NAME, TEST_BASE_PRICE);
        final Recipe recipe2 = new Recipe(TEST_RECIPE_TWO_NAME, TEST_BASE_PRICE);
        final Recipe recipe3 = new Recipe(TEST_RECIPE_THREE_NAME, TEST_BASE_PRICE);
        final Pantry pantry = new Pantry(recipe1, recipe2, recipe3);

        return new SimulatePantryDataAccessInterface() {
            @Override
            public Pantry getPantry() {
                return pantry;
            }

            @Override
            public Map<String, Integer> getStock() {
                final Map<String, Integer> stock = new HashMap<>();
                for (String dishName: pantry.getDishNames()) {
                    stock.put(dishName, pantry.getRecipe(dishName).getStock());
                }
                return stock;
            }

            @Override
            public void saveStock(Map<String, Integer> stock) {
                for (String dishName: stock.keySet()) {
                    pantry.getRecipe(dishName).setStock(stock.get(dishName));
                }
            }

            @Override
            public Map<String, Double> getCurrentPrices() {
                final Map<String, Double> prices = new HashMap<>();
                for (String dishName: pantry.getDishNames()) {
                    prices.put(dishName, pantry.getRecipe(dishName).getCurrentPrice());
                }
                return prices;
            }

        };
    }

    private SimulatePlayerDataAccessInterface generatePlayerDataAccessObject(double balance) {
        return new SimulatePlayerDataAccessInterface() {
            @Override
            public Player getPlayer() {
                return new Player("Name", balance);
            }

            @Override
            public void savePlayer(Player player) {

            }
        };
    }

    private SimulateReviewDataAccessInterface generateReviewDataAccessObject() {
        final Map<Integer, ArrayList<Double>> reviewManager = new HashMap<>();
        return new SimulateReviewDataAccessInterface() {
            @Override
            public void addReview(Review review) {
                if (!reviewManager.containsKey(review.getDayNum())) {
                    reviewManager.put(review.getDayNum(), new ArrayList<>());
                }
                reviewManager.get(review.getDayNum()).add(review.getRating());
            }

            @Override
            public ArrayList<Double> getReviewsByDay(int day) {
                final ArrayList<Double> result;
                final ArrayList<Double> reviews = reviewManager.get(day);
                if (reviews == null) {
                    result = new ArrayList<>();
                }
                else {
                    result = reviews;
                }
                return result;
            }
        };
    }

    private SimulateWageDataAccessInterface generateWageDataAccessObject(int cookWage, int waiterWage) {
        return new SimulateWageDataAccessInterface() {
            @Override
            public Employee getEmployee(String position) {
                Employee result = null;
                if (position.equals(SimulateInteractor.COOK_POSITION)) {
                    result = new Employee(cookWage, SimulateInteractor.COOK_POSITION);
                }
                else if (position.equals(SimulateInteractor.WAITER_POSITION)) {
                    result = new Employee(waiterWage, SimulateInteractor.WAITER_POSITION);
                }
                return result;
            }

            @Override
            public int getTotalWage() {
                return waiterWage + cookWage;
            }
        };
    }

    private SimulateDayRecordsDataAccessInterface generateDayRecordsDataAccessObject() {
        final List<PerDayRecord> dayRecords = new ArrayList<PerDayRecord>();
        dayRecords.add(new PerDayRecord(0, 0, 0));
        return new SimulateDayRecordsDataAccessInterface() {
            @Override
            public void saveNewData(PerDayRecord dayRecord) {
                dayRecords.add(dayRecord);
            }

            @Override
            public PerDayRecord getDayData(int day) {
                return dayRecords.get(day - 1);
            }

            @Override
            public void updateDayData(int day, PerDayRecord updatedRecord) {
                dayRecords.set(day - 1, updatedRecord);
            }

        };
    }

    /**
     * Test if simulation succeeds to return the next day.
     */
    @Test
    void nextDaySuccessTest() {
        final int expectedNextDay = TEST_CURRENT_DAY + 1;
        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, TEST_CUSTOMER_COUNT);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(
                TEST_PLAYER_BALANCE);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(BASE_COOK_WAGE,
                BASE_WAITER_WAGE);

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertEquals(expectedNextDay, outputData.getCurrentDay());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

    /**
     * Test if simulation succeeds with no stock.
     */
    @Test
    void noStockSuccessTest() {
        final int expectedCurrentBalance = TEST_PLAYER_BALANCE;
        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, TEST_CUSTOMER_COUNT);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(
                TEST_PLAYER_BALANCE);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(0, 0);

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertEquals(expectedCurrentBalance, outputData.getCurrentBalance());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

    /**
     * Test if simulation succeeds with current balance reduced due to employee expenses.
     */
    @Test
    void employeeExpensesApplied() {
        final int waiterWage = BASE_WAITER_WAGE * 35;
        final int currentBalance = 100 + BASE_COOK_WAGE + waiterWage;
        final int expectedCurrentBalance = currentBalance - BASE_COOK_WAGE - waiterWage;
        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, TEST_CUSTOMER_COUNT);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(currentBalance);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(BASE_COOK_WAGE,
                waiterWage);

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertEquals(expectedCurrentBalance, outputData.getCurrentBalance());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

    /**
     * Test if customer count is within range.
     */
    @Test
    void customerCountWithinRange() {
        final int customerCount = 50;
        final int minBoundCustomerCount = customerCount - SimulateInteractor.CUSTOMER_RANGE;
        final int maxBoundCustomerCount = customerCount + SimulateInteractor.CUSTOMER_RANGE + 1;
        // cook effect adds by at least 1

        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, customerCount);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(
                TEST_PLAYER_BALANCE);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(
                COOK_WAGE_NO_REDUCTION,
                WAITER_WAGE_NO_REDUCTION
        );

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertTrue(minBoundCustomerCount <= outputData.getCurrentCustomerCount()
                        && outputData.getCurrentCustomerCount() <= maxBoundCustomerCount
                );
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

    /**
     * Test if revenue generated from orders is within a range.
     */
    @Test
    void revenueMadeWithinRange() {

        final int customerCount = 50;
        final int minBoundCustomerCount = customerCount - SimulateInteractor.CUSTOMER_RANGE;
        final int maxBoundCustomerCount = customerCount + SimulateInteractor.CUSTOMER_RANGE + 1;
        // cook effect adds by at least 1

        final int minBoundExpectedBalance = TEST_PLAYER_BALANCE + TEST_BASE_PRICE * minBoundCustomerCount;
        final int maxBoundExpectedBalance = TEST_PLAYER_BALANCE + TEST_BASE_PRICE * maxBoundCustomerCount;

        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, customerCount);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final Map<String, Integer> stock = new HashMap<>();
        stock.put(TEST_RECIPE_ONE_NAME, maxBoundCustomerCount);
        stock.put(TEST_RECIPE_TWO_NAME, maxBoundCustomerCount);
        stock.put(TEST_RECIPE_THREE_NAME, maxBoundCustomerCount);
        pantryDataAccessObject.saveStock(stock);
        final int totalStockCount = maxBoundCustomerCount * 3;
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(
                TEST_PLAYER_BALANCE);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(0, 0);

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertTrue(minBoundExpectedBalance <= outputData.getCurrentBalance()
                        && outputData.getCurrentBalance() <= maxBoundExpectedBalance
                );
                int stockCount = 0;
                for (String name: outputData.getStock().keySet()) {
                    stockCount += outputData.getStock().get(name);
                }
                assertEquals(totalStockCount, stockCount + outputData.getCurrentCustomerCount());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

    /**
     * Test if customer count is within range when considering reviews.
     */
    @Test
    void customerCountWithinRangeWithReviews() {
        final double ratingMultiplier = 1.5;
        final int customerCount = 50;
        final int minBoundCustomerCount = (int) ((customerCount - SimulateInteractor.CUSTOMER_RANGE)
                * ratingMultiplier);
        final int maxBoundCustomerCount = (int) ((customerCount + SimulateInteractor.CUSTOMER_RANGE + 1)
                * ratingMultiplier);
        // cook effect adds by at least 1

        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, customerCount);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(
                TEST_PLAYER_BALANCE);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final Review review = new Review(5.0, TEST_CURRENT_DAY);
        reviewManagerDataAccessObject.addReview(review);
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(
                COOK_WAGE_NO_REDUCTION,
                WAITER_WAGE_NO_REDUCTION
        );

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertTrue(minBoundCustomerCount <= outputData.getCurrentCustomerCount()
                        && outputData.getCurrentCustomerCount() <= maxBoundCustomerCount
                );
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

    /**
     * Test if simulation fails when balance is negative.
     */
    @Test
    void negativeBalanceTest() {
        final int currentBalance = -50;
        final String expectedErrorMessage = "You are bankrupt and can no longer simulate more days";
        final SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, TEST_CUSTOMER_COUNT);
        final SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        final SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(currentBalance);
        final SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        final SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(0, 0);

        final SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                fail(UNEXPECTED_SUCCESS_MESSAGE + outputData.getCurrentDay());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertEquals(expectedErrorMessage, errorMessage);
            }
        };

        final SimulateInputBoundary interactor = new SimulateInteractor(
                successPresenter,
                pantryDataAccessObject,
                reviewManagerDataAccessObject,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );
        interactor.execute(inputData);
    }

}
