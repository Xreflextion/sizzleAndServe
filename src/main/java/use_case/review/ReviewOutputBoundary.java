package use_case.review;

import entity.ReviewEntity;

/**
 * Used to define how the use case should deliver results
 */

public interface ReviewOutputBoundary {
    void present(ReviewOutputData outputData);
}
