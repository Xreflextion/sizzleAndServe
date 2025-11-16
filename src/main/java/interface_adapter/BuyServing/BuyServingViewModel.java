package interface_adapter.BuyServing;

import entity.Player;
import entity.Pantry;
import entity.Recipe;
import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;

public class BuyServingViewModel extends ViewModel<BuyServingViewModel.State> {

    public static class State{
        public String message = "";
        public double balance = 0;
        public boolean success = false;
    }

    private final Player player;
    private Pantry pantry;
    private final List<Recipe> recipes;

    public BuyServingViewModel(Player player, Pantry pantry, List<Recipe> recipes) {
        super("buyServing");
        this.player = player;
        this.pantry = pantry;
        this.recipes = recipes;
        State initialState = new State();
        initialState.balance = player.getBalance();
        setState(initialState);
    }

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
        getState().message = message;
        firePropertyChange();
    }

    public void setNewBalance(double newBalance) {
        getState().balance = newBalance;
        firePropertyChange();
    }

    public void setSuccess(boolean success) {
        getState().success = success;
        firePropertyChange();
    }
}
