package interface_adapter.manage_wages;

import interface_adapter.ViewModel;

public class WageViewModel extends ViewModel<WageState> {

    public static final String TITLE = "Manage Employees' Wages";
    public static final String WAITER = "Waiter";
    public static final String WAGE = "Wage";
    public static final String COOK = "Cook";
    public static final String EFFECT = "Wage Effect";
    public static final String WAITER_WAGE_EFFECT = "Customer Rating alters by 20%";
    public static final String COOK_WAGE_EFFECT = "Number of Customers alters by 20%";

    public WageViewModel() {
        super("Manage Wage");
        setState(new WageState());
    }

    @Override
    public void setState(WageState state) {
        super.setState(state);
    }
}

