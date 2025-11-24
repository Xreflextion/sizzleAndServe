package data_access;

import entity.Player;
import use_case.buy_serving.PlayerDataAccessInterface;

public class PlayerDataAccessObject implements PlayerDataAccessInterface,
        WagePlayerDataAccessInterface {
    private final Player player;

    public PlayerDataAccessObject(double balance) {
        this.player = new Player("Name", balance);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void savePlayer(Player player) {}
}
