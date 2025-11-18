package data_Access;

import use_case.WageUserDataAccessInterface;
import entity.Employee;

import java.util.Map;

    public class WageDataAccessObject implements WageUserDataAccessInterface {

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
    }


