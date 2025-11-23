package interface_adapter.review;

public class ReviewState {
    private double rating; // Doesn't matter if average overall rating or just the average rating for the day
    private String emoji;

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

}
