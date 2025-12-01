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
        getState().setDishNames(dishNames);
        firePropertyChange();
    }

    /**
     * Updates the list of dish costs in the view state and notifies the view.
     *
     * @param costs the cost of each dish corresponding to the stored dish names
     */
    public void setDishCosts(int[] costs) {
        getState().setDishCosts(costs);
        firePropertyChange();
    }

    /**
     * Updates the list of dish stock counts in the view state and notifies the view.
     *
     * @param stocks the number of servings remaining for each dish
     */
    public void setDishStocks(int[] stocks) {
        getState().setDishStocks(stocks);
        firePropertyChange();
    }

    /**
     * Sets the message to be displayed in the view and notifies the view.
     *
     * @param message the message describing the result of the purchase attempt
     */
    public void setMessage(String message) {
        getState().setMessage(message);
        firePropertyChange();
    }

    /**
     * Updates the user's balance in the view state and notifies the view.
     *
     * @param newBalance the updated balance after the purchase
     */
    public void setNewBalance(double newBalance) {
        getState().setBalance(newBalance);
        firePropertyChange();
    }

    /**
     * Sets whether the purchase was successful and notifies the view.
     *
     * @param success true if the purchase succeeded, false otherwise
     */
    public void setSuccess(boolean success) {
        getState().setSuccess(success);
        firePropertyChange();
    }

    public static class State {
        private String message = "";
        private double balance;
        private boolean success;
        private String[] dishNames;
        private int[] dishCosts;
        private int[] dishStocks;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String[] getDishNames() {
            return dishNames;
        }

        public void setDishNames(String[] dishNames) {
            this.dishNames = dishNames;
        }

        public int[] getDishCosts() {
            return dishCosts;
        }

        public void setDishCosts(int[] dishCosts) {
            this.dishCosts = dishCosts;
        }

        public int[] getDishStocks() {
            return dishStocks;
        }

        public void setDishStocks(int[] dishStocks) {
            this.dishStocks = dishStocks;
        }
    }
}
