package interface_adapter.buy_serving;

import use_case.buy_serving.BuyServingInputBoundary;
import use_case.buy_serving.BuyServingInputData;

public class BuyServingController {
    private final BuyServingInputBoundary interactor;

    public BuyServingController(BuyServingInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Initiates the buy serving use case by converting the user's selected dishes
     * and quantities into an input data object and passing it to the interactor.
     *
     * @param dishNames  the names of the dishes the user wants to purchase
     * @param quantities the corresponding quantities for each dish name
     */
    public void confirmPurchase(String[] dishNames, int[] quantities) {
        final BuyServingInputData inputData = new BuyServingInputData(dishNames, quantities);
        interactor.execute(inputData);
    }
}
