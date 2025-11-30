package interfaceadapter.review;

import usecase.review.ReviewInputBoundary;
import usecase.review.ReviewInputData;
import usecase.review.ReviewInteractor;


public class ReviewController {
    // Controller is meant to send the data to the ReviewView

    // Creates interactor since the interactor handles all the calculations methods
    private final ReviewInputBoundary reviewInteractor;


    public ReviewController(ReviewInteractor reviewInteractor) {
        this.reviewInteractor = reviewInteractor;
    }


    /**
     * Gets a review
     * @param day - this is needed to get the review by the day, or left as null if it's the overall review
     */
    public void getReview(Integer day) {
        ReviewInputData inputData = new ReviewInputData(day);
        reviewInteractor.execute(inputData);
    }


    // Requests available days
    public void requestDays() {
        reviewInteractor.fetchDays();
    }
}