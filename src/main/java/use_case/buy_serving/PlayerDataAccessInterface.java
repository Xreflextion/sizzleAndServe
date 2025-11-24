package use_case.buy_serving;

import entity.Player;

public interface PlayerDataAccessInterface {
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
