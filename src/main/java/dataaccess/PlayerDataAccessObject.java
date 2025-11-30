package dataaccess;

import com.google.gson.JsonObject;
import constants.Constants;
import entity.Player;
import usecase.buy_serving.PlayerDataAccessInterface;
import usecase.manage_wage.WagePlayerDataAccessInterface;
import usecase.simulate.SimulatePlayerDataAccessInterface;

public class PlayerDataAccessObject implements PlayerDataAccessInterface,
        WagePlayerDataAccessInterface, SimulatePlayerDataAccessInterface {
    private final Player player;
    private FileHelperObject fileHelperObject;

    public PlayerDataAccessObject(double balance, FileHelperObject fileHelperObject) {

        this.fileHelperObject = fileHelperObject;

        JsonObject playerObject = fileHelperObject.getObjectFromSaveData(Constants.PLAYER_KEY);

        String name = "Name";
        if (playerObject.keySet().contains("name")) {
            name = playerObject.get("name").getAsString();
        }
        if (playerObject.keySet().contains("balance")) {
            balance = playerObject.get("balance").getAsDouble();
        }
        this.player = new Player(name, balance);

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void savePlayer(Player player) {}
}
