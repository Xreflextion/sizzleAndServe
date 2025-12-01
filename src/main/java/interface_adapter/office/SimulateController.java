package interface_adapter.office;

import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInputData;

/**
 * The controller for the Simulate Use Case.
 */
public class SimulateController {

    private final SimulateInputBoundary simulateUseCaseInteractor;

    public SimulateController(SimulateInputBoundary simulateUseCaseInteractor) {
        this.simulateUseCaseInteractor = simulateUseCaseInteractor;
    }

    /**
     * Executes the Simulate Use Case by simulating the next day.
     * @param currentDay The current day
     * @param currentCustomerCount The number of customers on this day
     */
    public void execute(int currentDay, int currentCustomerCount) {
        final SimulateInputData simulateInputData = new SimulateInputData(currentDay, currentCustomerCount);

        simulateUseCaseInteractor.execute(simulateInputData);
    }
}
