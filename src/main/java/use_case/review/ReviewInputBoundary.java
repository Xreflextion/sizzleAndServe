package use_case.review;

import java.util.List;

/**
 * Review input boundary used for the interactor to implement correct methods
 */

public interface ReviewInputBoundary {
    void execute(ReviewInputData inputData);

    List<Integer> getAvailableDays();
}
