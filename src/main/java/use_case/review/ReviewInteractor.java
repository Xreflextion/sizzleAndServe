
package use_case.review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import data_access.ReviewDataAccessObject;
import interface_adapter.review.ReviewState;

public class ReviewInteractor implements ReviewInputBoundary {

    // Constants
    private static final double ROUNDING = 10.0;
    private static final double LOW_RATING = 2.0;
    private static final double MED_RATING = 3.0;

    // Creates a DAO hashmap
    private final ReviewDataAccessObject reviewDataAccessObjectHash;

    // Creates the presenter which is bounded by the output boundary
    private final ReviewOutputBoundary presenter;

    public ReviewInteractor(ReviewDataAccessObject reviewDataAccessObjectHash, ReviewOutputBoundary presenter) {
        this.reviewDataAccessObjectHash = reviewDataAccessObjectHash;
        this.presenter = presenter;
    }

    @Override
    public void execute(ReviewInputData input) {
        final Integer day = input.getDay();
        final double averageRating;
        if (day != null) {
            // For the day average
            averageRating = getAverageReviewDay(day);
        }
        else {
            averageRating = getAverageOverall();
        }
        final String emoji = getEmoji(averageRating);

        final List<Integer> days = getAvailableDays();
        final ReviewState reviewState = new ReviewState();
        reviewState.setRating(averageRating);
        reviewState.setEmoji(emoji);
        reviewState.setAvailableDays(days);

        final ReviewOutputData output = new ReviewOutputData(averageRating, emoji);
        presenter.present(output);
    }

    @Override
    public void fetchDays() {
        final List<Integer> availableDays = new ArrayList<>(reviewDataAccessObjectHash.getAllDays());
        availableDays.remove(Integer.valueOf(0));
        Collections.sort(availableDays);
        availableDays.add(0, 0);

        // Pass only the updated list to the presenter
        presenter.presentAvailableDays(availableDays);
    }

    /**
     * This will get the total number of reviews of the entire restaurant
     * It will iterate through the values of the hashmap
     * The values it will iterate through are array list since they keep the reviews
     * then the counter variable will increment by the size of these array list
     * since that will be the number of reviews
     * uses the DAO to get all reviews to increase the num of reviews.
     * @return - The number of total reviews
     */
    public int getTotalNumReview() {
        int counter = 0;
        for (double ignored : reviewDataAccessObjectHash.getAllReviews()) {
            counter += 1;
        }
        return counter;
    }

    /**
     * Gets the average review by the day
     * For example day 1: 3.5 stars out of 5
     * uses the getReviews for the day to get the number of reviews for that day
     * Iterates through the reviews to find the numerator.
     * @param day - the day to get the average review for the day
     * @return - Returns the average rating for the day.
     */
    public double getAverageReviewDay(int day) {
        double numerator = 0;
        final double totalReviews = reviewDataAccessObjectHash.getReviewsByDay(day).size();
        for (Double reviews: reviewDataAccessObjectHash.getReviewsByDay(day)) {
            numerator += reviews;
        }
        final double avg = numerator / totalReviews;
        return Math.round(avg * ROUNDING) / ROUNDING;
    }

    /**
     * Gets the average reviews overall for the restaurant
     * uses the get all reviews to iterate through all reviews.
     * @return - the average rating
     */
    public double getAverageOverall() {
        final int totalReviews = getTotalNumReview();
        double numerator = 0;
        for (double review : reviewDataAccessObjectHash.getAllReviews()) {
            numerator += review;
        }
        final double avg = numerator / totalReviews;
        return Math.round(avg * ROUNDING) / ROUNDING;
    }

    /**
     * Returns the emoji.
     * @param rating - the rating to get the right emoji
     * @return - the emoji for the rating
     */
    public String getEmoji(double rating) {
        String emoji = "";
        if (rating <= LOW_RATING) {
            emoji = "\uD83D\uDE22";
        }
        else if (rating <= MED_RATING) {
            emoji = "\uD83D\uDE10";
        }
        else {
            emoji = "\uD83D\uDE01";
        }
        return emoji;
    }

    /**
     * Gets all the days the user has completed.
     * @return - A list of the days
     */
    public List<Integer> getAvailableDays() {
        final Set<Integer> dayKeys = reviewDataAccessObjectHash.getAllDays();
        final List<Integer> days = new ArrayList<>(dayKeys);
        Collections.sort(days);
        return days;
    }
}
