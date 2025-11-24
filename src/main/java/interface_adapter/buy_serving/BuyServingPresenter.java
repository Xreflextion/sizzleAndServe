package interface_adapter.buy_serving;

import use_case.buy_serving.BuyServingOutputBoundary;
import use_case.buy_serving.BuyServingOutputData;

/**
 * The Presenter for the BuyServing Use Case.
 */
public class BuyServingPresenter implements BuyServingOutputBoundary {

    private final BuyServingViewModel viewModel;

    public BuyServingPresenter(BuyServingViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void present(BuyServingOutputData outputData) {
        viewModel.setMessage(outputData.getMessage());
        viewModel.setNewBalance(outputData.getNewBalance());
        viewModel.setSuccess(outputData.isSuccess());
        viewModel.setDishNames(outputData.getDishNames());
        viewModel.setDishCosts(outputData.getDishCosts());
        viewModel.setDishStocks(outputData.getDishStocks());
        viewModel.firePropertyChange("buyServing");
    }
}