package interface_adapter.office;

import interface_adapter.ViewModel;

public class OfficeViewModel extends ViewModel<OfficeState> {
    public static final String VIEW_NAME = "office";

    public static final int HEIGHT = 300;
    public static final int WIDTH = 700;
    public static final int BUTTON_PADDING = 10;

    public static final String TITLE_LABEL = "Office";
    public static final float TITLE_SIZE = 20f;

    public static final String EMPLOYEE_BUTTON_LABEL = "Employee Manager";
    public static final String INVENTORY_BUTTON_LABEL = "Inventory";
    public static final String PRICE_MANAGER_BUTTON_LABEL = "Price Manager";
    public static final String INSIGHTS_BUTTON_LABEL = "Insights";
    public static final String REVIEW_MANAGER_BUTTON_LABEL = "Rating Manager";
    public static final String SIMULATE_BUTTON_LABEL = "Simulate Next Day";

    public static final String CURRENT_DAY_LABEL = "Current Day: ";
    public static final String CURRENT_BALANCE_LABEL = "Current Balance: ";
    public static final String CURRENT_CUSTOMER_COUNT_LABEL = "Number of Customers Today: ";


    public OfficeViewModel() {
        super(VIEW_NAME);
        setState(new OfficeState());
    }
}
