package entity;


public class Employee {
   /* Employee of the restaurant:
   User can increase or decrease the wages of two employees: waiter & cook
   every 1 increase in waiter's wage leads to 20 percentage point increase in rating
   every 1 increase in cook's wage leads to -20 percentage point preparation time needed
    */
    private static final int MIN_WAGE = 50;
    private static int totalWage = 0;
    /*
    a static variable that is responsible for counting total wages and should be used in daily expense
     */

    private int wage;
    private float wageEffect;
    private final String position;/*
     initial creation put it at minWage
     */


    public Employee(int wage, String position) {
        this.wage = Math.max(wage, MIN_WAGE);
        this.position = position;
        this.wageEffect = calculateInitialEffect(this.wage);
        totalWage += this.wage;
    }

    // divide wage - MIN_WAGE by 10 since wageEffect increases by 0.2 for every 10 wage increase
    private float calculateInitialEffect(int wage) {
        return 1 + (wage - MIN_WAGE)/10 * 0.2f;
    }

    public void increaseWage() {
        this.wage += 10;
        this.wageEffect += 0.2f;
        totalWage += 10;
    }

    public void decreaseWage() {
        if (this.wage > MIN_WAGE) {
            this.wage -= 10;
            this.wageEffect -= 0.2f;
            totalWage -= 10;
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
