package dataaccess;

import usecase.manage_wage.WageUserDataAccessInterface;
import entity.Employee;
import usecase.simulate.SimulateWageDataAccessInterface;

import java.util.Map;

    public class WageDataAccessObject implements WageUserDataAccessInterface, SimulateWageDataAccessInterface {

        private final Map<String, Employee> employees;

        public WageDataAccessObject(Map<String, Employee> employees) {
            this.employees = employees;
        }

        @Override
        public Employee getEmployee(String position) {
            return employees.get(position);
        }

        @Override
        public void save(Employee employee) {
            employees.put(employee.getPosition(), employee);
        }

        @Override
        public int getTotalWage() {
            return Employee.getTotalWage();
        }
    }


