package app;

import data_access.PlayerDataAccessObject;
import entity.Employee;
import entity.Player;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WagePresenter;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import use_case.manage_wage.WageInteractor;
import use_case.manage_wage.WagePlayerDataAccessInterface;
import use_case.manage_wage.WageUserDataAccessInterface;
import view.ManageWagesView;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ManageWageAppBuilder {
    public static void main(String[] args) {
        final JPanel cardPanel = new JPanel();
        // 1) Initialize the playerDAO
        WagePlayerDataAccessInterface playerDataAccess= new PlayerDataAccessObject();
        // 2) Initialize the two employees with wage=0, effect=1 on every run
        Map<String, Employee> employees = new HashMap<>() {{
            put("Cook", new Employee(1, "Cook"));
            put("Waiter", new Employee(1, "Waiter"));
        }};
        // 3) ViewModel + seed initial state so labels are correct immediately
        WageViewModel wageViewModel = new WageViewModel();
        WageState state = wageViewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());                // 0
        state.setCookWageEffect(employees.get("Cook").getWageEffect());    // 1.0
        state.setWaiterWage(employees.get("Waiter").getWage());            // 0
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect());// 1.0
        state.setCurrentBalance(playerDataAccess.getPlayer().getBalance());
        wageViewModel.setState(state); // fires property change

        // 4) WageDataAccess + Presenter + Controller
        WageUserDataAccessInterface dataAccess = new data_access.WageDataAccessObject(employees);

        WagePresenter presenter = new WagePresenter(wageViewModel);
        WageController controller =
                new WageController(new WageInteractor(dataAccess, playerDataAccess, presenter, employees));

        // 5) Build the view and inject the controller
        ManageWagesView wageView = new ManageWagesView(wageViewModel);
        wageView.setController(controller);

        JFrame application = new JFrame("Sizzle and Serve");
        application.setSize(400, 300);
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        application.setContentPane(wageView);
        application.setVisible(true);
    }
}

