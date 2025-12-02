package use_case.review;

/**
 * Input data for the reviews, since day is the only possible thing the user can input.
 */

public class ReviewInputData {

    // Nullable for the overall restaurant reviews
    private final Integer day;

    public ReviewInputData(Integer day) {
        this.day = day;
    }

    public Integer getDay() {
        return day;
    }

}
