package interface_adapter.office;

public class OfficeState {
    private int currentDay;
    private int currentBalance;
    private int pastCustomerCount;

    public int getCurrentDay() {
        return currentDay;
    }
    public void setCurrentDay(int newDay) {
        currentDay = newDay;
    }

    public int getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(int newBalance) {
        currentBalance = newBalance;
    }


    public int getPastCustomerCount() {
        return pastCustomerCount;
    }

    public void setPastCustomerCount(int newCustomerCount) {
        pastCustomerCount = newCustomerCount;
    }


}
