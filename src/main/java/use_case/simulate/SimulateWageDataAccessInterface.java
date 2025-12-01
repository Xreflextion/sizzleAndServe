package use_case.simulate;

import entity.Employee;

/**
 * Wage DAO interface for the Simulate Use Case.
 */
public interface SimulateWageDataAccessInterface {

    /**
     * Get an employee.
     * @param position the position of the employee
     * @return the employee
     */
    Employee getEmployee(String position);

    /**
     * Get the total wage of all employees.
     * @return the total wage
     */
    int getTotalWage();
}
