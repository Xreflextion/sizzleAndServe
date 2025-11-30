package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import java.io.IOException;
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

        public void saveToFile() throws IOException {
            JsonArray array = new JsonArray();
            for (Map.Entry<String, Employee> entry : employees.entrySet()) {
                Employee employee = entry.getValue();
                JsonObject obj = new JsonObject();
                obj.addProperty("position", employee.getPosition());
                obj.addProperty("wage", employee.getWage());
                array.add(obj);
            }
            fileHelperObject.saveArray(constants.Constants.EMPLOYEE_KEY, array);
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


