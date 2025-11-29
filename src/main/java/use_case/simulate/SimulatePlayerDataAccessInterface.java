package use_case.simulate;

import entity.Player;

public interface SimulatePlayerDataAccessInterface {

    Player getPlayer();

    void savePlayer(Player player);
}
