package data_access;

import entity.ReviewEntity;

import java.util.ArrayList;
import java.util.List;

public interface ReviewDAO{
    // manage how review data is stored and retrieved
    // the interactor and controller don't need to know how the data is stored
    void addReview(ReviewEntity review);
    ArrayList<Double> getReviewsByDay(int day);
    List<Double> getAllReviews();
}

