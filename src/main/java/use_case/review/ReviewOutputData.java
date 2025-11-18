package use_case.review;

public class ReviewOutputData {
    private final double rating;
    private final String emoji;

    // packages the info from the controller and gives it to the View
    public ReviewOutputData(double rating, String emoji) {
        this.rating = rating;
        this.emoji = emoji;

    }

    // gets the rating
    public  double getRating() {
        return rating;
    }

    // gets the emoji
    public String getEmoji() {
        return emoji;
    }
}


