package interface_adapter.review;


import use_case.review.ReviewOutputData;

public class ReviewPresenter{

    // viewModel is needed since the presenter receives output data then sends to viewModel to update the GUI
    private final ReviewViewModel viewModel;
    public ReviewPresenter(interface_adapter.review.ReviewViewModel viewModel) {
        this.viewModel = viewModel;
    }


    // Presents the overall review
    public void presentOverallReview(ReviewOutputData output){
        viewModel.setState(output);
        viewModel.firePropertyChange();
    }

    // Presents the overall review by the day
    public void presentDayReview(int day, ReviewOutputData output) {
        viewModel.setState(output);
        viewModel.firePropertyChange();
    }
}