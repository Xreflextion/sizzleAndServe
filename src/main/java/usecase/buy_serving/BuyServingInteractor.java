package usecase.buy_serving;

import entity.Player;
import entity.Pantry;
import entity.Recipe;

public class BuyServingInteractor implements BuyServingInputBoundary{
    private final PlayerDataAccessInterface playerDao;
    private final PantryDataAccessInterface pantryDao;
    private final BuyServingOutputBoundary outputBoundary;

    public BuyServingInteractor(PlayerDataAccessInterface playerDao, PantryDataAccessInterface pantryDao, BuyServingOutputBoundary outputBoundary){
        this.playerDao = playerDao;
        this.pantryDao = pantryDao;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(BuyServingInputData inputData) {
        Player player = playerDao.getPlayer();
        Pantry pantry = pantryDao.getPantry();
        String[] dishNames = inputData.getDishNames();
        int[] servingsToBuy = inputData.getServingsToBuy();
        double totalCost = 0;

        for (int i = 0; i < servingsToBuy.length; i++) {
            Recipe recipe = pantry.getRecipe(dishNames[i]);
            totalCost += recipe.getBasePrice() * servingsToBuy[i];
        }

        double balance = player.getBalance();
        final double EPSILON = 1e-10;

        if (balance + EPSILON < totalCost) {
            BuyServingOutputData output = new BuyServingOutputData(
                    false,
                    "Transaction failed.",
                    balance,
                    null,
                    null
            );
            outputBoundary.present(output);
            return;
        }

        double newBalance = balance - totalCost;
        player.setBalance(newBalance);

        for (int i = 0; i < dishNames.length; i++) {
            pantry.addStock(dishNames[i], servingsToBuy[i]);
        }

        playerDao.savePlayer(player);
        pantryDao.savePantry(pantry);

        int[] updatedDishCosts = new int[dishNames.length];
        int[] updatedDishStocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            Recipe recipe = pantry.getRecipe(dishNames[i]);
            updatedDishCosts[i] = recipe.getBasePrice();
            updatedDishStocks[i] = recipe.getStock();
        }

        BuyServingOutputData output = new BuyServingOutputData(
                true,
                "Transaction succeeded.",
                newBalance,
                updatedDishCosts,
                updatedDishStocks
        );

        outputBoundary.present(output);
    }
}
