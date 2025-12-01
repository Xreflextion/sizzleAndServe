package entity;

public class Employee {
    /**
    * Employee of the restaurant:
    * User can increase or decrease the wages of two employees: waiter & cook
    * every 1 increase in waiter's wage leads to 20 percentage point increase in rating
    * every 1 increase in cook's wage leads to -20 percentage point preparation time needed.
    */
    private static final int MIN_WAGE = 50;
    private static final int CHANGE_AMOUNT = 10;
    private static final float WAGE_EFFECT_STEP = 0.2f;
    // a static variable that is responsible for counting total wages and should be used in daily expense
    private static int totalWage;

    private int wage;
    private float wageEffect;
    private final String position;
    // initial creation put it at minWage

    public Employee(int wage, String position) {
        this.wage = Math.max(wage, MIN_WAGE);
        this.position = position;
        this.wageEffect = calculateInitialEffect(this.wage);
        totalWage += this.wage;
    }

    // divide wage - MIN_WAGE by 10 since wageEffect increases by 0.2 for every 10 wage increase
    private float calculateInitialEffect(int newWage) {
        return 1 + (newWage - MIN_WAGE) / CHANGE_AMOUNT * WAGE_EFFECT_STEP;
    }

    /**
     * Increases the wage of the employee by a fixed amount.
     */
    public void increaseWage() {
        this.wage += CHANGE_AMOUNT;
        this.wageEffect += WAGE_EFFECT_STEP;
        totalWage += CHANGE_AMOUNT;
    }

    /**
     * Decreases the wage of the employee by a fixed amount.
     */
    public void decreaseWage() {
        if (this.wage > MIN_WAGE) {
            this.wage -= CHANGE_AMOUNT;
            this.wageEffect -= WAGE_EFFECT_STEP;
            totalWage -= CHANGE_AMOUNT;
        }
    }

    public int getWage() {
        return wage;
    }

    public float getWageEffect() {
        return wageEffect;
    }

    public String getPosition() {
        return position;
    }

    public static int getTotalWage() {
        return totalWage;
    }
}
