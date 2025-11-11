package Use_Case.WaiterWageIncrease;

import sizzleAndServe.Waiter;

public class WaiterWageIncreaseInteractor implements WaiterWageIncreaseInputBoundary {
    private WaiterWageIncreaseUserDataAccessInterface wageIncreaseUserDataAccess;
    private WaiterWageIncreaseOutputBoundary waiterWageIncreasePresenter;
    private final Waiter waiter;

    public WaiterWageIncreaseInteractor(
            WaiterWageIncreaseUserDataAccessInterface wageIncreaseUserDataAccess,
            WaiterWageIncreaseOutputBoundary waiterWageIncreasePresenter,
            Waiter waiter) {
        this.wageIncreaseUserDataAccess = wageIncreaseUserDataAccess;
        this.waiterWageIncreasePresenter = waiterWageIncreasePresenter;
        this.waiter = waiter;}

    @Override
    public void execute() {
        //* instantiate output
        this.waiter.increaseWage();
        this.wageIncreaseUserDataAccess.setCurrentWaiter(waiter);
        final WaiterWageIncreaseOutputData WWI = new WaiterWageIncreaseOutputData
                (this.wageIncreaseUserDataAccess.getCurrentWaiter());
        waiterWageIncreasePresenter.prepareSuccessView(WWI);
    }


}
