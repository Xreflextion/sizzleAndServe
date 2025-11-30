package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import use_case.manage_wage.WageUserDataAccessInterface;
import entity.Employee;
import use_case.simulate.SimulateWageDataAccessInterface;

import java.util.HashMap;
import java.util.Map;

    public class WageDataAccessObject implements WageUserDataAccessInterface, SimulateWageDataAccessInterface {

        private final Map<String, Employee> employees;
        private FileHelperObject fileHelperObject;

        public WageDataAccessObject(Map<String, Employee> employees) {
            this.employees = employees;
        }

        public WageDataAccessObject(FileHelperObject fileHelperObject) {

            this.fileHelperObject = fileHelperObject;
            JsonArray employeeArray = fileHelperObject.getArrayFromSaveData(Constants.EMPLOYEE_KEY);
            if (employeeArray.size() == 2) {
                Map<String, Employee> savedEmployees = new HashMap<>();
                for (JsonElement element: employeeArray) {
                    JsonObject employeeJsonObject = element.getAsJsonObject();
                    String position = employeeJsonObject.get("position").getAsString();
                    int wage = employeeJsonObject.get("wage").getAsInt();
                    Employee employee = new Employee(wage, position);
                    savedEmployees.put(position, employee);
                }
                this.employees = savedEmployees;
            } else {
                // Default
                this.employees = new HashMap<>();
                employees.put("Cook", new Employee(1, "Cook"));
                employees.put("Waiter", new Employee(1, "Waiter"));
            }

        }

        @Override
        public Employee getEmployee(String position) {
            return employees.get(position);
        }

        @Override
        public void save(Employee employee) {
            employees.put(employee.getPosition(), employee);
            // TODO save
        }

        @Override
        public int getTotalWage() {
            return Employee.getTotalWage();
        }

        public Map<String, Employee> getEmployees() {
            return employees;
        }
    }


