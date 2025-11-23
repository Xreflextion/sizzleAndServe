package interface_adapter.office;

import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInputData;

import java.util.Map;

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
    public void execute(int currentDay, double currentBalance, int pastCustomerCount) {
        final SimulateInputData simulateInputData = new SimulateInputData(currentDay, currentBalance, pastCustomerCount);

        simulateUseCaseInteractor.execute(simulateInputData);
    }
}
