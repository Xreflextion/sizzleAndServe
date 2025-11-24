package use_case.simulate;

import entity.Pantry;
import entity.PerDayRecord;
import entity.Player;
import entity.ReviewEntity;

import java.util.*;

public class SimulateInteractor implements SimulateInputBoundary {
    // value to add to yesterday's customer count to determine today's possible range of customers
    private final int CUSTOMER_RANGE = 3;
    private final int NORMAL_RATING_UPPER_BOUND = 5;
    private final int NO_STOCK_RATING_UPPER_BOUND = 2;
    private final int RATING_LOWER_BOUND = 1;

    private final String COOK_POSITION = "Cook";
    private final String WAITER_POSITION = "Waiter";

    private final SimulateOutputBoundary simulatePresenter;

    private SimulatePantryDataAccessInterface pantryDataAccessObject;
    private SimulateReviewDataAccessInterface reviewManagerDataAccessObject;
    private SimulateWageDataAccessInterface wageDataAccessObject;
    private SimulatePlayerDataAccessInterface playerDataAccessObject;
    private SimulateDayRecordsDataAccessInterface dayRecordsDataAccessInterface;

    private final RandomHelper randomHelper;

    public SimulateInteractor(
            SimulateOutputBoundary simulateOutputBoundary,
            SimulatePantryDataAccessInterface pantryDataAccessInterface,
            SimulateReviewDataAccessInterface reviewManagerDataAccessInterface,
            SimulateWageDataAccessInterface wageDataAccessInterface,
            SimulatePlayerDataAccessInterface playerDataAccessInterface,
            SimulateDayRecordsDataAccessInterface dayRecordsDataAccessInterface) {
        this.simulatePresenter = simulateOutputBoundary;
        this.pantryDataAccessObject = pantryDataAccessInterface;
        this.reviewManagerDataAccessObject = reviewManagerDataAccessInterface;
        this.wageDataAccessObject = wageDataAccessInterface;
        this.playerDataAccessObject = playerDataAccessInterface;
        this.dayRecordsDataAccessInterface = dayRecordsDataAccessInterface;
        randomHelper = new RandomHelper();
    }

    @Override
    public void execute(SimulateInputData simulateInputData) {
        Pantry pantry = pantryDataAccessObject.getPantry();
        String[] dishes = pantry.getDishNames();
        Map<String, Integer> stock = pantryDataAccessObject.getStock();
        Map<String, Double> currentPrices = pantryDataAccessObject.getCurrentPrices();
        double currentBalance = simulateInputData.getCurrentBalance();
        // hardcoded
        int curDay = simulateInputData.getCurrentDay();
        ArrayList<Double> curRatings = reviewManagerDataAccessObject.getReviewsByDay(curDay);
        double curRating = getAverageRating(curRatings);
        double cookEffect = wageDataAccessObject.getEmployee(COOK_POSITION).getWageEffect();
        double waiterEffect = wageDataAccessObject.getEmployee(WAITER_POSITION).getWageEffect();
        System.out.println("cur rating" + curRating);
        System.out.println("cook effect" + cookEffect);
        System.out.println("waiter" + waiterEffect);
        int pastCustomerCount = simulateInputData.getPastCustomerCount();
        if (curDay == 0 ) {
            pastCustomerCount = 5;
        }
        int customerCount = getCustomerCount(curRating, cookEffect, pastCustomerCount);
        ArrayList<String> orders = getOrders(customerCount, dishes);
        ArrayList<Double> ratings = new ArrayList<>();
        double ratingsSum = 0;
        double profit = 0;
        double revenue = 0;
        double expenses = 0;

        // TODO Remove print statements
        System.out.println(pantryDataAccessObject.getStock());

        for (String order: orders) {
            double newRating;

            if (stock.get(order) > 0) {
                stock.put(order, stock.get(order) - 1);
                revenue += currentPrices.get(order);
                newRating = generateRating(NORMAL_RATING_UPPER_BOUND, RATING_LOWER_BOUND, waiterEffect);
            } else {
                newRating = generateRating(NO_STOCK_RATING_UPPER_BOUND, RATING_LOWER_BOUND, waiterEffect);
            }
            ratings.add(newRating); // TODO Figure out how to calculate rating using employee wages
            ratingsSum += newRating;
        }


        int newDay = curDay + 1;
        // TODO Remove print statements
        System.out.println("Revenue: " + revenue);
        System.out.println("Ratings:");

        // Saving reviews
        for (double rating: ratings ) {
            ReviewEntity reviewEntity = new ReviewEntity(rating, newDay);
            reviewManagerDataAccessObject.addReview(reviewEntity);
            System.out.println(rating);
        }

        // Saving stock
        pantryDataAccessObject.saveStock(stock);

        // Managing expenses
        expenses += 20; // hardcoded

        // Changing current balance
        currentBalance += revenue;
        currentBalance -= expenses;
        System.out.println("Expenses: "+ expenses);

        double avgRating = 3;
        if (!ratings.isEmpty()) {
            avgRating = ratingsSum / ratings.size();
        }
        System.out.println("Average rating: " + avgRating);


        // Saving player balance
        Player player = playerDataAccessObject.getPlayer();
        player.setBalance(currentBalance);
        playerDataAccessObject.savePlayer(player);

        // Saving day record
        PerDayRecord newDayRecord = new PerDayRecord(revenue, expenses, avgRating);
        dayRecordsDataAccessInterface.saveNewData(newDayRecord);

        // Generating output data
        SimulateOutputData outputData = new SimulateOutputData(newDay, currentBalance, customerCount);
        simulatePresenter.prepareSuccessView(outputData);

        System.out.println(playerDataAccessObject.getPlayer().getBalance());
        System.out.println(pantryDataAccessObject.getStock());

        System.out.println("****");
    }



