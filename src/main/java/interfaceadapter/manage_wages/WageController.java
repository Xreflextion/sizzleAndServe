package interfaceadapter.manage_wages;

import usecase.manage_wage.WageInputBoundary;

public class WageController {
    private final WageInputBoundary interactor;

    public WageController(WageInputBoundary interactor) {
        this.interactor = interactor;
    }

    public void cookIncrease() {
        interactor.increaseWage("Cook");
    }

    public void cookDecrease() {
        interactor.decreaseWage("Cook");
    }

    public void waiterIncrease() {
        interactor.increaseWage("Waiter");
    }

    public void waiterDecrease() {
        interactor.decreaseWage("Waiter");
    }

}
