package data_access;

import entity.Player;
import use_case.buy_serving.PlayerDataAccessInterface;
import use_case.manage_wage.WagePlayerDataAccessInterface;
import use_case.simulate.SimulatePlayerDataAccessInterface;

public class PlayerDataAccessObject implements PlayerDataAccessInterface,
        WagePlayerDataAccessInterface, SimulatePlayerDataAccessInterface {
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
