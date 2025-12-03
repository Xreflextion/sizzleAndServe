package data_access;

import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import constants.Constants;
import entity.Player;
import use_case.buy_serving.PlayerDataAccessInterface;
import use_case.manage_wage.WagePlayerDataAccessInterface;
import use_case.simulate.SimulatePlayerDataAccessInterface;

public class PlayerDataAccessObject implements PlayerDataAccessInterface,
        WagePlayerDataAccessInterface, SimulatePlayerDataAccessInterface {
    public static final String NAME = "name";
    public static final String BALANCE = "balance";
    private Player player;
    private FileHelperObject fileHelperObject;

    public PlayerDataAccessObject(double balance, FileHelperObject fileHelperObject) {

        this.fileHelperObject = fileHelperObject;

        final JsonArray playerObjectArray = fileHelperObject.getArrayFromSaveData(Constants.PLAYER_KEY);

        JsonObject playerObject = new JsonObject();

        if (!playerObjectArray.isEmpty()) {
            playerObject = playerObjectArray.get(0).getAsJsonObject();
        }

        String name = "Name";
        if (playerObject.keySet().contains(NAME)) {
            name = playerObject.get(NAME).getAsString();
        }
        double loadedBalance = balance;
        if (playerObject.keySet().contains(BALANCE)) {
            loadedBalance = playerObject.get(BALANCE).getAsDouble();
        }
        this.player = new Player(name, loadedBalance);

    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void savePlayer(Player newPlayer) {
        this.player = newPlayer;
        save();
    }

    /**
     * Serializes the current state of the player.
     *
     * @throws IOException if an IO error occurs during the file saving process
     */
    private void saveToFile() throws IOException {
        final JsonArray playerArray = new JsonArray();
        final JsonObject playerObject = new JsonObject();
        playerObject.addProperty("name", player.getName());
        playerObject.addProperty("balance", player.getBalance());
        playerArray.add(playerObject);
        fileHelperObject.saveArray(Constants.PLAYER_KEY, playerArray);
    }

    /**
     * Saves the current state of the player data to a JSON file.
     */
    public void save() {
        if (fileHelperObject != null) {
            try {
                saveToFile();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
}
