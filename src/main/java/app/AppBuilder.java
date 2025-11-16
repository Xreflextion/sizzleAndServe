package app;

import data_access.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.office.SimulateController;
import interface_adapter.office.SimulatePresenter;
import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInteractor;
import use_case.simulate.SimulateOutputBoundary;
import view.OfficeView;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

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

    public AppBuilder addSimulateUseCase() {
        final SimulateOutputBoundary simulateOutputBoundary = new SimulatePresenter(viewManagerModel,
                officeViewModel);

        final SimulateInputBoundary simulateInteractor = new SimulateInteractor(simulateOutputBoundary);

        SimulateController controller = new SimulateController(simulateInteractor);
        officeView.setSimulationController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        application.setResizable(false);

        viewManagerModel.setState(officeView.getViewName());
        officeViewModel.getState().setCurrentBalance(INITIAL_BALANCE);
        officeViewModel.getState().setCurrentDay(INITIAL_DAY);
        officeViewModel.getState().setPastCustomerCount(INITIAL_PAST_CUSTOMER_COUNT);
        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }
}
