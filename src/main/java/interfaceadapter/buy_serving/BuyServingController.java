package interfaceadapter.buy_serving;

import usecase.buy_serving.BuyServingInputBoundary;
import usecase.buy_serving.BuyServingInputData;

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
