package interfaceadapter.office;

public class OfficeState {
    private int currentDay; // Simulation has already occurred for this day
    private double currentBalance; // Balance after simulation has occurred for this day
    private int currentCustomerCount; // The number of customers that occurred for this day

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


    public int getCurrentCustomerCount() {
        return currentCustomerCount;
    }

    public void setCurrentCustomerCount(int newCustomerCount) {
        currentCustomerCount = newCustomerCount;
    }


}
