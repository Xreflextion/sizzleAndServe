package use_case.review;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import data_access.ReviewDataAccessObject;
import entity.ReviewEntity;

public class ReviewTest {

    @Test
    void testExecuteDayReview() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter presenter = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, presenter);

        reviewInteractor.execute(new ReviewInputData(1));

        assertNotNull(presenter.reviewOutput);
        assertEquals(3.5, presenter.reviewOutput.getRating());
        assertEquals("\uD83D\uDE01", presenter.reviewOutput.getEmoji());

    }

    @Test
    void testExecuteOverallReview() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter outputBoundary = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, outputBoundary);

        final double overallAvg = reviewInteractor.getAverageOverall();

        assertEquals(2.6, overallAvg);
    }

    @Test
    void testExecuteDayNull() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter presenter = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, presenter);

        reviewInteractor.execute(new ReviewInputData(null));
        assertEquals(2.6, presenter.reviewOutput.getRating());
        assertEquals("\uD83D\uDE10", presenter.reviewOutput.getEmoji());
    }

    @Test
    void testFetchAvailableDays() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter presenter = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, presenter);

        reviewInteractor.fetchDays();

        assertNotNull(presenter.availableDaysList);

        // Placeholder 0 should be first
        assertEquals(List.of(1,2,3,4), presenter.availableDaysList);
    }

    @Test
    void testGetAverageOverallAndEmoji() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter presenter = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, presenter);

        final double overallAvg = reviewInteractor.getAverageOverall();
        assertEquals(2.6, overallAvg);

        assertEquals("\uD83D\uDE22", reviewInteractor.getEmoji(1.5));
        assertEquals("\uD83D\uDE10", reviewInteractor.getEmoji(2.5));
        assertEquals("\uD83D\uDE01", reviewInteractor.getEmoji(5.0));
    }

    @Test
    void testGetAverageReviewDay() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter outputBoundary = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, outputBoundary);

        final double avgDay1 = reviewInteractor.getAverageReviewDay(1);
        final double avgDay2 = reviewInteractor.getAverageReviewDay(2);
        final double avgDay3 = reviewInteractor.getAverageReviewDay(3);

        assertEquals(3.5, avgDay1);
        assertEquals(1.0, avgDay2);
        assertEquals(4.0, avgDay3);

    }

    @Test
    void testGetAvailableDaysDirect() {
        final TestReviewDataAccessInterface dataAccessObject = new TestReviewDataAccessInterface();
        final TestPresenter outputBoundary = new TestPresenter();
        final ReviewInteractor reviewInteractor = new ReviewInteractor(dataAccessObject, outputBoundary);

        final List<Integer> days = reviewInteractor.getAvailableDays();
        assertEquals(List.of(1, 2, 3), days);
    }

    // Test Review DAO
    static class TestReviewDataAccessInterface extends ReviewDataAccessObject {
        TestReviewDataAccessInterface() {
            super(null);
            addReview(new ReviewEntity(5.0, 1));
            addReview(new ReviewEntity(2.0, 1));
            addReview(new ReviewEntity(1.0, 2));
            addReview(new ReviewEntity(1.0, 2));
            addReview(new ReviewEntity(4.0, 3));
        }
    }

    // Test output boundary/data to get presenter output
    static class TestPresenter implements ReviewOutputBoundary {
        private ReviewOutputData reviewOutput;
        private List<Integer> availableDaysList;

        @Override
        public void present(ReviewOutputData outputData) {
            this.reviewOutput = outputData;
        }

        @Override
        public void presentAvailableDays(List<Integer> availableDays) {
            this.availableDaysList = availableDays;
        }
    }
}
