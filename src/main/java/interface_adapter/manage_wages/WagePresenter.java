package interface_adapter.manage_wages;

import use_case.manage_wage.WageOutputBoundary;
import use_case.manage_wage.WageOutputData;

public class WagePresenter implements WageOutputBoundary {

    private final WageViewModel viewModel;

    public WagePresenter(WageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(WageOutputData data) {
        WageState state = viewModel.getState();

        if ("Cook".equals(data.getPosition())) {
            state.setCookWage(data.getWageAfter());
            state.setCookWageEffect(data.getWageEffectAfter());
        } else if ("Waiter".equals(data.getPosition())) {
            state.setWaiterWage(data.getWageAfter());
            state.setWaiterWageEffect(data.getWageEffectAfter());
        }
        viewModel.setState(state);
        viewModel.firePropertyChange();

    }
}
