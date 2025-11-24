package use_case.manage_wage;

import data_access.WageDataAccessObject;
import entity.Employee;
import entity.Player;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WagePresenter;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ManageWageTest {

    /** Fake Player DAO fixed at balance = 10. */
    private static class FakePlayerDAO implements WagePlayerDataAccessInterface {
        private Player player;
        FakePlayerDAO(int balance) { this.player = new Player("Tester", balance); }
        @Override public Player getPlayer() { return player; }
    }

    private WageController controller;
    private WageViewModel viewModel;
    private WageState state;
    private WageUserDataAccessInterface wageDAO;
    private WagePlayerDataAccessInterface playerDAO;
    private Map<String, Employee> employees;

    /** Reset the static Employee.totalWage without changing production code. */
    private static void resetTotalWageStatic() {
        try {
            // Try canonical name first
            java.lang.reflect.Field f = Employee.class.getDeclaredField("totalWage");
            f.setAccessible(true);
            f.setInt(null, 0);
        } catch (NoSuchFieldException e) {
            // In case your compiled class uses a different private name (e.g., "_totalWage")
            try {
                java.lang.reflect.Field f2 = Employee.class.getDeclaredField("_totalWage");
                f2.setAccessible(true);
                f2.setInt(null, 0);
            } catch (Exception ex) {
                throw new RuntimeException("Unable to reset Employee.totalWage via reflection", ex);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        // 1) Reset static total wage so each test starts clean
        resetTotalWageStatic();

        // 2) Create employees at MIN_WAGE = 1 (Cook=1, Waiter=1)
        employees = new HashMap<>();
        employees.put("Cook", new Employee(1, "Cook"));
        employees.put("Waiter", new Employee(1, "Waiter"));
        // totalWage is now 2

        // 3) Real Wage DAO over the map
        wageDAO = new WageDataAccessObject(employees);

        // 4) Fake Player DAO with balance = 10 (your requirement)
        playerDAO = new FakePlayerDAO(10);

        // 5) View stack
        viewModel = new WageViewModel();
        state = viewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());                 // 1
        state.setCookWageEffect(employees.get("Cook").getWageEffect());     // 1.0
        state.setWaiterWage(employees.get("Waiter").getWage());             // 1
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect()); // 1.0
        viewModel.setState(state);

        // 6) Presenter + Interactor + Controller
        WagePresenter presenter = new WagePresenter(viewModel);
        WageInteractor interactor = new WageInteractor(wageDAO, playerDAO, presenter, employees);
        controller = new WageController(interactor);
    }

    @Test
    void successIncrease() {
        // Given: total = 2, balance = 10 -> increases allowed while total < balance
        controller.cookIncrease();   // total: 3 (cook: 2)
        controller.waiterIncrease(); // total: 4 (waiter: 2)

        assertEquals(2, viewModel.getState().getCookWage(), "Cook wage should be 2 after one increase");
        assertEquals(2, viewModel.getState().getWaiterWage(), "Waiter wage should be 2 after one increase");
        assertEquals(4, viewModel.getState().getTotalWage(), "Total wage should be 4 after two increases");
    }

    @Test
    void successDecrease() {
        // Increase once then decrease back
        controller.cookIncrease();    // total: 3 (cook: 2)
        controller.cookDecrease();    // total: 2 (cook: 1)

        assertEquals(1, viewModel.getState().getCookWage(), "Cook wage should return to 1 after decrease");
        assertEquals(2, viewModel.getState().getTotalWage(), "Total wage should return to 2 after decrease");
    }

    @Test
    void failIncrease() {
        // Push total to exactly 10 (allowed while total < 10; last step from 9->10 is allowed)
        for (int i = 0; i < 8; i++) {
            controller.waiterIncrease(); // total: 3..10
        }
        assertEquals(10, viewModel.getState().getTotalWage(), "Total wage should be 10 after allowed increases");

        // At total == balance, checkCurrentBalance() => 10 < 10 is false => block further increases
        controller.cookIncrease(); // should be blocked

        assertEquals(10, viewModel.getState().getTotalWage(), "Total wage should remain 10 when increase is blocked");
        assertEquals(1, viewModel.getState().getCookWage(), "Cook wage should remain 1 when blocked at balance");
    }

    @Test
    void failDecrease() {
        // Both employees at MIN_WAGE=1; decreasing should do nothing
        controller.cookDecrease();
        controller.waiterDecrease();

        assertEquals(1, viewModel.getState().getCookWage(), "Cook wage should not go below MIN_WAGE=1");
        assertEquals(1, viewModel.getState().getWaiterWage(), "Waiter wage should not go below MIN_WAGE=1");
        assertEquals(2, viewModel.getState().getTotalWage(), "Total wage should remain 2 at minimum wages");
    }
}