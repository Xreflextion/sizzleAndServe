package use_case.simulate;

import data_access.*;

import java.util.*;

public class SimulateInteractor implements SimulateInputBoundary {
    // value to add to yesterday's customer count to determine today's possible range of customers
    private final int CUSTOMER_RANGE = 3;
    // Multiplier to impact customer count based on current rating
    private final double[] RATING_MULTIPLIER = new double[] {0.5, 0.8, 1, 1.2, 1.5};
    // Possible default rating options (equal probability)
    private final int[] NORMAL_RATING_OPTIONS = new int[] {1,2,3,4,5};
    // Possible rating options when order is incomplete (equal probability)
    private final int[] NO_STOCK_RATING_OPTIONS = new int[] {1,2,3};

    private final SimulateOutputBoundary simulatePresenter;

    private final PantryDataAccessObject pantry;

    private final RandomHelper randomHelper;

    public SimulateInteractor(SimulateOutputBoundary simulateOutputBoundary,
                              PantryDataAccessObject pantry) {
        this.simulatePresenter = simulateOutputBoundary;
        this.pantry = pantry;
        randomHelper = new RandomHelper();
    }

    @Override
    public void execute(SimulateInputData simulateInputData) {
        ArrayList<String> dishes = new ArrayList<>(pantry.getPrices().keySet());
        Map<String, Integer> stock = pantry.getStock();
        int currentBalance = simulateInputData.getCurrentBalance();
        // hardcoded
        int customerCount = getCustomerCount(3, 0, simulateInputData.getPastCustomerCount());
        ArrayList<String> orders = getOrders(customerCount, dishes);
        ArrayList<Integer> ratings = new ArrayList<>();
        int ratingsSum = 0;
        int profit = 0;
        int expenses = 0;

        // TODO Remove print statements
        System.out.println("Initial stock:");
        for (String dish: stock.keySet() ) {
            System.out.println("Dish " + dish + ": " + stock.get(dish));
        }


        for (String order: orders) {
            int newRating;
            if (stock.get(order) > 0) {
                stock.put(order, stock.get(order) - 1);
                profit += pantry.getPrices().get(order);
                newRating = randomHelper.getValueFromList(NORMAL_RATING_OPTIONS);
            } else {
                newRating = randomHelper.getValueFromList(NO_STOCK_RATING_OPTIONS);
            }
            ratings.add(newRating); // TODO Figure out how to calculate rating using employee wages
            ratingsSum += newRating;
        }

        // TODO Remove print statements
        System.out.println("Revenue: " + profit);
        System.out.println("Ratings:");
        for (int rating: ratings ) {
            System.out.println(rating);
        }
        System.out.println("Final stock:");
        for (String dish: stock.keySet() ) {
            System.out.println("Dish " + dish + ": " + stock.get(dish));
        }
        pantry.setStock(stock);

        // Managing expenses
        expenses += 20; // hardcoded

        // Changing current balance
        currentBalance += profit;
        currentBalance -= expenses;
        System.out.println("Expenses: "+ expenses);

        int avgRating = 3;
        if (!ratings.isEmpty()) {
            avgRating = ratingsSum / ratings.size();
        }
        System.out.println("Average rating: " + avgRating);

        int newDay = simulateInputData.getCurrentDay() + 1;
        // Do sth to save new day number, profit, expenses, customer count, average rating

        SimulateOutputData outputData = new SimulateOutputData(newDay, currentBalance, customerCount);
        simulatePresenter.prepareSuccessView(outputData);

        System.out.println("****");
    }


    /* Randomize the number of customers for the day depending on the rating of the restaurant and the cooks*/
    private int getCustomerCount(int rating, int cookEffect, int pastCustomerCount) {
        int lowerBound = Math.max(1, pastCustomerCount - CUSTOMER_RANGE);
        int upperBound = pastCustomerCount + CUSTOMER_RANGE;
        int customerCount = randomHelper.generateRandomInt(upperBound, lowerBound);
        if (rating > 0) {
            customerCount *= (int) RATING_MULTIPLIER[rating - 1];
        }
        return customerCount + cookEffect;
    }

    /* Randomize the customer orders */
    private ArrayList<String> getOrders(int customerCount, ArrayList<String> dishes) {
        int min = 0;
        int max = dishes.size() - 1;
        ArrayList<String> orders = new ArrayList<>();
        while (customerCount > 0) {
            customerCount --;
            int idx = randomHelper.generateRandomInt(max, min);
            System.out.println("Index: " + idx + ", Dishes: " + dishes.get(idx));
            String dish = dishes.get(idx);
            orders.add(dish);
        }
        return orders;
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

    public int getValueFromList(int[] list) {
        return list[generateRandomInt(list.length - 1, 0)];
    }

}
