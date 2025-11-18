package use_case.manage_wage;

import entity.Employee;

import java.util.Map;
/**
        * Interactor for managing employee wages.
 * Implements the WageInputBoundary and coordinates between data access and presenter.
        */
public class WageInteractor implements WageInputBoundary {
    private final WageUserDataAccessInterface dataAccess;
    private final WageOutputBoundary presenter;
    private final Map<String, Employee> employees;

    public WageInteractor(WageUserDataAccessInterface dataAccess,
                          WageOutputBoundary presenter,
                          Map<String, Employee> employees) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
        this.employees = employees;
    }

    @Override
    public void increaseWage(String position) {
        Employee currentEmployee = employees.get(position);
        if (currentEmployee != null) {
            currentEmployee.increaseWage();
            dataAccess.save(currentEmployee); // ✅ Persist changes
            WageOutputData outputData = new WageOutputData(currentEmployee);
            presenter.prepareSuccessView(outputData);
        }
    }

    @Override
    public void decreaseWage(String position) {
        Employee currentEmployee = employees.get(position);
        if (currentEmployee != null) {
            currentEmployee.decreaseWage();
            dataAccess.save(currentEmployee); // ✅ Persist changes
            WageOutputData outputData = new WageOutputData(currentEmployee);
            presenter.prepareSuccessView(outputData);
        }
    }
}
