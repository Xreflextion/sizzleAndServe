package interface_adapter.insight;

import interface_adapter.ViewModel;

public class InsightsViewModel extends ViewModel<InsightsState>{

    public static final String VIEW_NAME = "Insights";
    public InsightsViewModel() {
        super(VIEW_NAME);
        setState(new InsightsState());
    }

    @Override
    public void setState(InsightsState state) {
        super.setState(state);
        firePropertyChange();
    }

}
