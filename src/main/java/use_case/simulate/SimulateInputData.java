package use_case.simulate;

public class SimulateInputData {
    private int previousDay;
    private int previousCustomerCount;

    public SimulateInputData(int previousDay, int previousCustomerCount) {
        this.previousDay = previousDay;
        this.previousCustomerCount = previousCustomerCount;
    }

    public int getPreviousDay() {
        return previousDay;
    }

    public int getPreviousCustomerCount() {
        return previousCustomerCount;
    }

}
