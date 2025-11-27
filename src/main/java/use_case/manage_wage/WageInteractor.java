package use_case.manage_wage;

import entity.Employee;

import java.util.Map;
/**
        * Interactor for managing employee wages.
 * Implements the WageInputBoundary and coordinates between data access and presenter.
        */
public class WageInteractor implements WageInputBoundary {
    private final WageUserDataAccessInterface dataAccess;
    private final WagePlayerDataAccessInterface playerDataAccess;
    private final WageOutputBoundary presenter;
    private final Map<String, Employee> employees;
    private final int WAGE_CHANGE = 10;

    public WageInteractor(WageUserDataAccessInterface dataAccess,
                          WagePlayerDataAccessInterface playerDataAccess,
                          WageOutputBoundary presenter,
                          Map<String, Employee> employees) {
        this.dataAccess = dataAccess;
        this.playerDataAccess = playerDataAccess;
        this.presenter = presenter;
        this.employees = employees;
    }

    private boolean canAffordIncrease() {
        int prospectiveTotal = Employee.getTotalWage() + WAGE_CHANGE;
        double balance = playerDataAccess.getPlayer().getBalance();
        return prospectiveTotal <= balance;}

    @Override
    public void increaseWage (String position){
        Employee currentEmployee = employees.get(position);

        if (canAffordIncrease()) {
            currentEmployee.increaseWage();
            dataAccess.save(currentEmployee);

            presenter.prepareSuccessView(
                    new WageOutputData(currentEmployee,
                            Employee.getTotalWage(),
                            playerDataAccess.getPlayer().getBalance())
            );
        } else {
            // Do not change anything; warn and refresh UI
            presenter.prepareErrorView("exceed current balance");
            presenter.prepareSuccessView(
                    new WageOutputData(currentEmployee,Employee.getTotalWage(),
                            playerDataAccess.getPlayer().getBalance())
            );
        }
    }

    @Override
    public void decreaseWage (String position){
        Employee currentEmployee = employees.get(position);

        currentEmployee.decreaseWage();
        dataAccess.save(currentEmployee);

        presenter.prepareSuccessView(
                new WageOutputData(currentEmployee,
                        Employee.getTotalWage(),
                        playerDataAccess.getPlayer().getBalance())
        );
    }
    }
