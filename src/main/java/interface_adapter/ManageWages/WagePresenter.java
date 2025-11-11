package interface_adapter.ManageWages;

import Use_Case.WageOutputBoundary;
import Use_Case.WageOutputData;

public class WagePresenter implements WageOutputBoundary {

    private final WageViewModel viewModel;

    public WagePresenter(WageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WageOutputData data) {
        WageState state = viewModel.getState();

        if ("Cook".equals(data.getCurrentEmployee().getPosition())) {
            state.setCookWage(data.getWageAfter());
            state.setCookWageEffect(data.getWageEffectAfter());
        } else if ("Waiter".equals(data.getCurrentEmployee().getPosition())) {
            state.setWaiterWage(data.getWageAfter());
            state.setWaiterWageEffect(data.getWageEffectAfter());
        }
        viewModel.setState(state);
        viewModel.firePropertyChange();

    }
}
