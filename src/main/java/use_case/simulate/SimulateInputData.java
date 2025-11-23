package use_case.simulate;

public class SimulateInputData {
    private double currentBalance;
    private int currentDay;
    private int pastCustomerCount;

    public SimulateInputData(int currentDay, double currentBalance, int pastCustomerCount) {
        this.currentDay = currentDay;
        this.currentBalance = currentBalance;
        this.pastCustomerCount = pastCustomerCount;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public int getPastCustomerCount() {return pastCustomerCount;}




}
