package Use_Case;

import sizzleAndServe.Employee;
/**
 * Output data for the Wage Management use case.
 * Contains updated wage and wage effect for the employee.
 */
public class WageOutputData {
    private final String position;
    private final int wageAfter;
    private final float wageEffectAfter;

    public WageOutputData(Employee employee) {
        this.position = employee.getPosition();
        this.wageAfter = employee.getWage();
        this.wageEffectAfter = employee.getWageEffect();
    }

    public String getPosition() {
        return position;
    }

    public int getWageAfter() {
        return wageAfter;
    }

    public float getWageEffectAfter() {
        return wageEffectAfter;
    }
}

