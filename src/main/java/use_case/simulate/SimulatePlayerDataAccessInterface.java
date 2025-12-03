package use_case.simulate;

import entity.Player;

/**
 * Player DAO interface for the Simulate Use Case.
 */
public interface SimulatePlayerDataAccessInterface {

    /**
     * Get the player.
     *
     * @return the stored player
     */
    Player getPlayer();

    /**
     * Save the given player.
     *
     * @param player the player to save
     */
    void savePlayer(Player player);
}
