package use_case.review;

import entity.ReviewEntity;
import interface_adapter.review.ReviewState;

/**
 * Used to define how the use case should deliver results
 */

public interface ReviewOutputBoundary {
    void present(ReviewOutputData outputData);
    void presentDays(ReviewState state);
}