package use_case.buy_serving;

/**
 * The Output Boundary for the Buy Servings Use Case.
 * The interactor will implement this interface to present the result to the user.
 */
public interface BuyServingInputBoundary {

    /**
     * Execute the Buy Servings Use Case.
     *
     * @param inputData the input data for this use case
     */
    void execute(BuyServingInputData inputData);
}
