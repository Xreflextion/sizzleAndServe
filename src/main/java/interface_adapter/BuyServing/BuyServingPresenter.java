package interface_adapter.BuyServing;

import use_case.BuyServing.BuyServingOutputBoundary;
import use_case.BuyServing.BuyServingOutputData;

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
        viewModel.firePropertyChange("buyServing");
    }
}