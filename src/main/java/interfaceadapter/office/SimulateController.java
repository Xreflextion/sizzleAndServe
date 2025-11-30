package interfaceadapter.office;

import usecase.simulate.SimulateInputBoundary;
import usecase.simulate.SimulateInputData;

/**
 * The controller for the Simulate Use Case.
 */
public class SimulateController {

    private final SimulateInputBoundary simulateUseCaseInteractor;

    public SimulateController(SimulateInputBoundary simulateUseCaseInteractor) {
        this.simulateUseCaseInteractor = simulateUseCaseInteractor;
    }

    /**
     * Executes the Simulate Use Case.
     */
    public void execute(int currentDay, int currentCustomerCount) {
        final SimulateInputData simulateInputData = new SimulateInputData(currentDay, currentCustomerCount);

        simulateUseCaseInteractor.execute(simulateInputData);
    }
}
