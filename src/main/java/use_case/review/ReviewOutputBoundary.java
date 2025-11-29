package use_case.review;

import entity.ReviewEntity;
import interface_adapter.review.ReviewState;

import java.util.List;

/**
 * Used to define how the use case should deliver results
 */

public interface ReviewOutputBoundary {
    void present(ReviewOutputData outputData);
    void presentAvailableDays(List<Integer> availableDays);
}