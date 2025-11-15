package use_case.BuyServing;

import entity.Player;
import entity.Pantry;
import entity.Recipe;

public class BuyServingInteractor implements BuyServingInputBoundary{
    private final BuyServingDataAccessInterface dataAccess;
    private final BuyServingOutputBoundary outputBoundary;

    public BuyServingInteractor(BuyServingDataAccessInterface dataAccess, BuyServingOutputBoundary outputBoundary){
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(BuyServingInputData inputData) {
        Player player = dataAccess.getPlayer();
        Pantry pantry = dataAccess.getPantry();
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
                    "Insufficient balance",
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

        dataAccess.savePlayer(player);
        dataAccess.savePantry(pantry);

        BuyServingOutputData output = new BuyServingOutputData(
                true,
                "Transaction successful",
                newBalance
        );

        outputBoundary.present(output);
    }
}
