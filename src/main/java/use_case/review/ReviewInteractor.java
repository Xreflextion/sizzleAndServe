package use_case.review;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import interface_adapter.ViewModel;
import entity.ReviewEntity;
import data_access.ReviewDAOHash;
import interface_adapter.review.ReviewState;
import interface_adapter.review.ReviewViewModel;


public class ReviewInteractor implements ReviewInputBoundary{
    // Creates a DAO hashmap
    private final ReviewDAOHash reviewDAOHash;
    // Creates the presenter which is bounded by the output boundary
    private final ReviewOutputBoundary presenter;

    public ReviewInteractor(ReviewDAOHash reviewDAOHash, ReviewOutputBoundary presenter) {
        this.reviewDAOHash = reviewDAOHash;
        this.presenter = presenter;
    }

    @Override
    public void execute(ReviewInputData input){
        Integer day = input.getDay();
        double averageRating;
        if (day != null){
            // For the day average
            averageRating = getAverageReviewDay(day);
        }
        else{
            averageRating = getAverageOverall();
        }
        String emoji = getEmoji(averageRating);

        ReviewOutputData output = new ReviewOutputData(averageRating, emoji);

        List<Integer> days = getAvailableDays();
        ReviewState reviewState = new ReviewState();
        reviewState.setRating(averageRating);
        reviewState.setEmoji(emoji);
        reviewState.setAvailableDays(days);

        presenter.present(output);
    }

    @Override
    public void fetchDays(){
        List<Integer> availableDays = new ArrayList<>(reviewDAOHash.getAllDays());
        availableDays.remove(Integer.valueOf(0));
        Collections.sort(availableDays);
        availableDays.add(0,0);

        // Pass only the updated list to the presenter
        presenter.presentAvailableDays(availableDays);
    }

    // This will get the total number of reviews of the entire restaurant
    // It will iterate through the values of the hashmap
    // The values it will iterate through are array list since they keep the reviews
    // then the counter variable will increment by the size of these array list
    // since that will be the number of reviews
    // uses the DAO to get all reviews to increase the num of reviews
    public int getTotalNumReview(){
        int counter = 0;
        for(double iterate : reviewDAOHash.getAllReviews()){
            counter += 1;
        }
        return counter;
    }

    // gets the average review by the day
    // For example day 1: 3.5 stars out of 5
    // uses the getReviews for the day to get the number of reviews for that day
    // Iterates through the reviews to find the numerator
    public double getAverageReviewDay(int day){
        double numerator = 0;
        double totalReviews = reviewDAOHash.getReviewsByDay(day).size();
        for(Double reviews: reviewDAOHash.getReviewsByDay(day)){
            numerator += reviews;
        }
        double avg = numerator / totalReviews;
        return Math.round(avg * 10.0) / 10.0;
    }
    // gets the average reviews overall for the restaurant
    // uses the get all reviews to iterate through all reviews
    public double getAverageOverall(){
        int totalReviews = getTotalNumReview();
        double numerator = 0;
        for(double review : reviewDAOHash.getAllReviews()){
            numerator += review;
        }
        double avg = numerator / totalReviews;
        return Math.round(avg * 10.0) / 10.0;
    }
    // Returns emoji
    public String getEmoji(double rating){
        if(rating <= 2.0){
            return "\uD83D\uDE22";
        }
        else if(rating <= 3.0){
            return "\uD83D\uDE10";
        }
        else{
            return "\uD83D\uDE01";
        }
    }
    // Gets all the days the user has completed
    public List<Integer> getAvailableDays() {
        Set<Integer> dayKeys = reviewDAOHash.getAllDays();
        List<Integer> days = new ArrayList<>(dayKeys);
        Collections.sort(days);
        return days;
    }
}