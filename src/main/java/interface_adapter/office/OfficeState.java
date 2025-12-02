package interface_adapter.office;

public class OfficeState {
    // Simulation has already occurred for this day
    private int currentDay;

    // Balance after simulation has occurred for this day
    private double currentBalance;

    // The number of customers that occurred for this day
    private int pastCustomerCount;

    public int getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(int newDay) {
        currentDay = newDay;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double newBalance) {
        currentBalance = newBalance;
    }

    public int getPastCustomerCount() {
        return pastCustomerCount;
    }

    public void setPastCustomerCount(int newCustomerCount) {
        pastCustomerCount = newCustomerCount;
    }

}
