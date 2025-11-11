package Use_Case.WaiterWageIncrease;

import sizzleAndServe.Waiter;

public class WaiterWageIncreaseOutputData {
    //** Output data for WaiterWageIncrease: including the increased wage and rating effect
    //**

    private int waiterWageAfter;
    private float ratingEffectAfter;

    public WaiterWageIncreaseOutputData(Waiter waiter) {
        this.waiterWageAfter = waiter.getWage();
        this.ratingEffectAfter = waiter.getRatingEffect();
    }



}
