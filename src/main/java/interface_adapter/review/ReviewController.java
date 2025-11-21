package interface_adapter.review;

import use_case.review.ReviewInteractor;
import use_case.review.ReviewOutputData;

import java.util.List;


public class ReviewController {
    // Controller is meant to send the data to the ReviewView

    // Creates interactor since the interactor handles all the calculations methods
    private final ReviewInteractor reviewInteractor;

    // Translate between the interactor and the view
    private final ReviewPresenter presenter;


    public ReviewController(ReviewInteractor reviewInteractor, ReviewPresenter presenter) {
        this.reviewInteractor = reviewInteractor;
        this.presenter = presenter;
    }


    // get the avg review overall and emoji for the day
    public void getReview(int day) {
        double rating = reviewInteractor.getAverageReviewDay(day);
        String emoji = reviewInteractor.getEmoji(rating);
        ReviewOutputData output = new ReviewOutputData(rating, emoji);
        presenter.presentDayReview(day, output);
    }

    // get the avg review overall and emoji
    public void getReviewOverall() {
        double rating = reviewInteractor.getAverageOverall();
        String emoji = reviewInteractor.getEmoji(rating);
        ReviewOutputData output = new ReviewOutputData(rating, emoji);
        presenter.presentOverallReview(output);


    }

    // Return available days
    public List<Integer> getAvailableDays() {
        return reviewInteractor.getAvailableDays();
    }
}


