package interface_adapter.BuyServing;

import use_case.BuyServing.BuyServingInputBoundary;
import use_case.BuyServing.BuyServingInputData;

public class BuyServingController {
    private final BuyServingInputBoundary interactor;

    public BuyServingController(BuyServingInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void confirmPurchase(String[] dishNames, int[] quantities) {
        BuyServingInputData inputData = new BuyServingInputData(dishNames, quantities);
        interactor.execute(inputData);
    }
}
