package use_case.review;

/**
 * Review input boundary used for the interactor to implement correct methods.
 */

public interface ReviewInputBoundary {

    /**
     * Executes the main business logic for a review operation.
     *
     * @param inputData - Contains all the information of the review
     */
    void execute(ReviewInputData inputData);

    /**
     * Fetches the list of days.
     */
    void fetchDays();
}
