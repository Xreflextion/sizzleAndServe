package interface_adapter.BuyServing;
import use_case.BuyServing.BuyServingInputBoundary;
import use_case.BuyServing.BuyServingInputData;

public class BuyServingController {
    private final BuyServingInputBoundary interactor;
    private final String[] dishNames;
    private final int[] quantities = new int[3]; // Quantity the player try to add to the stock

    public BuyServingController(BuyServingInputBoundary interactor, String[] dishNames, double[] dishPrices) {
        this.interactor = interactor;
        this.dishNames = dishNames;
    }

    public void increaseQuantity(int index) { quantities[index]++; }

    public void decreaseQuantity(int index) {
        quantities[index] = Math.max(0, quantities[index] - 1);
    }

    public int getQuantity(int index) {
        if (index >= 0 && index < quantities.length) {
            return quantities[index];
        }
        return 0;
    }

    public void confirmPurchase() {
        BuyServingInputData inputData = new BuyServingInputData(dishNames, quantities);
        interactor.execute(inputData);
        for (int i = 0; i < quantities.length; i++) {
            quantities[i] = 0;
        }
    }
}
