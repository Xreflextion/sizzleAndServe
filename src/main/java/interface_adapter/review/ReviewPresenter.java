package interface_adapter.review;


import use_case.review.ReviewOutputBoundary;
import use_case.review.ReviewOutputData;

public class ReviewPresenter implements ReviewOutputBoundary {

    // viewModel is needed since the presenter receives output data then sends to viewModel to update the GUI
    private final ReviewViewModel reviewViewModel;

    public ReviewPresenter(interface_adapter.review.ReviewViewModel reviewViewModel) {
        this.reviewViewModel = reviewViewModel;
    }


    @Override
    public void present(ReviewOutputData reviewOutputData) {
        ReviewState state = reviewViewModel.getState();
        state.setRating(reviewOutputData.getRating());
        state.setEmoji(reviewOutputData.getEmoji());

        reviewViewModel.setState(state);
        reviewViewModel.firePropertyChange();
    }
}