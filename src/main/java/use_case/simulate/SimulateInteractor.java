package use_case.simulate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import entity.PerDayRecord;
import entity.Player;
import entity.ReviewEntity;

public class SimulateInteractor implements SimulateInputBoundary {
    public static final int CUSTOMER_RANGE = 2;
    // value to add to yesterday's customer count to determine today's possible range of customers
    public static final double COOK_EFFECT_REDUCTION = 1.2;
    // Amount to subtract from cook effect to allow cook effect to impact customer count negatively
    public static final double WAITER_EFFECT_REDUCTION = 0.8;
    // Amount to subtract from waiter effect to allow waiter effect to impact reviews negatively
    public static final String COOK_POSITION = "Cook";
    public static final String WAITER_POSITION = "Waiter";
    public static final String BANKRUPT_ERROR_MESSAGE = "You are bankrupt and can no longer simulate more days";

    private final SimulateOutputBoundary simulatePresenter;

    private final SimulatePantryDataAccessInterface pantryDataAccessObject;
    private final SimulateReviewDataAccessInterface reviewManagerDataAccessObject;
    private final SimulateWageDataAccessInterface wageDataAccessObject;
    private final SimulatePlayerDataAccessInterface playerDataAccessObject;
    private final SimulateDayRecordsDataAccessInterface dayRecordsDataAccessInterface;

