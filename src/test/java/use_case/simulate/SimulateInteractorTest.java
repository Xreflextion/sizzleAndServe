package use_case.simulate;
import entity.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SimulateInteractorTest {
    private final String UNEXPECTED_FAILURE_MESSAGE = "Use case failure is unexpected: ";

    private final int TEST_PLAYER_BALANCE = 100;
    private final int TEST_CUSTOMER_COUNT = 5;
    private final int TEST_CURRENT_DAY = 0;

    private final int TEST_BASE_PRICE = 5;
    private final String TEST_RECIPE_ONE_NAME = "One";
    private final String TEST_RECIPE_TWO_NAME = "Two";
    private final String TEST_RECIPE_THREE_NAME = "Three";

    private final int BASE_COOK_WAGE = 50;
    private final int BASE_WAITER_WAGE = 50;
    private final int COOK_WAGE_NO_REDUCTION = (int)(50 + 10*(SimulateInteractor.COOK_EFFECT_REDUCTION - 1.0)/0.2);
    private final int WAITER_WAGE_NO_REDUCTION = (int)(50 + 10*(SimulateInteractor.WAITER_EFFECT_REDUCTION - 1.0)/0.2);

    private SimulatePantryDataAccessInterface generatePantryDataAccessObject() {
        Recipe recipe1 = new Recipe(TEST_RECIPE_ONE_NAME, TEST_BASE_PRICE);
        Recipe recipe2 = new Recipe(TEST_RECIPE_TWO_NAME, TEST_BASE_PRICE);
        Recipe recipe3 = new Recipe(TEST_RECIPE_THREE_NAME, TEST_BASE_PRICE);
        Pantry pantry = new Pantry(recipe1, recipe2, recipe3);

        return new SimulatePantryDataAccessInterface() {
            @Override
            public Pantry getPantry() {
                return pantry;
            }

            @Override
            public Map<String, Integer> getStock() {
                Map<String, Integer> stock = new HashMap<>();
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
                Map<String, Double> prices = new HashMap<>();
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
        Map<Integer, ArrayList<Double>> reviewManager = new HashMap<>();
        return new SimulateReviewDataAccessInterface() {
            @Override
            public void addReview(ReviewEntity reviewEntity) {
                if (!reviewManager.containsKey(reviewEntity.getDayNum())) {
                    reviewManager.put(reviewEntity.getDayNum(), new ArrayList<>());
                }
                reviewManager.get(reviewEntity.getDayNum()).add(reviewEntity.getRating());
            }

            @Override
            public ArrayList<Double> getReviewsByDay(int day) {
                ArrayList<Double> reviews = reviewManager.get(day);
                if (reviews == null) {
                    return new ArrayList<>();
                }
                return reviews;
            }
        };
    }

    private SimulateWageDataAccessInterface generateWageDataAccessObject(int cookWage, int waiterWage) {
        return new SimulateWageDataAccessInterface() {
            @Override
            public Employee getEmployee(String position) {
                if (position == SimulateInteractor.COOK_POSITION) {
                    return new Employee(cookWage, SimulateInteractor.COOK_POSITION);
                }
                if (position == SimulateInteractor.WAITER_POSITION) {
                    return new Employee(waiterWage, SimulateInteractor.WAITER_POSITION);
                }
                return null;
            }

            @Override
            public int getTotalWage() {
                return waiterWage + cookWage;
            }
        };
    }


    private SimulateDayRecordsDataAccessInterface generateDayRecordsDataAccessObject() {
        List<PerDayRecord> dayRecords = new ArrayList<PerDayRecord>();
        return new SimulateDayRecordsDataAccessInterface() {
            @Override
            public void saveNewData(PerDayRecord dayRecord) {
                dayRecords.add(dayRecord);
            }

            @Override
            public PerDayRecord getDayData(int day) {
                return dayRecords.get(day-1);
            }
        };
    }


    /**
     * Test if simulation succeeds to return the next day
     */
    @Test
    void nextDaySuccessTest() {
        int expectedNextDay = TEST_CURRENT_DAY + 1;
        SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY, TEST_CUSTOMER_COUNT);
        SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(TEST_PLAYER_BALANCE);
        SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(BASE_COOK_WAGE, BASE_WAITER_WAGE);

        SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertEquals(expectedNextDay, outputData.getCurrentDay());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };


        SimulateInputBoundary interactor = new SimulateInteractor(
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
     * Test if simulation succeeds with no stock
     */
    @Test
    void noStockSuccessTest() {
        int expectedCurrentBalance = TEST_PLAYER_BALANCE;
        SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY,  TEST_CUSTOMER_COUNT);
        SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(TEST_PLAYER_BALANCE);
        SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(0, 0);


        SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertEquals(expectedCurrentBalance, outputData.getCurrentBalance());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        SimulateInputBoundary interactor = new SimulateInteractor(
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
     * Test if simulation succeeds with current balance reduced due to employee expenses
     */
    @Test
    void employeeExpensesApplied() {
        int waiterWage = BASE_WAITER_WAGE*35;
        int currentBalance = 100 + BASE_COOK_WAGE + waiterWage;
        int expectedCurrentBalance = currentBalance - BASE_COOK_WAGE - waiterWage;
        SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY,  TEST_CUSTOMER_COUNT);
        SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(currentBalance);
        SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(BASE_COOK_WAGE, waiterWage);


        SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertEquals(expectedCurrentBalance, outputData.getCurrentBalance());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        SimulateInputBoundary interactor = new SimulateInteractor(
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
     * Test if customer count is within range
     */
    @Test
    void customerCountWithinRange() {
        int customerCount = 50;
        int minBoundCustomerCount = customerCount - SimulateInteractor.CUSTOMER_RANGE;
        int maxBoundCustomerCount = customerCount + SimulateInteractor.CUSTOMER_RANGE + 1;
        // cook effect adds by at least 1

        SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY,  customerCount);
        SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(TEST_PLAYER_BALANCE);
        SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(
                COOK_WAGE_NO_REDUCTION,
                WAITER_WAGE_NO_REDUCTION
        );

        SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
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

        SimulateInputBoundary interactor = new SimulateInteractor(
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
     * Test if revenue generated from orders is within a range
     */
    @Test
    void revenueMadeWithinRange() {

        int customerCount = 50;
        int minBoundCustomerCount = customerCount - SimulateInteractor.CUSTOMER_RANGE;
        int maxBoundCustomerCount = customerCount + SimulateInteractor.CUSTOMER_RANGE + 1;
        // cook effect adds by at least 1

        int minBoundExpectedBalance = TEST_PLAYER_BALANCE + TEST_BASE_PRICE*minBoundCustomerCount;
        int maxBoundExpectedBalance = TEST_PLAYER_BALANCE + TEST_BASE_PRICE*maxBoundCustomerCount;

        SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY,  customerCount);
        SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        Map<String, Integer> stock = new HashMap<>();
        stock.put(TEST_RECIPE_ONE_NAME, maxBoundCustomerCount);
        stock.put(TEST_RECIPE_TWO_NAME, maxBoundCustomerCount);
        stock.put(TEST_RECIPE_THREE_NAME, maxBoundCustomerCount);
        pantryDataAccessObject.saveStock(stock);
        int totalStockCount = maxBoundCustomerCount*3;
        SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(TEST_PLAYER_BALANCE);
        SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(0, 0);

        SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
            @Override
            public void prepareSuccessView(SimulateOutputData outputData) {
                assertTrue(minBoundExpectedBalance <= outputData.getCurrentBalance()
                        && outputData.getCurrentBalance() <= maxBoundExpectedBalance
                );
                int stockCount = 0;
                for (String name: outputData.getStock().keySet()) {
                    stockCount += outputData.getStock().get(name);
                }
                assertTrue(totalStockCount == stockCount + outputData.getCurrentCustomerCount());
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail(UNEXPECTED_FAILURE_MESSAGE + errorMessage);
            }
        };

        SimulateInputBoundary interactor = new SimulateInteractor(
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
     * Test if customer count is within range when considering reviews
     */
    @Test
    void customerCountWithinRangeWithReviews() {
        double ratingMultiplier = 1.5;
        int customerCount = 50;
        int minBoundCustomerCount = (int) ((customerCount - SimulateInteractor.CUSTOMER_RANGE) * ratingMultiplier);
        int maxBoundCustomerCount = (int) ((customerCount + SimulateInteractor.CUSTOMER_RANGE + 1) * ratingMultiplier);
        // cook effect adds by at least 1

        SimulateInputData inputData = new SimulateInputData(TEST_CURRENT_DAY,  customerCount);
        SimulatePantryDataAccessInterface pantryDataAccessObject = generatePantryDataAccessObject();
        SimulatePlayerDataAccessInterface playerDataAccessObject = generatePlayerDataAccessObject(TEST_PLAYER_BALANCE);
        SimulateReviewDataAccessInterface reviewManagerDataAccessObject = generateReviewDataAccessObject();
        ReviewEntity review = new ReviewEntity(5.0, TEST_CURRENT_DAY);
        reviewManagerDataAccessObject.addReview(review);
        SimulateDayRecordsDataAccessInterface dayRecordsDataAccessObject = generateDayRecordsDataAccessObject();
        SimulateWageDataAccessInterface wageDataAccessObject = generateWageDataAccessObject(
                COOK_WAGE_NO_REDUCTION,
                WAITER_WAGE_NO_REDUCTION
        );

        SimulateOutputBoundary successPresenter = new SimulateOutputBoundary() {
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

        SimulateInputBoundary interactor = new SimulateInteractor(
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
