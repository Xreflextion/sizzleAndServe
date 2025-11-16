package data_access;

import entity.Player;
import use_case.BuyServing.PlayerDataAccessInterface;

public class PlayerDataAccessObject implements PlayerDataAccessInterface {
    private final Player player;

    public PlayerDataAccessObject() {
        this.player = new Player("Name", 100);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void savePlayer(Player player) {}
}
