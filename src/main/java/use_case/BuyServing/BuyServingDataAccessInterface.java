package use_case.BuyServing;

import entity.Player;
import entity.Pantry;

/**
 * The Data Access Interface for the Buy Servings Use Case.
 */
public interface BuyServingDataAccessInterface {

    /**
     * Retrieves the current player.
     * @return the Player entity
     */
    Player getPlayer();

    /**
     * Saves the updated player information.
     * @param player the Player entity with updated balance
     */
    void savePlayer(Player player);

    /**
     * Retrieves the pantry containing all recipes.
     * @return the Pantry entity
     */
    Pantry getPantry();

    /**
     * Saves the updated pantry state.
     * @param pantry the Pantry entity with updated stock
     */
    void savePantry(Pantry pantry);
}
