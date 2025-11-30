package interfaceadapter.office;

import interfaceadapter.ViewManagerModel;
import interfaceadapter.buy_serving.BuyServingViewModel;
import interfaceadapter.manage_wages.WageViewModel;
import usecase.simulate.SimulateOutputBoundary;
import usecase.simulate.SimulateOutputData;

public class SimulatePresenter implements SimulateOutputBoundary {

    private final OfficeViewModel officeViewModel;
    private final BuyServingViewModel buyServingViewModel;
    private final ViewManagerModel viewManagerModel;
    private final WageViewModel wageViewModel;

    public SimulatePresenter(ViewManagerModel viewManagerModel,
                            OfficeViewModel officeViewModel,
                             BuyServingViewModel buyServingViewModel,
                             WageViewModel wageViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.officeViewModel = officeViewModel;
        this.buyServingViewModel = buyServingViewModel;
        this.wageViewModel = wageViewModel;
    }

    @Override
    public void prepareSuccessView(SimulateOutputData outputData) {
        final OfficeState officeState = officeViewModel.getState();
        officeState.setCurrentBalance(outputData.getCurrentBalance());
        officeState.setCurrentDay(outputData.getCurrentDay());
        officeState.setCurrentCustomerCount(outputData.getCurrentCustomerCount());
        officeViewModel.firePropertyChange();

        String[] dishNames = buyServingViewModel.getState().dishNames;
        int[] stocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i ++) {
            stocks[i] = 0;
            if (outputData.getStock().containsKey(dishNames[i])) {
                stocks[i] = outputData.getStock().get(dishNames[i]);
            }
        }
        buyServingViewModel.setDishStocks(stocks);
        buyServingViewModel.setNewBalance(outputData.getCurrentBalance());
        buyServingViewModel.firePropertyChange();
        wageViewModel.getState().setCurrentBalance(outputData.getCurrentBalance());
        wageViewModel.firePropertyChange();

        viewManagerModel.setState(officeViewModel.getViewName());
        viewManagerModel.firePropertyChange();
    }

    @Override
    public void prepareFailView(String errorMessage) {

    }
}
