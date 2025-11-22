package use_case.BuyServing;

public class BuyServingOutputData {

    private final boolean success;
    private final String message;
    private final double newBalance;
    private final String[] dishNames;
    private final int[] dishCosts;
    private final int[] dishStocks;

    /**
     * Constructs a BuyServingOutputData.
     * @param success whether the purchase was successful
     * @param message a message to display to the user
     * @param newBalance the user's new balance after purchase
     * @param dishNames the names of the dishes
     * @param dishCosts the costs of the dishes
     * @param dishStocks the stocks of the dishes
     */
    public BuyServingOutputData(
            boolean success,
            String message,
            double newBalance,
            String[] dishNames,
            int[] dishCosts,
            int[] dishStocks
    ) {
        this.success = success;
        this.message = message;
        this.newBalance = newBalance;
        this.dishNames = dishNames;
        this.dishCosts = dishCosts;
        this.dishStocks = dishStocks;
    }

    public boolean isSuccess() {
        return success;
    }

    public String[] getDishNames() {
        return dishNames;
    }

    public int[] getDishCosts() {
        return dishCosts;
    }

    public int[] getDishStocks() {
        return dishStocks;
    }

    public String getMessage() {
        return message;
    }

    public double getNewBalance() {
        return newBalance;
    }
}
