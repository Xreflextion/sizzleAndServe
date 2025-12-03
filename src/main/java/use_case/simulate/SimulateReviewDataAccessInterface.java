package use_case.simulate;

import java.util.ArrayList;

import entity.ReviewEntity;

/**
 * Review DAO interface for the Simulate Use Case.
 */
public interface SimulateReviewDataAccessInterface {

    /**
     * Add a review.
     *
     * @param reviewEntity new review to add
     */
    void addReview(ReviewEntity reviewEntity);

    /**
     * Return the reviews of the given day.
     *
     * @param day The day to get the reviews for
     * @return A list of all reviews of the given day
     */
    ArrayList<Double> getReviewsByDay(int day);
}
