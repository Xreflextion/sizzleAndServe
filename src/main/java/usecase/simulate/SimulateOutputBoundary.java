package usecase.simulate;

/**
 * The output boundary for the Simulate Use Case.
 */
public interface SimulateOutputBoundary {

    /**
     * Prepares the success view for the Simulate Use Case.
     * @param outputData the output data
     */
    void prepareSuccessView(SimulateOutputData outputData);

    /**
     * Prepares the failure view for the Simulate Use Case.
     * @param errorMessage the explanation of the failure
     */
    void prepareFailView(String errorMessage);
}
