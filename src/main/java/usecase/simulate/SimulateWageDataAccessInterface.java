package usecase.simulate;

import entity.Employee;

public interface SimulateWageDataAccessInterface {
    Employee getEmployee(String position);

    int getTotalWage();
}
