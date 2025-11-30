package usecase.manage_wage;

import dataaccess.WageDataAccessObject;
import entity.Employee;
import entity.Player;
import interfaceadapter.manage_wages.WageController;
import interfaceadapter.manage_wages.WagePresenter;
import interfaceadapter.manage_wages.WageState;
import interfaceadapter.manage_wages.WageViewModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ManageWageTest {

    /** Fake Player DAO fixed at balance = 500. */
    private static class FakePlayerDAO implements WagePlayerDataAccessInterface {
        private Player player;
        FakePlayerDAO(int balance) { this.player = new Player("Tester", balance); }
        @Override public Player getPlayer() { return player; }
    }

    private WageController controller;
    private WageInteractor interactor;
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

        // 2) Create employees
        employees = new HashMap<>();
        employees.put("Cook", new Employee(230, "Cook"));
        employees.put("Waiter", new Employee(230, "Waiter"));
        // totalWage is now 2

        // 3) Real Wage DAO over the map
        wageDAO = new WageDataAccessObject(employees);

        // 4) Fake Player DAO with balance = 500 (your requirement)
        playerDAO = new FakePlayerDAO(500);

        // 5) View stack
        viewModel = new WageViewModel();
        state = viewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());                 // 230
        state.setCookWageEffect(employees.get("Cook").getWageEffect());     // 1.0
        state.setWaiterWage(employees.get("Waiter").getWage());             // 230
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect()); // 1.0
        viewModel.setState(state);

        // 6) Presenter + Interactor + Controller
        WagePresenter presenter = new WagePresenter(viewModel);
        interactor = new WageInteractor(wageDAO, playerDAO, presenter, employees);
        controller = new WageController(interactor);
    }

    @Test
    void successIncrease() {
        // Given: total = 460, balance = 500 -> increases allowed while total < balance
        controller.cookIncrease();   // total: 470 (cook: 240)
        controller.waiterIncrease(); // total: 480 (waiter: 240)

        assertEquals(240, viewModel.getState().getCookWage(), "Cook wage should be 250 after one increase");
        assertEquals(240, viewModel.getState().getWaiterWage(), "Waiter wage should be 250 after one increase");
        assertEquals(480, viewModel.getState().getTotalWage(), "Total wage should be 500 after two increases");
        assertEquals((float)37.2, viewModel.getState().getCookWageEffect(),"Cook's wage effect increased to 37.2");
        assertNull(viewModel.getState().getWarningMessage(), "No warning should occur on successful increase");
    }

    @Test
    void successDecrease() {
        // Increase once then decrease back
        controller.cookIncrease();    // total: 470 (cook: 240)
        controller.cookDecrease();    // total: 460 (cook: 230)

        assertEquals(230, viewModel.getState().getCookWage(), "Cook wage should return to 230 after decrease");
        assertEquals(460, viewModel.getState().getTotalWage(), "Total wage should return to 460 after decrease");
        assertNull(viewModel.getState().getWarningMessage(), "No warning should occur on successful increase");
    }

    @Test
    void failIncrease() {
        for (int i = 0; i < 5; i++) {
        controller.waiterIncrease();}// total: 500 (waiter: 270)*should not result in more increase to exceed balance
            controller.cookIncrease(); // total: 500 (cook 230) *should not result in increase
        assertEquals(270, viewModel.getState().getWaiterWage(), "Waiter wage should not increase anymore");
        assertEquals(500, viewModel.getState().getTotalWage(), "Total wage should be 500 after unallowed increases");
        assertEquals(230, viewModel.getState().getCookWage(), "Cook wage should not increase anymore");
    }

    @Test
    void failDecrease() {
        for (int i = 0; i< 18; i++) {
            controller.cookDecrease();} //total: 280 (Cook:50)
        controller.cookDecrease(); //total: 280 (Cook:0) * no decrease expected

        assertEquals(50, viewModel.getState().getCookWage(), "Cook wage should not go below MIN_WAGE=50");
        assertEquals(280, viewModel.getState().getTotalWage(), "Total wage should remain 280 at minimum wages");
    }

}