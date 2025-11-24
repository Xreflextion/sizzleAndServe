package use_case.simulate;

public class SimulateInputData {
    private double currentBalance;
    private int currentDay;
    private int currentCustomerCount;

    public SimulateInputData(int currentDay, double currentBalance, int currentCustomerCount) {
        this.currentDay = currentDay;
        this.currentBalance = currentBalance;
        this.currentCustomerCount = currentCustomerCount;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public int getCurrentCustomerCount() {return currentCustomerCount;}




}
