package use_case.buy_serving;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import entity.Pantry;
import entity.Player;
import entity.Recipe;

class BuyServingInteractorTest {

    @Test
    void successTest() {
        final TestPlayerDataAccessObject playerDataAccessObject = new TestPlayerDataAccessObject();
        final TestPantryDataAccessObject pantryDataAccessObject = new TestPantryDataAccessObject();
        final TestPresenter presenter = new TestPresenter();
        final BuyServingInteractor interactor = new BuyServingInteractor(playerDataAccessObject,
                pantryDataAccessObject, presenter);

        final String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();
        final int[] servingsToBuy = {1, 1, 0};
        final BuyServingInputData inputData = new BuyServingInputData(dishNames, servingsToBuy);
        interactor.execute(inputData);

        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(12.0, playerDataAccessObject.getPlayer().getBalance());
        assertEquals("Transaction succeeded.", presenter.lastOutput.getMessage());
        assertEquals(1, pantryDataAccessObject.getPantry().getRecipe("Pizza").getStock());
        assertEquals(1, pantryDataAccessObject.getPantry().getRecipe("Burger").getStock());

        presenter.lastOutput.getDishCosts();
        presenter.lastOutput.getDishStocks();
        presenter.lastOutput.getNewBalance();
    }

    @Test
    void failureTest() {
        final TestPlayerDataAccessObject playerDataAccessObject = new TestPlayerDataAccessObject();
        final TestPantryDataAccessObject pantryDataAccessObject = new TestPantryDataAccessObject();
        final TestPresenter presenter = new TestPresenter();
        final BuyServingInteractor interactor = new BuyServingInteractor(playerDataAccessObject,
                pantryDataAccessObject, presenter);

        final String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();
        final int[] servingsToBuy = {2, 2, 0};
        final BuyServingInputData inputData = new BuyServingInputData(dishNames, servingsToBuy);
        interactor.execute(inputData);

        assertFalse(presenter.lastOutput.isSuccess());
        assertEquals(30.0, playerDataAccessObject.getPlayer().getBalance());
        assertEquals("Transaction failed.", presenter.lastOutput.getMessage());
        assertEquals(0, pantryDataAccessObject.getPantry().getRecipe("Pizza").getStock());
        assertEquals(0, pantryDataAccessObject.getPantry().getRecipe("Burger").getStock());

        presenter.lastOutput.getDishCosts();
        presenter.lastOutput.getDishStocks();
        presenter.lastOutput.getNewBalance();
    }

    @Test
    void cumulativeStockTest() {
        final TestPlayerDataAccessObject playerDataAccessObject = new TestPlayerDataAccessObject();
        final TestPantryDataAccessObject pantryDataAccessObject = new TestPantryDataAccessObject();
        final TestPresenter presenter = new TestPresenter();
        final BuyServingInteractor interactor = new BuyServingInteractor(playerDataAccessObject,
                pantryDataAccessObject, presenter);

        final String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();

        final int[] servings1 = {0, 0, 2};
        final BuyServingInputData inputData1 = new BuyServingInputData(dishNames, servings1);
        interactor.execute(inputData1);

        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(18.0, playerDataAccessObject.getPlayer().getBalance());
        assertEquals(2, pantryDataAccessObject.getPantry().getRecipe("Salad").getStock());

        presenter.lastOutput.getDishCosts();
        presenter.lastOutput.getDishStocks();
        presenter.lastOutput.getNewBalance();

        final int[] servings2 = {0, 0, 3};
        final BuyServingInputData inputData2 = new BuyServingInputData(dishNames, servings2);
        interactor.execute(inputData2);

        assertTrue(presenter.lastOutput.isSuccess());
        assertEquals(0.0, playerDataAccessObject.getPlayer().getBalance());
        assertEquals(5, pantryDataAccessObject.getPantry().getRecipe("Salad").getStock());

        presenter.lastOutput.getDishCosts();
        presenter.lastOutput.getDishStocks();
        presenter.lastOutput.getNewBalance();
    }

    // Test Player DAO
    static class TestPlayerDataAccessObject implements PlayerDataAccessInterface {
        private Player player = new Player("TestUser", 30.0);

        @Override
        public Player getPlayer() {
            return player;
        }

        @Override
        public void savePlayer(Player newPlayer) {

        }
    }

    // Test Pantry DAO
    static class TestPantryDataAccessObject implements PantryDataAccessInterface {
        private Pantry pantry = new Pantry();

        TestPantryDataAccessObject() {
            pantry.getPantry().put("Pizza", new Recipe("Pizza", 10));
            pantry.getPantry().put("Burger", new Recipe("Burger", 8));
            pantry.getPantry().put("Salad", new Recipe("Salad", 6));
        }

        @Override
        public Pantry getPantry() {
            return pantry;
        }

        @Override
        public void savePantry(Pantry newPantry) {

        }
    }

    // Test Presenter
    static class TestPresenter implements BuyServingOutputBoundary {
        private BuyServingOutputData lastOutput;

        @Override
        public void present(BuyServingOutputData outputData) {
            lastOutput = outputData;
        }
    }
}

