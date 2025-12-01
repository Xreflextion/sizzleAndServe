package data_access;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import java.io.IOException;
import use_case.review.ReviewDAO;
import entity.ReviewEntity;
import use_case.simulate.SimulateReviewDataAccessInterface;

import java.util.*;



public class ReviewDAOHash implements ReviewDAO, SimulateReviewDataAccessInterface {

    /**
     * Review manager add reviews by a mapping with the key being the number day (for example
     * 1) and having a list of integers being the ratings you are able to get the average review
     * by day, you are able to get the average review for the restaurant coming from the insights class,
     * you are able to get the total number of reviews also coming from the insights class, and
     * you are able to get the total number of reviews per day
     * An example of what it may look like
     * {
     *     1 : [5.0, 4.1, 3.5, 5.0],
     *     2 : [2.9, 5.0],
     *     3 : [4.8, 4.5, 4.1]
     * }
     *
     */


    private final Map<Integer, ArrayList<Double>> storage;
    private final FileHelperObject fileHelperObject;

    // This implementation of the ReviewDAO is a hashmap
    public ReviewDAOHash(FileHelperObject fileHelperObject) {
        Map<Integer, ArrayList<Double>> reviewsByDay = new HashMap<>();
        this.fileHelperObject = fileHelperObject;

        if (fileHelperObject != null) {
            JsonArray daysArray = fileHelperObject.getArrayFromSaveData(Constants.DAY_RECORD_KEY);
            for (JsonElement element: daysArray) {
                JsonObject day = element.getAsJsonObject();
                int dayNumber = day.getAsJsonPrimitive("day_number").getAsInt();
                if (day.keySet().contains("ratings_list")) {
                    Double[] ratingsList = new Gson().fromJson(day.getAsJsonArray("ratings_list"), Double[].class);
                    ArrayList<Double> ratings = new ArrayList<>(Arrays.asList(ratingsList));
                    reviewsByDay.put(dayNumber, ratings);
                }
            }
        }
        this.storage = reviewsByDay;
        this.fileHelperObject = fileHelperObject;
    }


    @Override
    public void addReview(ReviewEntity reviewEntity) {
        // Checks if the key is in the hashmap if so then it will add the rating to the selected key
        if(storage.containsKey(reviewEntity.getDayNum())){

            // This will add the value by getting the key with the associated day number then add the rating
            storage.get(reviewEntity.getDayNum()).add(reviewEntity.getRating());
        }
        else{
            // If key doesn't exist in hashmap then a new array list will be made
            // then the new list will become the value for the day number
            // then the rating will be added to the array list for the day

            ArrayList<Double> newList = new ArrayList<>();
            storage.put(reviewEntity.getDayNum(), newList);
            storage.get(reviewEntity.getDayNum()).add(reviewEntity.getRating());

        }
        save();
    }

    // gets the reviews by the day
    @Override
    public ArrayList<Double> getReviewsByDay(int day) {

        ArrayList<Double> reviews = storage.get(day);
        if(reviews == null){
            return new ArrayList<>();
        }
        return reviews;
    }


    // gets all the reviews
    @Override
    public List<Double> getAllReviews() {
        ArrayList<Double> allReviews = new ArrayList<>();
        for(ArrayList<Double> reviews : storage.values()){
            allReviews.addAll(reviews);
        }
        return allReviews;
    }

    // gets the all days
    public Set<Integer> getAllDays() {
        return storage.keySet();
    }

    public void saveToFile() throws IOException {
        JsonArray array = new JsonArray();
        Gson gson = new Gson();

        for (Map.Entry<Integer, ArrayList<Double>> entry : storage.entrySet()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("day_number", entry.getKey());
            JsonArray ratingsArray = new JsonArray();
            for (Double rating : entry.getValue()) {
                ratingsArray.add(rating);
            }
            obj.add("ratings_list", ratingsArray);
            array.add(obj);
        }
        fileHelperObject.saveArray(Constants.REVIEWS_KEY, array);
    }

    public void save() {
        try {
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


