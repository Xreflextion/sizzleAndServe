package interfaceadapter.review;

import java.util.ArrayList;
import java.util.List;

public class ReviewState {
    private double rating = 0.0; // Doesn't matter if average overall rating or just the average rating for the day
    private String emoji = "\uD83D\uDE10";
    private List<Integer> availableDays = new ArrayList<>();

    public ReviewState() {
        availableDays.add(0); // The placeholder for the DAY 0
    }

    public double getRating() {
        return this.rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public String getEmoji() {
        return this.emoji;
    }
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public List<Integer> getAvailableDays() {
        return this.availableDays;
    }

    public void setAvailableDays(List<Integer> days) {
        this.availableDays = new ArrayList<>(days);
        if (!availableDays.contains(0)){
            availableDays.add(0); // always keep placeholder
        }
    }

}