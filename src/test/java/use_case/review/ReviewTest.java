package use_case.review;

import data_access.ReviewDataAccessObject;
import entity.ReviewEntity;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReviewTest {


    // Test Review DAO
    static class TestReviewDataAccessInterface extends ReviewDataAccessObject {
        public TestReviewDataAccessInterface(){
            super(null);
            addReview(new ReviewEntity(5.0,1));
            addReview(new ReviewEntity(2.0,1));
            addReview(new ReviewEntity(1.0,2));
            addReview(new ReviewEntity(1.0,2));
            addReview(new ReviewEntity(4.0,3));

        }
    }

    // Test output boundary/data to get presenter output
    static class TestPresenter implements ReviewOutputBoundary{
        ReviewOutputData reviewOutput;
        List<Integer> availableDaysList;
        @Override
        public void present(ReviewOutputData outputData) {
            this.reviewOutput = outputData;
        }

        @Override
        public void presentAvailableDays(List<Integer> availableDays) {
            this.availableDaysList = availableDays;
        }
    }

    @Test
    void testExecuteDayReview(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter presenter = new TestPresenter ();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, presenter);

        reviewInteractor.execute(new ReviewInputData(1));

        assertNotNull(presenter.reviewOutput);
        assertEquals(3.5, presenter.reviewOutput.getRating());
        assertEquals("\uD83D\uDE01", presenter.reviewOutput.getEmoji());

    }

    @Test
    void testExecuteOverallReview(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter outputBoundary = new TestPresenter();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        double overallAvg = reviewInteractor.getAverageOverall();

        assertEquals(2.6, overallAvg);
    }

    @Test
    void testExecuteDayNull(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter presenter = new TestPresenter();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, presenter);

        reviewInteractor.execute(new ReviewInputData(null));
        assertEquals(2.6, presenter.reviewOutput.getRating());
        assertEquals("\uD83D\uDE10", presenter.reviewOutput.getEmoji());
    }

    @Test
    void testFetchAvailableDays(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter presenter = new TestPresenter ();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, presenter);

        reviewInteractor.fetchDays();

        assertNotNull(presenter.availableDaysList);

        // Placeholder 0 should be first
        assertEquals(List.of(0,1,2,3), presenter.availableDaysList);
    }

    @Test
    void testGetAverageOverallAndEmoji(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter presenter = new TestPresenter ();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, presenter);

        double overallAvg = reviewInteractor.getAverageOverall();
        assertEquals(2.6, overallAvg);

        assertEquals("\uD83D\uDE22", reviewInteractor.getEmoji(1.5));
        assertEquals("\uD83D\uDE10",  reviewInteractor.getEmoji(2.5));
        assertEquals("\uD83D\uDE01",  reviewInteractor.getEmoji(5.0));
    }

    @Test
    void testGetAverageReviewDay(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter  outputBoundary = new TestPresenter();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        double avgDay1 = reviewInteractor.getAverageReviewDay(1);
        double avgDay2 = reviewInteractor.getAverageReviewDay(2);
        double avgDay3 = reviewInteractor.getAverageReviewDay(3);

        assertEquals(3.5, avgDay1);
        assertEquals(1.0, avgDay2);
        assertEquals(4.0, avgDay3);

    }

    @Test
    void testGetAvailableDaysDirect(){
        TestReviewDataAccessInterface dao = new TestReviewDataAccessInterface();
        TestPresenter outputBoundary = new TestPresenter();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        List<Integer> days = reviewInteractor.getAvailableDays();
        assertEquals(List.of(1,2,3), days);
    }




}
