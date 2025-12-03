package use_case.review;

import java.util.ArrayList;
import java.util.List;

import entity.Review;

public interface ReviewDataAccessInterface {
    // manage how review data is stored and retrieved
    // the interactor and controller don't need to know how the data is stored

    /**
     * Adds a review to the DAO.
     *
     * @param review - the review that will be added to the DAO
     */
    void addReview(Review review);

    /**
     * Gets the reviews/ratings by the day.
     *
     * @param day - the day to get the reviews from
     * @return - array list of doubles since the review/ratings are doubles
     */
    ArrayList<Double> getReviewsByDay(int day);

    /**
     * Gets all the reviews of the restaurant.
     *
     * @return - array list of doubles since the review/ratings are doubles
     */
    List<Double> getAllReviews();
}
