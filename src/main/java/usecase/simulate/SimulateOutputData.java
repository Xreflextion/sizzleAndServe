package usecase.simulate;

import java.util.Map;

public class SimulateOutputData {
    private final int currentDay;
    private final double currentBalance;
    private final int currentCustomerCount;
    private final Map<String, Integer> stock;

    public SimulateOutputData(int currentDay, double currentBalance, int currentCustomerCount, Map<String, Integer>  stock) {
        this.currentDay = currentDay;
        this.currentBalance = currentBalance;
        this.currentCustomerCount = currentCustomerCount;
        this.stock = stock;
    }

    public int getCurrentDay() {
        return currentDay;
    }
    public double getCurrentBalance() {
        return currentBalance;
    }
    public int getCurrentCustomerCount() {return currentCustomerCount;}

    public Map<String, Integer> getStock() {
        return stock;
    }

}
