package interface_adapter.office;

import interface_adapter.ViewModel;

public class OfficeViewModel extends ViewModel<OfficeState> {
    public static final String VIEW_NAME = "office";
    public static final String TITLE_LABEL = "Office";
    public static final String SIMULATE_LABEL = "Simulate Next Day";
    public static final String CURRENT_DAY_LABEL = "Current Day: ";
    public static final String CURRENT_BALANCE_LABEL = "Current Balance: ";
    public static final String PAST_CUSTOMER_COUNT_LABEL = "Number of Customers Yesterday: ";


    public OfficeViewModel() {
        super(VIEW_NAME);
        setState(new OfficeState());
    }
}
