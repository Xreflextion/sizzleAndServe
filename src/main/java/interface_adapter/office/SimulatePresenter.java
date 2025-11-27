package interface_adapter.office;

import interface_adapter.ViewManagerModel;
import use_case.simulate.SimulateOutputBoundary;
import use_case.simulate.SimulateOutputData;

public class SimulatePresenter implements SimulateOutputBoundary {

    private final OfficeViewModel officeViewModel;
    private final ViewManagerModel viewManagerModel;

    public SimulatePresenter(ViewManagerModel viewManagerModel,
                           OfficeViewModel officeViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.officeViewModel = officeViewModel;
    }

    @Override
    public void prepareSuccessView(SimulateOutputData outputData) {
        final OfficeState officeState = officeViewModel.getState();
        officeState.setCurrentBalance(outputData.getCurrentBalance());
        officeState.setCurrentDay(outputData.getCurrentDay());
        officeState.setCurrentCustomerCount(outputData.getCurrentCustomerCount());
        officeViewModel.firePropertyChange();

        viewManagerModel.setState(officeViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
