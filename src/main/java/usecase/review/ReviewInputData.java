package usecase.review;

/**
 * Input data for the reviews, since day is the only possible thing the user can input
 */

public class ReviewInputData {

    private final Integer day; // Nullable for the overall restaurant reviews

    public ReviewInputData(Integer day) {
        this.day = day;
    }

    public Integer getDay() {
        return day;
    }

}
