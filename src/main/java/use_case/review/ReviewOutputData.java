
package use_case.review;

public class ReviewOutputData {

    // could be day average OR overall average
    private final double rating;
    private final String emoji;

    public ReviewOutputData(double rating, String emoji) {
        this.rating = rating;
        this.emoji = emoji;
    }

    public double getRating() {
        return rating;
    }

    public String getEmoji() {
        return emoji;
    }

}
