package interface_adapter.buy_serving;

import interface_adapter.ViewModel;

public class BuyServingViewModel extends ViewModel<BuyServingViewModel.State> {

    public static final String VIEW_NAME = "buyServing";

    public BuyServingViewModel() {
        super("buyServing");
        setState(new State());
    }

    /**
     * Updates the list of dish names in the view state and notifies the view.
     *
     * @param dishNames the names of the dishes available for the user to purchase
     */
    public void setDishNames(String[] dishNames) {
        getState().dishNames = dishNames;
        firePropertyChange();
    }

    /**
     * Updates the list of dish costs in the view state and notifies the view.
     *
     * @param costs the cost of each dish corresponding to the stored dish names
     */
    public void setDishCosts(int[] costs) {
        getState().dishCosts = costs;
        firePropertyChange();
    }

    /**
     * Updates the list of dish stock counts in the view state and notifies the view.
     *
     * @param stocks the number of servings remaining for each dish
     */
    public void setDishStocks(int[] stocks) {
        getState().dishStocks = stocks;
        firePropertyChange();
    }

    /**
     * Sets the message to be displayed in the view and notifies the view.
     *
     * @param message the message describing the result of the purchase attempt
     */
    public void setMessage(String message) {
        getState().message = message;
        firePropertyChange();
    }

    /**
     * Updates the user's balance in the view state and notifies the view.
     *
     * @param newBalance the updated balance after the purchase
     */
    public void setNewBalance(double newBalance) {
        getState().balance = newBalance;
        firePropertyChange();
    }

    /**
     * Sets whether the purchase was successful and notifies the view.
     *
     * @param success true if the purchase succeeded, false otherwise
     */
    public void setSuccess(boolean success) {
        getState().success = success;
        firePropertyChange();
    }

    public static class State {
        public String message = "";
        public double balance = 0;
        public boolean success = false;
        public String[] dishNames;
        public int[] dishCosts;
        public int[] dishStocks;
    }
}
