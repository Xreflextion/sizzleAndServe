package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.office.OfficeViewModel;
import view.OfficeView;

import javax.swing.*;
import java.awt.*;

public class AppBuilder {
    public static final int INITIAL_BALANCE = 500;
    public static final int INITIAL_DAY = 0;
    public static final int INITIAL_PAST_CUSTOMER_COUNT = 0;

    private final JPanel cardPanel = new JPanel();
    final ViewManagerModel viewManagerModel = new ViewManagerModel();

    private OfficeViewModel officeViewModel;
    private OfficeView officeView;


    public AppBuilder() {

        cardPanel.setLayout(new CardLayout());
    }

    public AppBuilder addOfficeView() {
        officeViewModel = new OfficeViewModel();
        officeView = new OfficeView(officeViewModel);
        cardPanel.add(officeView, officeView.getViewName());
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);

        // Set initial state to office
        viewManagerModel.setState(officeView.getViewName());

        // Initial values
        officeViewModel.getState().setCurrentBalance(INITIAL_BALANCE);
        officeViewModel.getState().setCurrentDay(INITIAL_DAY);
        officeViewModel.getState().setPastCustomerCount(INITIAL_PAST_CUSTOMER_COUNT);

        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }
}
