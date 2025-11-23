package use_case.simulate;

public class SimulateOutputData {
    private final int currentDay;
    private final double currentBalance;
    private final int pastCustomerCount;

    public SimulateOutputData(int currentDay, double currentBalance, int pastCustomerCount) {
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
