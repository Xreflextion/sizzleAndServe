package use_case.simulate;

public class SimulateOutputData {
    private final int currentDay;
    private final int currentBalance;
    private final int pastCustomerCount;

    public SimulateOutputData(int currentDay, int currentBalance, int pastCustomerCount) {
        this.currentDay = currentDay;
        this.currentBalance = currentBalance;
        this.pastCustomerCount = pastCustomerCount;
    }

    public int getCurrentDay() {
        return currentDay;
    }
    public int getCurrentBalance() {
        return currentBalance;
    }
    public int getPastCustomerCount() {return pastCustomerCount;}

}
