package use_case.simulate;

public class SimulateOutputData {
    private final int currentDay;
    private final double currentBalance;
    private final int currentCustomerCount;

    public SimulateOutputData(int currentDay, double currentBalance, int currentCustomerCount) {
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
