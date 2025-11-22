package use_case.BuyServing;

import entity.Pantry;
import entity.Recipe;
import entity.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BuyServingInteractorTest {

    // Test Player DAO
    static class TestPlayerDAO implements PlayerDataAccessInterface {
        Player player = new Player("TestUser", 30.0);

        @Override
        public Player getPlayer() { return player; }

        @Override
        public void savePlayer(Player player) {}
    }

    // Test Pantry DAO
    static class TestPantryDAO implements PantryDataAccessInterface {
        Pantry pantry = new Pantry();

        public TestPantryDAO() {
            pantry.getPantry().put("Pizza", new Recipe("Pizza", 10));
            pantry.getPantry().put("Burger", new Recipe("Burger", 8));
            pantry.getPantry().put("Salad", new Recipe("Salad", 6));
        }

        @Override
        public Pantry getPantry() { return pantry; }

        @Override
        public void savePantry(Pantry pantry) {}
    }

    // Test Presenter
    static class TestPresenter implements BuyServingOutputBoundary {
        BuyServingOutputData lastOutput;

        @Override
        public void present(BuyServingOutputData outputData) {
            lastOutput = outputData;
        }
    }

    @Test
    void successTest() {
        TestPlayerDAO playerDAO = new TestPlayerDAO();
        TestPantryDAO pantryDAO = new TestPantryDAO();
        TestPresenter presenter = new TestPresenter();
        BuyServingInteractor interactor = new BuyServingInteractor(playerDAO, pantryDAO, presenter);

        String[] dishNames = pantryDAO.getPantry().getDishNames();
        int[] servingsToBuy = {1, 1, 0};
        BuyServingInputData inputData = new BuyServingInputData(dishNames, servingsToBuy);
        interactor.execute(inputData);

        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(12.0, playerDAO.getPlayer().getBalance());
        assertEquals("Transaction succeeded.", presenter.lastOutput.getMessage());
        assertEquals(1, pantryDAO.getPantry().getRecipe("Pizza").getStock());
        assertEquals(1, pantryDAO.getPantry().getRecipe("Burger").getStock());
    }

    @Test
    void failureTest() {
        TestPlayerDAO playerDAO = new TestPlayerDAO();
        TestPantryDAO pantryDAO = new TestPantryDAO();
        TestPresenter presenter = new TestPresenter();
        BuyServingInteractor interactor = new BuyServingInteractor(playerDAO, pantryDAO, presenter);

        String[] dishNames = pantryDAO.getPantry().getDishNames();
        int[] servingsToBuy = {2, 2, 0};
        BuyServingInputData inputData = new BuyServingInputData(dishNames, servingsToBuy);
        interactor.execute(inputData);

        assertFalse(presenter.lastOutput.isSuccess());
        assertEquals(30.0, playerDAO.getPlayer().getBalance());
        assertEquals("Transaction failed.", presenter.lastOutput.getMessage());
        assertEquals(0, pantryDAO.getPantry().getRecipe("Pizza").getStock());
        assertEquals(0, pantryDAO.getPantry().getRecipe("Burger").getStock());
    }

    @Test
    void cumulativeStockTest() {
        TestPlayerDAO playerDAO = new TestPlayerDAO();
        TestPantryDAO pantryDAO = new TestPantryDAO();
        TestPresenter presenter = new TestPresenter();
        BuyServingInteractor interactor = new BuyServingInteractor(playerDAO, pantryDAO, presenter);

        String[] dishNames = pantryDAO.getPantry().getDishNames();

        int[] servings1 = {0, 0, 2};
        BuyServingInputData inputData1 = new BuyServingInputData(dishNames, servings1);
        interactor.execute(inputData1);

        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(18.0, playerDAO.getPlayer().getBalance());
        assertEquals(2, pantryDAO.getPantry().getRecipe("Salad").getStock());

        int[] servings2 = {0, 0, 3};
        BuyServingInputData inputData2 = new BuyServingInputData(dishNames, servings2);
        interactor.execute(inputData2);

        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(0.0, playerDAO.getPlayer().getBalance());
        assertEquals(5, pantryDAO.getPantry().getRecipe("Salad").getStock());
    }
}

