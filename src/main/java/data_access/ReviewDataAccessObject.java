package data_access;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import entity.ReviewEntity;
import use_case.review.ReviewDataAccessInterface;
import use_case.simulate.SimulateReviewDataAccessInterface;

public class ReviewDataAccessObject implements ReviewDataAccessInterface, SimulateReviewDataAccessInterface {

    /**
     * Review manager add reviews by a mapping with the key being the number day (for example
     * 1) and having a list of integers being the ratings you are able to get the average review
     * by day, you are able to get the average review for the restaurant coming from the insights class,
     * you are able to get the total number of reviews also coming from the insights class, and
     * you are able to get the total number of reviews per day.
     * you are able to get the total number of reviews per day
     * An example of what it may look like
     * {
     * 1 : [5.0, 4.1, 3.5, 5.0],
     * 2 : [2.9, 5.0],
     * 3 : [4.8, 4.5, 4.1]
     * }
     *
     */

    private static final String RATINGS_LIST = "ratings_list";

    private final Map<Integer, ArrayList<Double>> storage;
    private final FileHelperObject fileHelperObject;

    // This implementation of the ReviewDAO is a hashmap
    public ReviewDataAccessObject(FileHelperObject fileHelperObject) {
        final Map<Integer, ArrayList<Double>> reviewsByDay = new HashMap<>();
        this.fileHelperObject = fileHelperObject;

        if (fileHelperObject != null) {
            final JsonArray daysArray = fileHelperObject.getArrayFromSaveData(Constants.REVIEWS_KEY);
            for (JsonElement element : daysArray) {
                final JsonObject day = element.getAsJsonObject();
                final int dayNumber = day.getAsJsonPrimitive("day_number").getAsInt();
                if (day.keySet().contains(RATINGS_LIST)) {
                    final Double[] ratingsList = new Gson().fromJson(
                            day.getAsJsonArray(RATINGS_LIST), Double[].class);
                    final ArrayList<Double> ratings = new ArrayList<>(Arrays.asList(ratingsList));
                    reviewsByDay.put(dayNumber, ratings);
                }
            }
        }
        this.storage = reviewsByDay;
    }

    @Override
    public void addReview(ReviewEntity reviewEntity) {
        // Checks if the key is in the hashmap if so then it will add the rating to the selected key
        if (storage.containsKey(reviewEntity.getDayNum())) {

            // This will add the value by getting the key with the associated day number then add the rating
            storage.get(reviewEntity.getDayNum()).add(reviewEntity.getRating());
        }
        else {
            // If key doesn't exist in hashmap then a new array list will be made
            // then the new list will become the value for the day number
            // then the rating will be added to the array list for the day

            final ArrayList<Double> newList = new ArrayList<>();
            storage.put(reviewEntity.getDayNum(), newList);
            storage.get(reviewEntity.getDayNum()).add(reviewEntity.getRating());

        }
        save();
    }

    // gets the reviews by the day
    @Override
    public ArrayList<Double> getReviewsByDay(int day) {

        ArrayList<Double> reviews = storage.get(day);
        if (reviews == null) {
            reviews = new ArrayList<>();
        }
        return reviews;
    }

    // gets all the reviews
    @Override
    public List<Double> getAllReviews() {
        final ArrayList<Double> allReviews = new ArrayList<>();
        for (ArrayList<Double> reviews : storage.values()) {
            allReviews.addAll(reviews);
        }
        return allReviews;
    }

    // gets the all days
    public Set<Integer> getAllDays() {
        return storage.keySet();
    }

    /**
     * Converts stored review data into JSON format and writes it to disk.
     * @throws IOException if error occurs when writing to file
     */
    public void saveToFile() throws IOException {
        final JsonArray array = new JsonArray();
        // final Gson gson = new Gson();

        for (Map.Entry<Integer, ArrayList<Double>> entry : storage.entrySet()) {
            final JsonObject obj = new JsonObject();
            obj.addProperty("day_number", entry.getKey());
            final JsonArray ratingsArray = new JsonArray();
            for (Double rating : entry.getValue()) {
                ratingsArray.add(rating);
            }
            obj.add("ratings_list", ratingsArray);
            array.add(obj);
        }
        fileHelperObject.saveArray(Constants.REVIEWS_KEY, array);
    }

    /**
     * Save all current review data to persistent storage.
     */
    public void save() {
        if (fileHelperObject != null) {
            try {
                saveToFile();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

}
