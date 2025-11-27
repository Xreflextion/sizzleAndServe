package use_case.review;

import data_access.ReviewDAOHash;
import entity.ReviewEntity;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ReviewTest {


    // Test Review DAO
    static class TestReviewDAO extends ReviewDAOHash{
        public TestReviewDAO(){
            super(new HashMap<>());
            addReview(new ReviewEntity(5.0,1));
            addReview(new ReviewEntity(2.0,1));
            addReview(new ReviewEntity(1.0,2));
            addReview(new ReviewEntity(1.0,2));
            addReview(new ReviewEntity(4.0,3));

        }
    }

    // Test output boundary/data to get presenter output
    static class TestOutputBoundary implements ReviewOutputBoundary{
        ReviewOutputData reviewOutputBoundary;

        @Override
        public void present(ReviewOutputData outputData) {
            this.reviewOutputBoundary = outputData;
        }
    }

    @Test
    void testAverageReviewDay(){
        TestReviewDAO dao = new TestReviewDAO();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        double avgDay1 = reviewInteractor.getAverageReviewDay(1);
        double avgDay2 = reviewInteractor.getAverageReviewDay(2);
        double avgDay3 = reviewInteractor.getAverageReviewDay(3);


        assertEquals(3.5, avgDay1);
        assertEquals(1.0, avgDay2);
        assertEquals(4.0, avgDay3);


    }

    @Test
    void testAverageOverall(){
        TestReviewDAO dao = new TestReviewDAO();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        double overallAvg = reviewInteractor.getAverageOverall();

        assertEquals(2.6, overallAvg);
    }

    @Test
    void testGetEmoji(){
        TestReviewDAO dao = new TestReviewDAO();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        assertEquals("\uD83D\uDE22", reviewInteractor.getEmoji(1.5)); // sad
        assertEquals("\uD83D\uDE10", reviewInteractor.getEmoji(2.5)); // normal
        assertEquals("\uD83D\uDE01", reviewInteractor.getEmoji(4.5)); // happy

    }

    @Test
    void testGetAvailableDays(){
        TestReviewDAO dao = new TestReviewDAO();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        List<Integer> days = reviewInteractor.getAvailableDays();
        assertEquals(List.of(1,2,3), days);
    }

    @Test
    void testExecuteDayReview(){
        TestReviewDAO dao = new TestReviewDAO();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        // For day 1
        reviewInteractor.execute(new ReviewInputData(1));
        assertNotNull(outputBoundary.reviewOutputBoundary);
        assertEquals(3.5, outputBoundary.reviewOutputBoundary.getRating());
        assertEquals("\uD83D\uDE01", outputBoundary.reviewOutputBoundary.getEmoji());
    }

    @Test
    void testExecuteOverallReview(){
        TestReviewDAO dao = new TestReviewDAO();
        TestOutputBoundary outputBoundary = new TestOutputBoundary();
        ReviewInteractor reviewInteractor = new ReviewInteractor(dao, outputBoundary);

        reviewInteractor.execute(new ReviewInputData(null));
        assertNotNull(outputBoundary.reviewOutputBoundary);
        assertEquals(2.6, outputBoundary.reviewOutputBoundary.getRating());
        assertEquals("\uD83D\uDE10", outputBoundary.reviewOutputBoundary.getEmoji());
    }


}
