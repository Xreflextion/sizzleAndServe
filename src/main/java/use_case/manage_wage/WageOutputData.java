package use_case.manage_wage;

import entity.Employee;
/**
 * Output data for the Wage Management use case.
 * Contains updated wage and wage effect for the employee.
 */
public class WageOutputData {
    private final String position;
    private final int wageAfter;
    private final float wageEffectAfter;

    private final int totalWage;
    private final double currentBalance;


    public WageOutputData(Employee employee,int totalWage, double currentBalance) {
        this.position = employee.getPosition();
        this.wageAfter = employee.getWage();
        this.wageEffectAfter = employee.getWageEffect();
        this.totalWage = totalWage;
        this.currentBalance = currentBalance;

    }

    public String getPosition() { return position; }

    public int getWageAfter() { return wageAfter; }

    public float getWageEffectAfter() { return wageEffectAfter; }

    public int getTotalWage() { return totalWage; }

    public double getCurrentBalance() { return currentBalance; }
}

