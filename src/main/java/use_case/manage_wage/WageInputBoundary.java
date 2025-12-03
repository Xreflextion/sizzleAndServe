package use_case.manage_wage;

/**
 * Input Boundary for the Wage Management use case.
 * Allows increasing or decreasing the wage of an employee by position.
 */
public interface WageInputBoundary {

    /**
     * Increases the wage of the employee with the given position.
     *
     * @param position The position of the employee (e.g., "Cook", "Waiter")
     */
    void increaseWage(String position);

    /**
     * Decreases the wage of the employee with the given position.
     *
     * @param position The position of the employee (e.g., "Cook", "Waiter")
     */
    void decreaseWage(String position);
}

