package interface_adapter.buy_serving;

import use_case.buy_serving.BuyServingInputBoundary;
import use_case.buy_serving.BuyServingInputData;

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
