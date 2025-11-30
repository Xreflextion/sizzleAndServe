package usecase.buy_serving;

public class BuyServingInputData {

    private final String[] dishNames;
    private final int[] servingsToBuy;

    /**
     * Constructs a BuyServingInputData.
     * @param dishNames the list of dishes to purchase servings for
     * @param servingsToBuy the number of servings the user wants to buy
     */
    public BuyServingInputData(String[] dishNames, int[] servingsToBuy) {
        this.dishNames = dishNames;
        this.servingsToBuy = servingsToBuy;
    }

    public String[] getDishNames() {
        return dishNames;
    }

    public int[] getServingsToBuy() {
        return servingsToBuy;
    }
}