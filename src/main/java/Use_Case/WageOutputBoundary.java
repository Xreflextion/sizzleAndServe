package Use_Case;

public interface WageOutputBoundary {
    /**
     * Prepares the success view for the wage update use case.
     * @param wageOutputData The output data containing updated wage and effect.
     */
    void prepareSuccessView(WageOutputData wageOutputData);
}

