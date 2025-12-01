
package use_case.review;

import java.util.List;

/**
 * Used to define how the use case should deliver results.
 */

public interface ReviewOutputBoundary {
    /**
     * Presents the review results.
     * @param outputData - outputData the ratings and emoji
     */
    void present(ReviewOutputData outputData);

    /**
     * Presents the list of days that have reviews available.
     * @param availableDays - list of review day values that can be selected
     */
    void presentAvailableDays(List<Integer> availableDays);
}
