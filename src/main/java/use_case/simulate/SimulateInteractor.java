package use_case.simulate;

import entity.Pantry;
import entity.PerDayRecord;
import entity.Player;
import entity.ReviewEntity;

import java.util.*;

public class SimulateInteractor implements SimulateInputBoundary {
    // value to add to yesterday's customer count to determine today's possible range of customers
    private final int CUSTOMER_RANGE = 2;
    private final int NORMAL_RATING_UPPER_BOUND = 5;
    private final int NO_STOCK_RATING_UPPER_BOUND = 2;
    private final int RATING_LOWER_BOUND = 1;

    private final String COOK_POSITION = "Cook";
    private final String WAITER_POSITION = "Waiter";
    private final double COOK_EFFECT_REDUCTION = 1.2;
    // Amount to subtract from cook effect to allow cook effect to impact customer count negatively
    private final double WAITER_EFFECT_REDUCTION = 1.6;
    // Amount to subtract from waiter effect to allow waiter effect to impact reviews negatively

    private final SimulateOutputBoundary simulatePresenter;

    private SimulatePantryDataAccessInterface pantryDataAccessObject;
    private SimulateReviewDataAccessInterface reviewManagerDataAccessObject;
    private SimulateWageDataAccessInterface wageDataAccessObject;
    private SimulatePlayerDataAccessInterface playerDataAccessObject;
    private SimulateDayRecordsDataAccessInterface dayRecordsDataAccessInterface;

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
        randomGenerator = new Random();
    }

    @Override
    public void execute(SimulateInputData simulateInputData) {

        Pantry pantry = pantryDataAccessObject.getPantry();
        String[] dishes = pantry.getDishNames();
        Map<String, Integer> stock = pantryDataAccessObject.getStock();
        Map<String, Double> currentPrices = pantryDataAccessObject.getCurrentPrices();
        System.out.println(pantryDataAccessObject.getCurrentPrices());

        double currentBalance = playerDataAccessObject.getPlayer().getBalance();
        int curDay = simulateInputData.getCurrentDay();
        int pastCustomerCount = simulateInputData.getCurrentCustomerCount();

        ArrayList<Double> curRatings = reviewManagerDataAccessObject.getReviewsByDay(curDay);
        double curRating = getAverageRating(curRatings);

        double cookEffect = wageDataAccessObject.getEmployee(COOK_POSITION).getWageEffect();
        double waiterEffect = wageDataAccessObject.getEmployee(WAITER_POSITION).getWageEffect();
        System.out.println("cookEffect: " + cookEffect);
        System.out.println("waiterEffect: " + waiterEffect);

        // get customer count & orders
        int customerCount = getCustomerCount(curRating, cookEffect, pastCustomerCount);
        ArrayList<String> orders = getOrders(customerCount, dishes);

        // initialize variables
        ArrayList<Double> ratings = new ArrayList<>();
        double ratingsSum = 0;
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
                // No stock: rating cannot be max
                newRating = generateRating(NO_STOCK_RATING_UPPER_BOUND, RATING_LOWER_BOUND, waiterEffect);
            }
            ratings.add(newRating);
            ratingsSum += newRating;
        }


        int newDay = curDay + 1;

        // Saving reviews
        for (double rating: ratings ) {
            ReviewEntity reviewEntity = new ReviewEntity(rating, newDay);
            reviewManagerDataAccessObject.addReview(reviewEntity);
        }

        // Saving stock
        pantryDataAccessObject.saveStock(stock);

        // Managing expenses
        expenses += wageDataAccessObject.getTotalWage();

        // Changing current balance
        currentBalance += revenue;
        currentBalance -= expenses;

        // Saving player balance
        Player player = playerDataAccessObject.getPlayer();
        player.setBalance(currentBalance);
        playerDataAccessObject.savePlayer(player);

        double avgRating = 3;
        if (!ratings.isEmpty()) {
            avgRating = ratingsSum / ratings.size();
        }

        // Saving day record
        PerDayRecord newDayRecord = new PerDayRecord(revenue, expenses, avgRating);
        dayRecordsDataAccessInterface.saveNewData(newDayRecord);

        // Generating output data
        SimulateOutputData outputData = new SimulateOutputData(newDay, currentBalance, customerCount);
        simulatePresenter.prepareSuccessView(outputData);

        System.out.println(playerDataAccessObject.getPlayer().getBalance());
        System.out.println(pantryDataAccessObject.getStock());
        System.out.println("Expenses: " + dayRecordsDataAccessInterface.getDayData(newDay).getExpenses());
        System.out.println("Revenue: " + dayRecordsDataAccessInterface.getDayData(newDay).getRevenue());
        System.out.println("Profit: " + dayRecordsDataAccessInterface.getDayData(newDay).getProfit());
        System.out.println("Rating: " + dayRecordsDataAccessInterface.getDayData(newDay).getRating());
        System.out.println(reviewManagerDataAccessObject.getReviewsByDay(newDay));

        System.out.println("****");
    }


    /**
     * Get the average rating of a list of ratings. Default is 3 if no ratings exist
     * @param ratings list of ratings
     * @return average rating
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
     * Get a multiplier to impact new customer count based on current rating
     * @param rating the rating
     * @return multiplier to use on the randomized customer count
     */
    private double generateRatingMultiplier(double rating) {
        return 0.25*(rating - 3.0) + 1.0;
    }


    /** Randomize the number of customers for the day depending on the
     * rating of the restaurant and the cook effect.
     * Assumes 1 <= rating <= 5
     * @param rating the rating
     * @param cookEffect the cook effect, larger than 1.0
     * @param pastCustomerCount the customer count from the previous day
     * */
    private int getCustomerCount(double rating, double cookEffect, int pastCustomerCount) {
        // Step 1: Generate a random customer count that is based on the previous day's count
        // The range is [max(1, pastCustomerCount - CUSTOMER_RANGE), pastCustomerCount + CUSTOMER_RANGE]
        int lowerBound = Math.max(1, pastCustomerCount - CUSTOMER_RANGE);
        int upperBound = pastCustomerCount + CUSTOMER_RANGE;
        double customerCount = generateRandomInt(upperBound, lowerBound);

        System.out.println("orig cus count");
        System.out.println(customerCount);
        // Step 2: Add some customers to the count based on the cook effect
        // - We divide a COOK_EFFECT_REDUCTION from the cookEffect so cookEffect is less effective under a threshold
        // - We multiply A by customerCount (B)
        // - We multiply B by 1/(customerCount - 0.7) so if the customerCount was small, we will increase it more
        double cookEffectAddition = (cookEffect/COOK_EFFECT_REDUCTION)*customerCount*(1/(customerCount - 0.7));
        System.out.println("cook effect addition and result " + cookEffectAddition + " " + customerCount);
        customerCount += cookEffectAddition;
        if (rating > 0) {

            System.out.println("result of rating");
            System.out.println(generateRatingMultiplier(rating));
            // Step 3: Multiply the customerCount by a rating multiplier
            // Follows the function: f(x) = 0.25*(x - 3.0) + 1.0
            customerCount *= generateRatingMultiplier(rating);
            System.out.println("new cus count");
            System.out.println(customerCount);
        }

        return (int) customerCount;
    }

    /** Generate a list of random customer orders
     *
     * @param customerCount number of customers
     * @param dishes dish options
     * @return a list of dish names where each item represents an order
     */
    private ArrayList<String> getOrders(int customerCount, String[] dishes) {
        int min = 0;
        int max = dishes.length - 1;
        ArrayList<String> orders = new ArrayList<>();
        while (customerCount > 0) {
            customerCount --;
            int idx = generateRandomInt(max, min);
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
        // Step 1: Generate a random number between the max and min, both multiplied by 10
        // They are multiplied by 10 so we can divide by 10 and get a larger variety of ratings
        int number = generateRandomInt(max*10, min*10);
        double rating = number / 10.0; // the value will range be in the list [1.0, 1.1, ... 5.0] if range given is 1-5
        // Step 2: Add the waiterEffect / rating to the rating
        // - This is done so the rating increases by a larger amount if it is smaller at first
        // - Subtract WAITER_EFFECT_REDUCTION from waiterEffect so the waiter wage could negatively impact the rating
        System.out.println("rating before waiter effect" + rating);
        rating += ((waiterEffect - WAITER_EFFECT_REDUCTION)/rating);
        // Step 3: Make sure rating is within the interval [1.0, 5.0]
        if (rating > 5.0) {
            rating = 5.0;
        }
        if (rating < 1.0) {
            rating = 1.0;
        }
        // Step 4: round to one decimal place
        return roundToOneDecimalPlace(rating);
    }

    /**
     * Generate a random integer between a range (inclusive)
     * @param max max value
     * @param min min value
     * @return random value between max and min
     */
    public int generateRandomInt(int max, int min) {
        return randomGenerator.nextInt((max - min) + 1) + min;
    }

    /**
     * Round a number to one decimal place
     * @param number to round
     * @return number rounded to one decimal place
     */
    private double roundToOneDecimalPlace(double number) {
        return (double) Math.round(number * 10.0) / 10.0;
    }

}

