
package interface_adapter.review;

import java.util.ArrayList;
import java.util.List;

public class ReviewState {

    // Doesn't matter if average overall rating or just the average rating for the day
    private double rating;
    private String emoji = "\uD83D\uDE10";
    private List<Integer> availableDays = new ArrayList<>();

    // The placeholder for the DAY 0
    public ReviewState() {
        availableDays.add(0);
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

    /**
     * Sets the list of available review days. Always ensures that
     * the placeholder day `0` is included.
     *
     * @param days - The list of review days to set
     */
    public void setAvailableDays(List<Integer> days) {
        this.availableDays = new ArrayList<>(days);
        if (!availableDays.contains(0)) {
            // always keep placeholder
            availableDays.add(0);
        }
    }

}
