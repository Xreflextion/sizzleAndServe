package usecase.simulate;

/**
 * Input Boundary for actions which are related to simulation.
 */
public interface SimulateInputBoundary {

    /**
     * Executes the simulate use case.
     * @param simulateInputData the input data
     */
    void execute(SimulateInputData simulateInputData);
}
