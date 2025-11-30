package interfaceadapter.buy_serving;

import interfaceadapter.manage_wages.WageViewModel;
import interfaceadapter.office.OfficeViewModel;
import usecase.buy_serving.BuyServingOutputBoundary;
import usecase.buy_serving.BuyServingOutputData;

/**
 * The Presenter for the BuyServing Use Case.
 */
public class BuyServingPresenter implements BuyServingOutputBoundary {

    private final BuyServingViewModel viewModel;
    private final OfficeViewModel officeViewModel;
    private final WageViewModel wageViewModel;

    public BuyServingPresenter(BuyServingViewModel viewModel,
                                OfficeViewModel officeViewModel,
                               WageViewModel wageViewModel) {
        this.viewModel = viewModel;
        this.officeViewModel = officeViewModel;
        this.wageViewModel = wageViewModel;
    }

    @Override
    public void present(BuyServingOutputData outputData) {
        viewModel.setMessage(outputData.getMessage());
        viewModel.setNewBalance(outputData.getNewBalance());
        viewModel.setSuccess(outputData.isSuccess());
        if (outputData.getDishCosts() != null) {
            viewModel.setDishCosts(outputData.getDishCosts());
        }
        if (outputData.getDishStocks() != null) {
            viewModel.setDishStocks(outputData.getDishStocks());
        }
        viewModel.firePropertyChange("buyServing");

        wageViewModel.getState().setCurrentBalance(outputData.getNewBalance());
        wageViewModel.firePropertyChange();
        officeViewModel.getState().setCurrentBalance(outputData.getNewBalance());
        officeViewModel.firePropertyChange();
    }
}