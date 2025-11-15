package use_case.BuyServing;

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

        for (int i = 0; i < dishNames.length; i++) {
            Recipe recipe = pantry.getRecipe(dishNames[i]);
            if (recipe == null) {
                BuyServingOutputData output = new BuyServingOutputData(
                        false,
                        "Dish not found: " + dishNames[i],
                        player.getBalance()
                );
                outputBoundary.present(output);
                return;
            }
            totalCost += recipe.getCost() * servingsToBuy[i];
        }

        double balance = player.getBalance();

        if (balance < totalCost) {
            BuyServingOutputData output = new BuyServingOutputData(
                    false,
                    "Transaction failed.",
                    balance
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

        BuyServingOutputData output = new BuyServingOutputData(
                true,
                "Transaction succeeded.",
                newBalance
        );

        outputBoundary.present(output);
    }
}
