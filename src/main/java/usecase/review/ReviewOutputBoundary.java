package usecase.review;

import java.util.List;

/**
 * Used to define how the use case should deliver results
 */

public interface ReviewOutputBoundary {
    void present(ReviewOutputData outputData);
    void presentAvailableDays(List<Integer> availableDays);
}