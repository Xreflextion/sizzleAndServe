package use_case.simulate;

import entity.Employee;

public interface SimulateWageDataAccessInterface {
    Employee getEmployee(String position);
}
