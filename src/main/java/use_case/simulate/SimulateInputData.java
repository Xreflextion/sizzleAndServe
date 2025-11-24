package use_case.simulate;

public class SimulateInputData {
    private int currentDay;
    private int currentCustomerCount;

    public SimulateInputData(int currentDay, int currentCustomerCount) {
        this.currentDay = currentDay;
        this.currentCustomerCount = currentCustomerCount;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentCustomerCount() {return currentCustomerCount;}




}
