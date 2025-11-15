package interface_adapter.BuyServing;

import entity.Player;
import entity.Pantry;
import entity.Recipe;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class BuyServingViewModel {

    public static class State{
        public String message = "";
        public double balance = 0;
        public boolean success = false;
    }

    private final State state = new State();
    private final Player player;
    private Pantry pantry;
    private final List<Recipe> recipes;
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public BuyServingViewModel(Player player, Pantry pantry, List<Recipe> recipes) {
        this.player = player;
        this.pantry = pantry;
        this.recipes = recipes;
        this.state.balance = player.getBalance();
    }

    public State getState() { return state; }

    public Player getPlayer() { return player; }

    public Pantry getPantry() { return pantry; }

    public String getDishName(int i) {
        return recipes.get(i).getName();
    }

    public int getDishCost(int i) {
        return recipes.get(i).getCost();
    }
    public int getDishStock(int i) {
        return recipes.get(i).getStock();
    }

    public void setMessage(String message) {
        state.message = message;
    }

    public void setNewBalance(double newBalance) {
        state.balance = newBalance;
    }

    public void setSuccess(boolean success) {
        state.success = success;
    }

    // Notify the View the state has changed
    public void firePropertyChange(String buyServing) {
        support.firePropertyChange("buyServing", null, state);
    }

    // View registers to the update event of the ViewModel.
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}
