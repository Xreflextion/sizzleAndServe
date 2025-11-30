package use_case.manage_wage;

import entity.Employee;
/**
 * Data Access Interface for Wage Management use case.
 * Provides methods to retrieve and save employee data.
 */
public interface WageUserDataAccessInterface {
    /**
     * Retrieves an employee by position.
     * @param position The position of the employee (e.g., "Cook", "Waiter")
     * @return The Employee object, or null if not found.
     */
    Employee getEmployee(String position);
    /**
     * Return the total wage of the all the employees in DAO.
     * @return the wage of all the employees, adding up together.
     */
    int getTotalWage();

    /**
     * Saves the updated employee data.
     * @param employee The Employee object to save.
     */
    void save(Employee employee);
}
