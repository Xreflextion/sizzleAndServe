package use_case.manage_wage;

import entity.Player;

/**
 * The other Data Access Interface for Wage Management use case
 * Provides methods to retrieve and saved player data.
 */
public interface WagePlayerDataAccessInterface {
    /**
     * Retrieves the current player.
     *
     * @return the Player entity
     */
    Player getPlayer();
    /**
     * Saves the updated player information.
     *
     * @param player the Player entity with updated balance
     */
    void savePlayer(Player player);
}