    private double roundToOneDecimalPlace(double number) {
        return (double) Math.round(number * 10.0) / 10.0;
    }


    /**
     * Get the average rating of a list of ratings. Default is 3 if no ratings exist
     * @param ratings list of ratings
     * @return
     */
    private double getAverageRating(ArrayList<Double> ratings) {
        if (ratings == null || ratings.isEmpty()) {
            return 3;
        }
        double sum = 0;
        for (double rating: ratings) {
            sum += rating;
        }

        return roundToOneDecimalPlace(sum/ratings.size());
    }


    /**
     * Get a multiplier to impact customer count based on current rating
     * @param rating
     * @return multiplier to use on the randomized customer count
     */
    private double generateRatingMultiplier(double rating) {
        if (rating == 3.0) {
            return 1.0;
        }
        else if (rating > 3.0) {
            return 0.5*(rating - 3.0) + 1.0;
        } else {
            return 0.25*(rating - 3.0) + 1.0;
        }
    }


    /** Randomize the number of customers for the day depending on the
     * rating of the restaurant and the cook effect.
     * Assumes 1 <= rating <= 5
     * */
    private int getCustomerCount(double rating, double cookEffect, int pastCustomerCount) {
        int lowerBound = Math.max(1, pastCustomerCount - CUSTOMER_RANGE);
        int upperBound = pastCustomerCount + CUSTOMER_RANGE;
        double customerCount = randomHelper.generateRandomInt(upperBound, lowerBound);
        if (rating > 0) {
            System.out.println("prev cus count");
            System.out.println(customerCount);
            System.out.println("result of rating");
            System.out.println(generateRatingMultiplier(rating));
            customerCount *= generateRatingMultiplier(rating);
            System.out.println("new cus count");
            System.out.println(customerCount);
        }


        return (int) (customerCount * cookEffect);
    }

    /* Randomize the customer orders */
    private ArrayList<String> getOrders(int customerCount, String[] dishes) {
        int min = 0;
        int max = dishes.length - 1;
        ArrayList<String> orders = new ArrayList<>();
        while (customerCount > 0) {
            customerCount --;
            int idx = randomHelper.generateRandomInt(max, min);
            String dish = dishes[idx];
            orders.add(dish);
        }
        return orders;
    }



    /**
     * Generate a customer rating given a range between 1 and 5.
     * @param waiterEffect the waiter effect that impacts customer rating
     * @return rating the rating
     */
    private double generateRating(int max, int min, double waiterEffect) {
        int number = randomHelper.generateRandomInt(max*10, min*10);
        double rating = number / 10; // the value will range be in the list [1.0, 1.1, ... 5.0] if range given is 1-5
        System.out.println("iniital rating" + rating);
        rating *= waiterEffect;
        System.out.println("after waiter"+ rating);
        if (rating > 5.0) {
            rating = 5.0;
        }
        if (rating < 1.0) {
            rating = 1.0;
        }
        return roundToOneDecimalPlace(rating);
    }

}

class RandomHelper {
    private final Random randomGenerator;

    public RandomHelper() {
        randomGenerator = new Random();
    }

    public int generateRandomInt(int max, int min) {
        return randomGenerator.nextInt((max - min) + 1) + min;
    }

}
