package interface_adapter.manage_wages;

import entity.Employee;

public class WageState {
    /**
     * The state for the Wage Management View Model.
     */
    private int waiterWage;
    private int cookWage;
    private float waiterWageEffect;
    private float cookWageEffect;
    private double currentBalance;
    private String warningMessage;

    public int getWaiterWage() {
        return waiterWage;
    }

    public void setWaiterWage(int waiterWage) {
        this.waiterWage = waiterWage;
    }

    public int getCookWage() {
        return cookWage;
    }

    public void setCookWage(int cookWage) {
        this.cookWage = cookWage;
    }

    public int getTotalWage() {
        return Employee.getTotalWage();
    }

    public float getWaiterWageEffect() {
        return waiterWageEffect;
    }

    public void setWaiterWageEffect(float waiterWageEffect) {
        this.waiterWageEffect = waiterWageEffect;
    }

    public float getCookWageEffect() {
        return cookWageEffect;
    }

    public void setCookWageEffect(float cookWageEffect) {
        this.cookWageEffect = cookWageEffect;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }
}
