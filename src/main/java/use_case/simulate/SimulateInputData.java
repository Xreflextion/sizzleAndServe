package use_case.simulate;

public class SimulateInputData {
    private int currentBalance;
    private int currentDay;
    private int pastCustomerCount;

    public SimulateInputData(int currentDay, int currentBalance, int pastCustomerCount) {
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
