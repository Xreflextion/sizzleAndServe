package interface_adapter.review;

import interface_adapter.ViewModel;
import use_case.review.ReviewOutputData;

public class ReviewViewModel extends ViewModel<ReviewOutputData>{

    public static final String VIEW_NAME = "review view";

    // Holds the current state of the Review view
    public ReviewViewModel() {
        super(VIEW_NAME);

    }

}