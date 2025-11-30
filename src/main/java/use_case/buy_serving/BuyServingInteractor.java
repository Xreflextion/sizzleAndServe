package use_case.buy_serving;

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

    private double computeTotalCost(Pantry pantry, String[] dishNames, int[] servingsToBuy) {
        double total = 0;
        for (int i = 0; i < servingsToBuy.length; i++) {
            Recipe recipe = pantry.getRecipe(dishNames[i]);
            total += recipe.getBasePrice() * servingsToBuy[i];
        }
        return total;
    }

    private boolean hasEnoughBalance(double balance, double totalCost) {
        final double EPSILON = 1e-10;
        return balance + EPSILON >= totalCost;
    }

    private void updatePantryAndPlayer(Player player, Pantry pantry, String[] dishNames, int[] servingsToBuy, double newBalance) {
        player.setBalance(newBalance);
        for (int i = 0; i < dishNames.length; i++) {
            pantry.addStock(dishNames[i], servingsToBuy[i]);
        }
        playerDao.savePlayer(player);
        pantryDao.savePantry(pantry);
    }

    private BuyServingOutputData prepareFailureOutput(double balance) {
        return new BuyServingOutputData(
                false,
                "Transaction failed.",
                balance,
                null,
                null
        );
    }

    private BuyServingOutputData prepareSuccessOutput(double newBalance, Pantry pantry, String[] dishNames) {
        int[] updatedDishCosts = new int[dishNames.length];
        int[] updatedDishStocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            Recipe recipe = pantry.getRecipe(dishNames[i]);
            updatedDishCosts[i] = recipe.getBasePrice();
            updatedDishStocks[i] = recipe.getStock();
        }
        return new BuyServingOutputData(
                true,
                "Transaction succeeded.",
                newBalance,
                updatedDishCosts,
                updatedDishStocks
        );
    }

    @Override
    public void execute(BuyServingInputData inputData) {
        Player player = playerDao.getPlayer();
        Pantry pantry = pantryDao.getPantry();
        String[] dishNames = inputData.getDishNames();
        int[] servingsToBuy = inputData.getServingsToBuy();

        double totalCost = computeTotalCost(pantry, dishNames, servingsToBuy);
        double balance = player.getBalance();

        if (!hasEnoughBalance(balance, totalCost)) {
            outputBoundary.present(prepareFailureOutput(balance));
            return;
        }

        double newBalance = balance - totalCost;
        updatePantryAndPlayer(player, pantry, dishNames, servingsToBuy, newBalance);

        outputBoundary.present(prepareSuccessOutput(newBalance, pantry, dishNames));
    }
}