    private final Random randomGenerator;

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
        this.randomGenerator = new Random();
    }

    @Override
    public void execute(SimulateInputData simulateInputData) {
        if (playerDataAccessObject.getPlayer().getBalance() < 0.0) {
            simulatePresenter.prepareFailView(BANKRUPT_ERROR_MESSAGE);
        }
        else {
            // calculate current day
            final int currentDay = simulateInputData.getPreviousDay() + 1;

            // get today's customer count
            final int newCustomerCount = getCustomerCount(
                    simulateInputData.getPreviousDay(),
                    simulateInputData.getPreviousCustomerCount()
            );

            final String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();
            final Map<String, Integer> stock = pantryDataAccessObject.getStock();

            // get orders that can be done and can't be done
            final Map<String, Integer> orders = getOrders(newCustomerCount, dishNames);
            final Map<String, Integer> doableOrders = new HashMap<>();
            final Map<String, Integer> impossibleOrders = new HashMap<>();
            for (String dishName: dishNames) {
                doableOrders.put(dishName, Math.min(stock.get(dishName), orders.get(dishName)));
                impossibleOrders.put(dishName, Math.max(orders.get(dishName) - stock.get(dishName), 0));
            }

            // reduce stock for doable orders
            for (String dishName: dishNames) {
                stock.put(dishName, stock.get(dishName) - doableOrders.get(dishName));
            }
            // Saving stock
            pantryDataAccessObject.saveStock(stock);

            // Get revenue for doable orders
            double revenue = 0;
            for (String dishName: dishNames) {
                revenue += pantryDataAccessObject.getCurrentPrices().get(dishName) * doableOrders.get(dishName);
            }

            // Get expenses for today
            final double expenses = wageDataAccessObject.getTotalWage();

            // Get ratings for today
            final ArrayList<Double> newRatings = getCurrentRatings(dishNames, doableOrders, impossibleOrders);
            // Saving ratings
            for (double rating: newRatings) {
                reviewManagerDataAccessObject.addReview(new ReviewEntity(rating, simulateInputData.getPreviousDay()));
            }

            // Current balance management
            final double currentBalance = roundToTwoDecimalPlace(
                    playerDataAccessObject.getPlayer().getBalance() + revenue - expenses
            );
            final Player player = playerDataAccessObject.getPlayer();
            player.setBalance(currentBalance);
            playerDataAccessObject.savePlayer(player);

            // Saving day record
            final PerDayRecord previousDay = dayRecordsDataAccessInterface.getDayData(
                    simulateInputData.getPreviousDay()
            );
            final PerDayRecord previousDayEdited = new PerDayRecord(
                    roundToTwoDecimalPlace(revenue),
                    roundToTwoDecimalPlace(expenses + previousDay.getExpenses()),
                    getAvgRating(newRatings)
            );
            dayRecordsDataAccessInterface.updateDayData(simulateInputData.getPreviousDay(), previousDayEdited);
            dayRecordsDataAccessInterface.saveNewData(new PerDayRecord(0, 0, 0));
            // Generating output data
            final SimulateOutputData outputData = new SimulateOutputData(
                    currentDay,
                    currentBalance,
                    newCustomerCount,
                    stock
            );
            simulatePresenter.prepareSuccessView(outputData);
        }

    }

    /**
     * Generate average rating from list of ratings.
     * @param ratings list of ratings that is non-empty
     * @return average rating
     */
    private double getAvgRating(ArrayList<Double> ratings) {
        double ratingsSum = 0;
        for (double rating: ratings) {
            ratingsSum += rating;
        }
        return roundToOneDecimalPlace(ratingsSum / ratings.size());
    }

    /**
     * Generate ratings for the given doable and impossible orders.
     * @param dishNames dish names
     * @param doableOrders mapping of dish name to number of orders that can be done
     * @param impossibleOrders mapping of dish name to number of orders that can't be done
     * @return list of ratings
     */
    private ArrayList<Double> getCurrentRatings(
            String[] dishNames,
            Map<String, Integer> doableOrders,
            Map<String, Integer> impossibleOrders
    ) {
        final double waiterEffect = wageDataAccessObject.getEmployee(WAITER_POSITION).getWageEffect();
        final ArrayList<Double> ratings = new ArrayList<>();

        for (String dishName: dishNames) {
            final int noStockRatingUpperBound = 2;
            final int normalRatingUpperBound = 5;
            final int ratingLowerBound = 1;

            for (int i = 0; i < doableOrders.get(dishName); i++) {
                final double newRating = generateRating(normalRatingUpperBound, ratingLowerBound, waiterEffect);
                ratings.add(newRating);
            }

            for (int i = 0; i < impossibleOrders.get(dishName); i++) {
                final double newRating = generateRating(noStockRatingUpperBound, ratingLowerBound, waiterEffect);
                ratings.add(newRating);
            }

        }

        return ratings;
    }

    /**
     * Get the average rating of a list of ratings. Default is 3 if no ratings exist
     * @param ratings list of ratings
     * @return average rating
     */
    private double getAverageRating(ArrayList<Double> ratings) {
        // Start with default rating
        final double defaultRating = 3.0;
        double avgRating = defaultRating;

        if (!ratings.isEmpty()) {
            double sum = 0;
            for (double rating: ratings) {
                sum += rating;
            }
            avgRating = sum / ratings.size();
        }
        return roundToOneDecimalPlace(avgRating);
    }

    /**
     * Get a multiplier to impact new customer count based on current rating.
     * @param rating the rating
     * @return multiplier to use on the randomized customer count
     */
    private double generateRatingMultiplier(double rating) {
        final double neutralRating = 3.0;
        final double intercept = 1.0;
        final double ratingSlope = 0.25;
        return ratingSlope * (rating - neutralRating) + intercept;
    }

    /**
     * Randomize the number of customers for the day.
     * @param pastDay the previous day
     * @param pastCustomerCount the customer count from the previous day
     * @return customer count
     * */
    private int getCustomerCount(int pastDay, int pastCustomerCount) {
        final ArrayList<Double> previousRatings = reviewManagerDataAccessObject.getReviewsByDay(pastDay);
        final double rating = getAverageRating(previousRatings);
        final double cookEffect = wageDataAccessObject.getEmployee(COOK_POSITION).getWageEffect();

        // Step 1: Generate a random customer count that is based on the previous day's count
        // The range is [max(1, pastCustomerCount - CUSTOMER_RANGE), pastCustomerCount + CUSTOMER_RANGE]
        final int lowerBound = Math.max(1, pastCustomerCount - CUSTOMER_RANGE);
        final int upperBound = pastCustomerCount + CUSTOMER_RANGE;
        double customerCount = generateRandomInt(upperBound, lowerBound);

        // Step 2: Add some customers to the count based on the cook effect
        final double cookEffectAddition = getCookEffectAddition(cookEffect, customerCount);
        customerCount += cookEffectAddition;

        // Step 3: Multiply the customerCount by a rating multiplier
        // Follows the function: f(x) = 0.25 * (x - 3.0) + 1.0
        customerCount *= generateRatingMultiplier(rating);
        return (int) customerCount;
    }

    /**
     * Add some customers to the current customer count based on the cook effect.
     * @param cookEffect cook effect
     * @param customerCount customer count
     * @return number of additional customers to add to customer count (not rounded yet)
     */
    private double getCookEffectAddition(double cookEffect, double customerCount) {
        final double customerCountThreshold = 0.7;
        // - We divide a COOK_EFFECT_REDUCTION from the cookEffect so cookEffect is less effective under a threshold
        // - We multiply A by customerCount (B)
        // - We multiply B by 1/(customerCount - CUSTOMER_COUNT_THRESHOLD)
        // - CUSTOMER_COUNT_THRESHOLD < 1 so if the customerCount was small, we will increase customer count by more
        return (cookEffect / COOK_EFFECT_REDUCTION) * customerCount * (1 / (customerCount - customerCountThreshold));
    }

    /**
     * Generate random customer orders.
     * @param customerCount number of customers
     * @param dishes dish options
     * @return a mapping of dish names to number of orders related to them
     */
    private Map<String, Integer> getOrders(int customerCount, String[] dishes) {
        final int min = 0;
        final int max = dishes.length - 1;

        final Map<String, Integer> orders = new HashMap<>();
        for (String dish: dishes) {
            orders.put(dish, 0);
        }

        int loopVariable = customerCount;
        while (loopVariable > 0) {
            loopVariable--;
            final int idx = generateRandomInt(max, min);
            final String dish = dishes[idx];
            orders.put(dish, orders.get(dish) + 1);
        }
        return orders;
    }

    /**
     * Generate a customer rating given a range between 1 and 5.
     * @param max upper bound for possible value of randomized rating
     * @param min lower bound for possible value of randomized rating
     * @param waiterEffect the waiter effect that impacts customer rating
     * @return rating the rating
     */
    private double generateRating(int max, int min, double waiterEffect) {
        final double maxRating = 5.0;
        final double minRating = 1.0;
        final int scaleFactor = 10;

        // Step 1: Generate a random number between the max and min, both multiplied by SCALE_FACTOR
        // They are multiplied by SCALE_FACTOR so we can divide by SCALE_FACTOR and get a larger variety of ratings
        final int number = generateRandomInt(max * scaleFactor, min * scaleFactor);
        double rating = number / (double) scaleFactor;
        // Ex. the value will range be in the list [1.0, 1.1, ... 5.0] if range given is 1-5 and SCALE_FACTOR = 10

        // Step 2: Add the waiterEffect / rating to the rating
        // - This is done so the rating increases by a larger amount if it is smaller at first
        // - Subtract WAITER_EFFECT_REDUCTION from waiterEffect so the waiter wage could negatively impact the rating
        rating += (waiterEffect - WAITER_EFFECT_REDUCTION) / rating;

        // Step 3: Make sure rating is within the interval [1.0, 5.0]
        rating = Math.min(maxRating, rating);
        rating = Math.max(minRating, rating);

        // Step 4: round to one decimal place
        return roundToOneDecimalPlace(rating);
    }

    /**
     * Generate a random integer between a range (inclusive).
     * @param max max value
     * @param min min value
     * @return random value between max and min
     */
    public int generateRandomInt(int max, int min) {
        return randomGenerator.nextInt((max - min) + 1) + min;
    }

    /**
     * Round a number to one decimal place.
     * @param number to round
     * @return number rounded to one decimal place
     */
    private double roundToOneDecimalPlace(double number) {
        final double decimalShift = 10.0;
        return Math.round(number * decimalShift) / decimalShift;
    }

    /**
     * Round a number to two decimal place.
     * @param number to round
     * @return number rounded to two decimal place
     */
    private double roundToTwoDecimalPlace(double number) {
        final double decimalShift = 10.00;
        return Math.round(number * decimalShift) / decimalShift;
    }

}

