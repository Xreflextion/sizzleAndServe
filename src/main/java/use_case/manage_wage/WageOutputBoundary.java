package use_case.manage_wage;

public interface WageOutputBoundary {
    /**
     * Prepares the success view for the wage update use case.
     * @param wageOutputData The output data containing updated wage and effect.
     */
    void prepareSuccessView(WageOutputData wageOutputData);

    /**
     * Prepares the error view for the wage update use case.
     * @param message The error message.
     */
    void prepareErrorView(String message);
}

