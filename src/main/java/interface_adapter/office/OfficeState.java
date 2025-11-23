package interface_adapter.office;

public class OfficeState {
    private int currentDay;
    private double currentBalance;
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
