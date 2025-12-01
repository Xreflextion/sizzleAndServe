package data_access;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import constants.Constants;
import entity.Employee;
import use_case.manage_wage.WageUserDataAccessInterface;
import use_case.simulate.SimulateWageDataAccessInterface;

public class WageDataAccessObject implements WageUserDataAccessInterface, SimulateWageDataAccessInterface {

    private final Map<String, Employee> employees;
    private FileHelperObject fileHelperObject;

    public WageDataAccessObject(Map<String, Employee> employees) {
        this.employees = employees;
    }

    public WageDataAccessObject(FileHelperObject fileHelperObject) {
        this.fileHelperObject = fileHelperObject;
        final JsonArray employeeArray = fileHelperObject.getArrayFromSaveData(Constants.EMPLOYEE_KEY);
        if (employeeArray.size() == 2) {
            final Map<String, Employee> savedEmployees = new HashMap<>();
            for (JsonElement element: employeeArray) {
                final JsonObject employeeJsonObject = element.getAsJsonObject();
                final String position = employeeJsonObject.get("position").getAsString();
                final int wage = employeeJsonObject.get("wage").getAsInt();
                final Employee employee = new Employee(wage, position);
                savedEmployees.put(position, employee);
            }
            this.employees = savedEmployees;
        }
        else {
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

    /**
     * Saves the given employee to the JSON file.
     *
     * @param employee the employee to be added or updated in the storage
     */
    @Override
    public void save(Employee employee) {
        employees.put(employee.getPosition(), employee);
        save();
    }

    /**
     * Saves the current employees' data to the JSON file.
     */
    public void save() {
        if (fileHelperObject != null) {
            try {
                saveToFile();
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public int getTotalWage() {
        return Employee.getTotalWage();
    }

    public Map<String, Employee> getEmployees() {
        return employees;
    }

    /**
     * Serializes the current employee data into a JSON array and saves it to a file.
     * @throws IOException if an error occurs while saving the JSON array to the file
     */
    public void saveToFile() throws IOException {
        final JsonArray wageArray = new JsonArray();
        for (Map.Entry<String, Employee> entry : employees.entrySet()) {
            final Employee employee = entry.getValue();
            final JsonObject obj = new JsonObject();
            obj.addProperty("position", employee.getPosition());
            obj.addProperty("wage", employee.getWage());
            wageArray.add(obj);
        }
        fileHelperObject.saveArray(constants.Constants.EMPLOYEE_KEY, wageArray);
    }
}
