package usecase.buy_serving;

/**
 * The Output Boundary for the Buy Servings Use Case.
 * The presenter will implement this interface to present the result to the user.
 */
public interface BuyServingOutputBoundary {

    /**
     * Present the result of the Buy Servings use case.
     * @param outputData the output data containing the result
     */
    void present(BuyServingOutputData outputData);
}
