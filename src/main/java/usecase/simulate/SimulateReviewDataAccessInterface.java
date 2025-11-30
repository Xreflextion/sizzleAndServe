package usecase.simulate;

import entity.ReviewEntity;

import java.util.ArrayList;

public interface SimulateReviewDataAccessInterface {

    void addReview(ReviewEntity reviewEntity);

    ArrayList<Double> getReviewsByDay(int day);
}
