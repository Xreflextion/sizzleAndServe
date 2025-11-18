package interface_adapter.BuyServing;

import interface_adapter.ViewModel;

import java.util.*;

public class BuyServingViewModel extends ViewModel<BuyServingViewModel.State> {

    public static class State{
        public String message = "";
        public double balance = 0;
        public boolean success = false;
        public String[] dishNames;
        public int[] dishCosts;
        public int[] dishStocks;
    }

    public BuyServingViewModel() {
        super("buyServing");
        setState(new State());
    }

    public void setDishNames(String[] names) {
        getState().dishNames = names;
        firePropertyChange();
    }

    public void setDishCosts(int[] costs) {
        getState().dishCosts = costs;
        firePropertyChange();
    }

    public void setDishStocks(int[] stocks) {
        getState().dishStocks = stocks;
        firePropertyChange();
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
