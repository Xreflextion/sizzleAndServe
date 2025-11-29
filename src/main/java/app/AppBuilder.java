package app;

import data_access.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.insight.*;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.office.SimulateController;
import interface_adapter.office.SimulatePresenter;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesPresenter;
import interface_adapter.product_prices.ProductPricesViewModel;
import use_case.insights_day_calculation.DayInsightsInputBoundary;
import use_case.insights_day_calculation.DayInsightsInteractor;
import use_case.insights_day_calculation.DayInsightsOutputBoundary;
import use_case.insights_performance_calculation.PerformanceCalculationInputBoundary;
import use_case.insights_performance_calculation.PerformanceCalculationInteractor;
import use_case.insights_performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.product_prices.ProductPricesInteractor;
import use_case.product_prices.ProductPricesOutputBoundary;
import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInteractor;
import use_case.simulate.SimulateOutputBoundary;
import view.InsightsView;
import view.OfficeView;
import view.ProductPricesView;

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

    private ProductPricesView productPricesView;
    private ProductPricesViewModel productPricesViewModel;

    private InsightsViewModel insightsViewModel;
    private InsightsView insightsView;

    public AppBuilder() {
        cardPanel.setLayout(new CardLayout());
    }

    public AppBuilder addOfficeView() {
        officeViewModel = new OfficeViewModel();
        officeView = new OfficeView(officeViewModel);
        cardPanel.add(officeView, officeView.getViewName());
        return this;
    }

    public AppBuilder addProductPricesView() {
        productPricesViewModel = new ProductPricesViewModel();
        ProductPricesPresenter productPricesPresenter = new ProductPricesPresenter(productPricesViewModel,
                viewManagerModel);
        ProductPricesInteractor productPricesInteractor = new ProductPricesInteractor(new PantryDataAccessObject(),
                productPricesPresenter);
        ProductPricesController productPricesController = new ProductPricesController(productPricesInteractor);
        productPricesView = new ProductPricesView(productPricesViewModel, productPricesController);
        cardPanel.add(productPricesView, productPricesView.getViewName());
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

    public AppBuilder addInsightsView(){
        insightsViewModel = new InsightsViewModel();

        PerformanceCalculationOutputBoundary performancePresenter = new PerformanceCalculationPresenter (insightsViewModel, viewManagerModel);
        PerformanceCalculationInputBoundary performanceInteractor = new PerformanceCalculationInteractor(new DayRecordsDataAccessObject(), performancePresenter);
        PerformanceCalculationController performanceController = new PerformanceCalculationController(performanceInteractor);
        DayInsightsOutputBoundary dayInsightsPresenter = new DayInsightsPresenter(insightsViewModel, viewManagerModel);
        DayInsightsInputBoundary dayInsightsInteractor = new DayInsightsInteractor(new DayRecordsDataAccessObject(), dayInsightsPresenter);
        DayInsightsController dayInsightsController = new DayInsightsController(dayInsightsInteractor);

        insightsView = new InsightsView(insightsViewModel, performanceController,dayInsightsController);
        cardPanel.add(insightsView, InsightsView.viewName);
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
