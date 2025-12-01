package interface_adapter.manage_wages;

import use_case.manage_wage.WageInputBoundary;

public class WageController {
    private final WageInputBoundary interactor;

    public WageController(WageInputBoundary interactor) {

        this.interactor = interactor;
    }

    /**
     * Passes increase cook wage event to wage interactor.
     */
    public void cookIncrease() {
        interactor.increaseWage("Cook");
    }

    /**
     * Passes decrease cook wage event to wage interactor.
     */
    public void cookDecrease() {
        interactor.decreaseWage("Cook");
    }

    /**
     * Passes increase waiter wage event to wage interactor.
     */
    public void waiterIncrease() {
        interactor.increaseWage("Waiter");
    }

    /**
     * Passes decrease waiter wage event to wage interactor.
     */
    public void waiterDecrease() {
        interactor.decreaseWage("Waiter");
    }

}
