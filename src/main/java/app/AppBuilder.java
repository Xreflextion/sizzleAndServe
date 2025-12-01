package app;

import data_access.*;
import entity.Employee;
import interface_adapter.ViewManagerModel;
import entity.Recipe;
import interface_adapter.insight.*;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WagePresenter;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.office.SimulateController;
import interface_adapter.office.SimulatePresenter;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesPresenter;
import interface_adapter.product_prices.ProductPricesViewModel;
import interface_adapter.buy_serving.BuyServingController;
import interface_adapter.buy_serving.BuyServingPresenter;
import interface_adapter.buy_serving.BuyServingViewModel;
import use_case.buy_serving.BuyServingInteractor;
import interface_adapter.review.ReviewController;
import interface_adapter.review.ReviewPresenter;
import interface_adapter.review.ReviewViewModel;
import use_case.insights_day_calculation.DayInsightsInputBoundary;
import use_case.insights_day_calculation.DayInsightsInteractor;
import use_case.insights_day_calculation.DayInsightsOutputBoundary;
import use_case.insights_performance_calculation.PerformanceCalculationInputBoundary;
import use_case.insights_performance_calculation.PerformanceCalculationInteractor;
import use_case.insights_performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.manage_wage.WageInteractor;
import use_case.manage_wage.WageUserDataAccessInterface;
import use_case.product_prices.ProductPricesInteractor;
import use_case.review.ReviewInteractor;
import use_case.simulate.*;
import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInteractor;
import use_case.simulate.SimulateOutputBoundary;
import view.*;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AppBuilder {
    public static final int INITIAL_BALANCE = 500;
    public static final int INITIAL_DAY = 0;
    public static final int INITIAL_PAST_CUSTOMER_COUNT = 0;

    private final JPanel cardPanel = new JPanel();
    private final CardLayout cardLayout = new CardLayout();
    private final ViewManagerModel viewManagerModel = new ViewManagerModel();
    private ViewManager viewManager = new ViewManager(cardPanel, cardLayout, viewManagerModel);

    private OfficeViewModel officeViewModel;
    private OfficeView officeView;

    private ProductPricesView productPricesView;
    private ProductPricesViewModel productPricesViewModel;

    private BuyServingViewModel buyServingViewModel;
    private BuyServingView buyServingView;

    private PlayerDataAccessObject playerDAO = new PlayerDataAccessObject(INITIAL_BALANCE);
    private PantryDataAccessObject pantryDAO = new PantryDataAccessObject();
    private ReviewDAOHash reviewDAO;
    private DayRecordsDataAccessObject dayRecordsDataAccessObject;

    private ManageWagesView wageView;
    private WageDataAccessObject wageDAO;
    private WageViewModel wageViewModel;
    private Map<String, Employee> employees = new HashMap<>();

    private InsightsViewModel insightsViewModel;
    private InsightsView insightsView;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // Initialize data access objects
        dayRecordsDataAccessObject = new DayRecordsDataAccessObject();
    }

    public AppBuilder addOfficeView() {
        officeViewModel = new OfficeViewModel();
        officeView = new OfficeView(officeViewModel, viewManagerModel);
        cardPanel.add(officeView, officeView.getViewName());
        return this;
    }

    public AppBuilder addProductPricesView() {
        Map<String, Recipe> recipes = pantryDAO.getPantry().getPantry();
        productPricesViewModel = new ProductPricesViewModel(recipes);
        ProductPricesPresenter productPricesPresenter = new ProductPricesPresenter(productPricesViewModel,
                viewManagerModel);
        ProductPricesInteractor productPricesInteractor = new ProductPricesInteractor(pantryDAO,
                productPricesPresenter);
        ProductPricesController productPricesController = new ProductPricesController(productPricesInteractor);
        productPricesView = new ProductPricesView(productPricesViewModel, productPricesController, viewManagerModel);
        cardPanel.add(productPricesView, productPricesView.getViewName());
        return this;
    }

    public AppBuilder addBuyServingViewAndUseCase() {

        buyServingViewModel = new BuyServingViewModel();
        buyServingViewModel.setNewBalance(playerDAO.getPlayer().getBalance());

        String[] dishNames = pantryDAO.getPantry().getDishNames();
        int[] dishCosts = new int[dishNames.length];
        int[] dishStocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            dishCosts[i] = pantryDAO.getPantry().getRecipe(dishNames[i]).getBasePrice();
            dishStocks[i] = pantryDAO.getPantry().getRecipe(dishNames[i]).getStock();
        }
        buyServingViewModel.setDishNames(dishNames);
        buyServingViewModel.setDishCosts(dishCosts);
        buyServingViewModel.setDishStocks(dishStocks);

        BuyServingPresenter presenter = new BuyServingPresenter(buyServingViewModel, officeViewModel, wageViewModel);
        BuyServingInteractor interactor = new BuyServingInteractor(playerDAO, pantryDAO, presenter);
        BuyServingController controller = new BuyServingController(interactor);

        buyServingView = new BuyServingView(controller, buyServingViewModel, viewManagerModel);
        cardPanel.add(buyServingView, BuyServingViewModel.VIEW_NAME);

        return this;
    }

    // Adds the ReviewView to the app builder
    public AppBuilder addReviewViewAndUseCase() {

        // Creates ViewModel
        ReviewViewModel reviewViewModel = new ReviewViewModel();

        // Creates a presenter
        ReviewPresenter reviewPresenter = new ReviewPresenter(reviewViewModel);

        // Creates a reviewDAO
        this.reviewDAO = new ReviewDAOHash(new HashMap<>());

        // Creates a review interactor
        ReviewInteractor reviewInteractor = new ReviewInteractor(reviewDAO, reviewPresenter);

        // Creates a review controller
        ReviewController reviewController = new ReviewController(reviewInteractor);

        // Initializes View and add it to card panel
        ReviewView reviewView = new ReviewView(reviewController, reviewViewModel, viewManagerModel);
        cardPanel.add(reviewView, ReviewViewModel.VIEW_NAME);

        return this;
    }

    // Adds the ManageWageView to the app builder
    public AppBuilder addManageWageViewAndUseCase() {

        // 1) Initialize the two employees with wage=1, effect=1 on every run
        employees.put("Cook", new Employee(1, "Cook"));
        employees.put("Waiter", new Employee(1, "Waiter"));

        // 2) ViewModel + seed initial state so labels are correct immediately
        wageViewModel = new WageViewModel();
        WageState state = wageViewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());                // 0
        state.setCookWageEffect(employees.get("Cook").getWageEffect());    // 1.0
        state.setWaiterWage(employees.get("Waiter").getWage());            // 0
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect());// 1.0
        state.setCurrentBalance(playerDAO.getPlayer().getBalance());
        wageViewModel.setState(state); // fires property change

        // 3) WageDataAccess + Presenter + Controller
        wageDAO = new WageDataAccessObject(employees);
        WagePresenter presenter = new WagePresenter(wageViewModel);
        WageController controller =
                new WageController(new WageInteractor(wageDAO, playerDAO, presenter, employees));

        // 4) Build the view and inject the controller
        wageView = new ManageWagesView(wageViewModel, viewManagerModel);
        wageView.setController(controller);
        // 5) Add view to the cardPanel
        cardPanel.add(wageView, wageViewModel.getViewName());
        return this;
    }


    public AppBuilder addSimulateUseCase() {
        final SimulateOutputBoundary simulateOutputBoundary = new SimulatePresenter(viewManagerModel,
                officeViewModel, buyServingViewModel, wageViewModel);

        final SimulateInputBoundary simulateInteractor = new SimulateInteractor(
                simulateOutputBoundary,
                pantryDAO,
                reviewDAO,
                wageDAO,
                playerDAO,
                dayRecordsDataAccessObject
        );

        SimulateController controller = new SimulateController(simulateInteractor);
        officeView.setSimulationController(controller);
        return this;
    }

    public AppBuilder addInsightsViewAndUseCase() {
        insightsViewModel = new InsightsViewModel();


        PerformanceCalculationOutputBoundary performancePresenter = new PerformanceCalculationPresenter(
                insightsViewModel, viewManagerModel);
        PerformanceCalculationInputBoundary performanceInteractor = new PerformanceCalculationInteractor(
                dayRecordsDataAccessObject, performancePresenter);
        PerformanceCalculationController performanceController = new PerformanceCalculationController(
                performanceInteractor);

        if (officeView != null){
            officeView.setPerformanceCalculationController(performanceController);
        }
        DayInsightsOutputBoundary dayInsightsPresenter = new DayInsightsPresenter(insightsViewModel, viewManagerModel);
        DayInsightsInputBoundary dayInsightsInteractor = new DayInsightsInteractor(dayRecordsDataAccessObject, dayInsightsPresenter);
        DayInsightsController dayInsightsController = new DayInsightsController(dayInsightsInteractor);

        insightsView = new InsightsView(
                insightsViewModel,
                performanceController,
                dayInsightsController,
                viewManagerModel
        );
        cardPanel.add(insightsView, insightsViewModel.getViewName());
        DrillDownView drillDownView = new DrillDownView(insightsViewModel, viewManagerModel);
        cardPanel.add(drillDownView, DrillDownView.VIEW_NAME);
        return this;

    }


    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        application.setResizable(false);

        viewManagerModel.setState(officeView.getViewName());
        officeViewModel.getState().setCurrentBalance(playerDAO.getPlayer().getBalance());
        officeViewModel.getState().setCurrentDay(INITIAL_DAY);
        officeViewModel.getState().setCurrentCustomerCount(INITIAL_PAST_CUSTOMER_COUNT);
        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }
}
