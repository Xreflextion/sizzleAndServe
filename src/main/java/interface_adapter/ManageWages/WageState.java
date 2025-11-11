package interface_adapter.ManageWages;

public class WageState {
    /**
     * The state for the Wage Management View Model.
     */
    private int WaiterWage = 1;
    private int CookWage = 1;
    private float WaiterWageEffect = 1;
    private float CookWageEffect = 1;
    private String BelowMiniumWageError;

    public int getWaiterWage() {
        return WaiterWage;
    }

    public int getCookWage() {
        return CookWage;
    }

    public float getWaiterWageEffect() {
        return WaiterWageEffect;
    }
    public float getCookWageEffect() {
        return CookWageEffect;
    }

    public void increaseWaiterWage() {
        WaiterWage++;
        WaiterWageEffect += 0.2F;
    }

    public void decreaseWaiterWage() {
        WaiterWage--;
        WaiterWageEffect -= 0.2F;
    }

    public void increaseCookWage() {
        CookWage++;
        CookWageEffect += 0.2F;
    }

    public void decreaseCookWage() {
        CookWage--;
        CookWageEffect -= 0.2F;
    }

    public String getBelowMiniumWageError() {
        return BelowMiniumWageError;
    }

    public void setBelowMiniumWageError(String belowMiniumWageError) {
        BelowMiniumWageError = belowMiniumWageError;
    }

}
