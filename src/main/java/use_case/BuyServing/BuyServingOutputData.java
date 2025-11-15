package use_case.BuyServing;

public class BuyServingOutputData {

    private final boolean success;
    private final String message;
    private final double newBalance;

    /**
     * Constructs a BuyServingOutputData.
     * @param success whether the purchase was successful
     * @param message a message to display to the user
     * @param newBalance the user's new balance after purchase
     */
    public BuyServingOutputData(boolean success, String message, double newBalance) {
        this.success = success;
        this.message = message;
        this.newBalance = newBalance;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public double getNewBalance() {
        return newBalance;
    }
}
