package use_case.buy_serving;

import entity.Pantry;
import entity.PerDayRecord;
import entity.Player;
import entity.Recipe;
import use_case.insights.performance_calculation.DayRecordsDataAccessInterface;

public class BuyServingInteractor implements BuyServingInputBoundary {
    private final PlayerDataAccessInterface playerDao;
    private final PantryDataAccessInterface pantryDao;
    private final DayRecordsDataAccessInterface dayRecordsDao;
    private final BuyServingOutputBoundary outputBoundary;

    public BuyServingInteractor(PlayerDataAccessInterface playerDao, PantryDataAccessInterface pantryDao,
                                DayRecordsDataAccessInterface dayRecordsDao,
                                BuyServingOutputBoundary outputBoundary) {
        this.playerDao = playerDao;
        this.pantryDao = pantryDao;
        this.dayRecordsDao = dayRecordsDao;
        this.outputBoundary = outputBoundary;
    }

    private double computeTotalCost(Pantry pantry, String[] dishNames, int[] servingsToBuy) {
        double total = 0;
        for (int i = 0; i < servingsToBuy.length; i++) {
            final Recipe recipe = pantry.getRecipe(dishNames[i]);
            total += recipe.getBasePrice() * servingsToBuy[i];
        }
        return total;
    }

    private boolean hasEnoughBalance(double balance, double totalCost) {
        final double epsilon = 1e-10;
        return balance + epsilon >= totalCost;
    }

    private void updateData(Player player, Pantry pantry, int today, PerDayRecord perDayRecord, String[] dishNames,
                            int[] servingsToBuy, double newBalance) {
        player.setBalance(newBalance);
        for (int i = 0; i < dishNames.length; i++) {
            pantry.addStock(dishNames[i], servingsToBuy[i]);
        }
        playerDao.savePlayer(player);
        pantryDao.savePantry(pantry);
        dayRecordsDao.updateDayData(today, perDayRecord);
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
        final int[] updatedDishCosts = new int[dishNames.length];
        final int[] updatedDishStocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            final Recipe recipe = pantry.getRecipe(dishNames[i]);
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
        final Player player = playerDao.getPlayer();
        final Pantry pantry = pantryDao.getPantry();
        final int today = dayRecordsDao.getNumberOfDays();
        final PerDayRecord perDayRecord = dayRecordsDao.getDayData(today);
        final String[] dishNames = inputData.getDishNames();
        final int[] servingsToBuy = inputData.getServingsToBuy();

        final double totalCost = computeTotalCost(pantry, dishNames, servingsToBuy);
        final double balance = player.getBalance();

        final double updatedExpense = perDayRecord.getExpenses() + totalCost;

        final PerDayRecord updatedDayRecord = new PerDayRecord(
                perDayRecord.getRevenue(),
                updatedExpense,
                perDayRecord.getRating()
        );

        if (!hasEnoughBalance(balance, totalCost)) {
            outputBoundary.present(prepareFailureOutput(balance));
        }
        else {
            final double newBalance = balance - totalCost;
            updateData(player, pantry, today, updatedDayRecord, dishNames, servingsToBuy, newBalance);
            outputBoundary.present(prepareSuccessOutput(newBalance, pantry, dishNames));
        }
    }
}
