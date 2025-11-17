package interface_adapter.review;

import use_case.review.ReviewInteractor;
import use_case.review.ReviewOutputData;

public class ReviewController {
    // Controller is meant to send the data to the ReviewView

    // Creates interactor since the interactor handles all the calculations methods, and the controller
    // needs to use said methods
    private final ReviewInteractor reviewInteractor;

    public ReviewController(ReviewInteractor reviewInteractor) {
        this.reviewInteractor = reviewInteractor;
    }

    // get the avg review for the day and emoji
    public ReviewOutputData getReview(int day){
        double rating = reviewInteractor.getAverageReviewDay(day);
        String emoji =  reviewInteractor.getEmoji(rating);
        return new ReviewOutputData(rating, emoji);
    }

    // get the avg review overall and emoji
    public ReviewOutputData getReviewOverall(){
        double rating = reviewInteractor.getAverageOverall();
        String emoji =  reviewInteractor.getEmoji(rating);

        return new ReviewOutputData(rating, emoji);
    }
}


