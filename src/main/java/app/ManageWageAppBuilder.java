package app;

import entity.Employee;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WagePresenter;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import use_case.manage_wage.WageInteractor;
import use_case.manage_wage.WageUserDataAccessInterface;
import view.ManageWagesView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ManageWageAppBuilder {
    public static void main(String[] args) {
        final JPanel cardPanel = new JPanel();
// 1) Initialize the two employees with wage=0, effect=1 on every run
        Map<String, Employee> employees = new HashMap<>();
        employees.put("Cook", new Employee(0, "Cook"));
        employees.put("Waiter", new Employee(0, "Waiter"));

        // 2) ViewModel + seed initial state so labels are correct immediately
        WageViewModel wageViewModel = new WageViewModel();
        WageState state = wageViewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());                // 0
        state.setCookWageEffect(employees.get("Cook").getWageEffect());    // 1.0
        state.setWaiterWage(employees.get("Waiter").getWage());            // 0
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect());// 1.0
        wageViewModel.setState(state); // fires property change

        // 3) DataAccess + Presenter + Controller
        WageUserDataAccessInterface dataAccess = new data_access.WageDataAccessObject(employees);
        WagePresenter presenter = new WagePresenter(wageViewModel);
        WageController controller = new WageController(new WageInteractor(dataAccess, presenter, employees));

        // 4) Build the view and inject the controller
        ManageWagesView wageView = new ManageWagesView(wageViewModel);
        wageView.setController(controller);

        JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        application.setContentPane(wageView);
        application.setVisible(true);
    }
}

