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
        final WageState state = viewModel.getState();

        if (data.getPosition() != null) {
            if ("Cook".equals(data.getPosition())) {
                state.setCookWage(data.getWageAfter());
                state.setCookWageEffect(data.getWageEffectAfter());
            }
            else if ("Waiter".equals(data.getPosition())) {
                state.setWaiterWage(data.getWageAfter());
                state.setWaiterWageEffect(data.getWageEffectAfter());
            }
        }
        state.setCurrentBalance(data.getCurrentBalance());
        state.setWarningMessage(null);
        viewModel.setState(state);
        viewModel.firePropertyChange();

    }

    /**
     * Prepare Error View when wage change in unsuccessful.
     * @param message the message shown when the increase/decrease is unsuccessful.
     */
    public void prepareErrorView(String message) {
        final WageState state = viewModel.getState();
        state.setWarningMessage(message);
        viewModel.setState(state);
        viewModel.firePropertyChange();
    }

}
