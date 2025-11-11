package Use_Case.WaiterWageIncrease;

public interface WaiterWageIncreaseOutputBoundary {
    /** Prepares success view for increasing waiter wage use case.
     * @param waiterWageIncreaseOutputData the output data
     */
    void prepareSuccessView (
            WaiterWageIncreaseOutputData waiterWageIncreaseOutputData);
}
