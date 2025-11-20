package interface_adapter.manage_wages;

public class WageState {
    /**
     * The state for the Wage Management View Model.
     */
    private int waiterWage;
    private int cookWage;
    private float waiterWageEffect;
    private float cookWageEffect;

    public void setWaiterWage(int waiterWage) {
        this.waiterWage = waiterWage;
    }

    public void setCookWage(int cookWage) {
        this.cookWage = cookWage;
    }

    public void setWaiterWageEffect(float waiterWageEffect) {
        this.waiterWageEffect = waiterWageEffect;
    }
    public void setCookWageEffect(float cookWageEffect) {
        this.cookWageEffect = cookWageEffect;
    }

    public int getWaiterWage() {
        return waiterWage;
    }

    public int getCookWage() {
        return cookWage;
    }

    public float getWaiterWageEffect() {
        return waiterWageEffect;
    }

    public float getCookWageEffect() {
        return cookWageEffect;
    }
}
