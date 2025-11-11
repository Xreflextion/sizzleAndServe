package sizzleAndServe;


public abstract class Employee {
   /* Employee of the restaurant:
   User can increase or decrease the wages of two employees: waiter & cook
   every 1 increase in waiter's wage leads to 20 percentage point increase in rating
   every 1 increase in cook's wage leads to -20 percentage point preparation time needed
    */
    protected static final int minWage = 1;
    protected int wage;
    /*
     initial creation put it at minWage
     */
    protected static int totalWage = 0;
    /*
    a static variable that is responsible for counting total wages and should be used in daily expense
     */
    public Employee(int wage) {
        if (wage < minWage) {
            this.wage = minWage;
        }
        else {
            this.wage = wage;
        }
        totalWage += this.wage;
    }
    public void increaseWage(int amount){
        this.wage += amount;
    }

    public void decreaseWage(int amount){
        int proposedWage = this.wage + amount;
        if  (proposedWage >= minWage) {
            this.wage -= amount;}
        else {this.wage = minWage;}
    }
    public abstract void applyWageEffect();

}
