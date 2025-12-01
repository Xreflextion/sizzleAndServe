package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import constants.Constants;
import entity.Player;
import use_case.buy_serving.PlayerDataAccessInterface;
import use_case.manage_wage.WagePlayerDataAccessInterface;
import use_case.simulate.SimulatePlayerDataAccessInterface;

import java.io.IOException;

public class PlayerDataAccessObject implements PlayerDataAccessInterface,
        WagePlayerDataAccessInterface, SimulatePlayerDataAccessInterface {
    private Player player;
    private FileHelperObject fileHelperObject;

    public PlayerDataAccessObject(double balance, FileHelperObject fileHelperObject) {

        this.fileHelperObject = fileHelperObject;

        JsonArray playerObjectArray = fileHelperObject.getArrayFromSaveData(Constants.PLAYER_KEY);

        JsonObject playerObject = new JsonObject();

        if (playerObjectArray.size() >= 1) {
            playerObject = playerObjectArray.get(0).getAsJsonObject();
        }

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
    public void savePlayer(Player player) {
        this.player = player;
        save();
    }

    private void saveToFile() throws IOException {
        JsonArray playerArray = new JsonArray();
        JsonObject playerObject = new JsonObject();
        playerObject.addProperty("name", player.getName());
        playerObject.addProperty("balance", player.getBalance());
        playerArray.add(playerObject);
        fileHelperObject.saveArray(Constants.PLAYER_KEY, playerArray);
    }

    public void save() {
        try {
            saveToFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
