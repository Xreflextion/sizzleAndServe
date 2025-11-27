package use_case.simulate;

import java.util.Map;

public class SimulateOutputData {
    private final int currentDay;
    private final double currentBalance;
    private final int currentCustomerCount;
    private final Map<String, Integer> stocks;

    public SimulateOutputData(int currentDay, double currentBalance, int currentCustomerCount, Map<String, Integer>  stocks) {
        this.currentDay = currentDay;
        this.currentBalance = currentBalance;
        this.currentCustomerCount = currentCustomerCount;
        this.stocks = stocks;
    }

    public int getCurrentDay() {
        return currentDay;
    }
    public double getCurrentBalance() {
        return currentBalance;
    }
    public int getCurrentCustomerCount() {return currentCustomerCount;}

    public Map<String, Integer> getStocks() {
        return stocks;
    }

}
