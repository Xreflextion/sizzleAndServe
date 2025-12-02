package app;

import java.awt.CardLayout;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import constants.Constants;
import data_access.DayRecordsDataAccessObject;
import data_access.FileHelperObject;
import data_access.PantryDataAccessObject;
import data_access.PlayerDataAccessObject;
import data_access.ReviewDataAccessObject;
import data_access.WageDataAccessObject;
import entity.Employee;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.buy_serving.BuyServingController;
import interface_adapter.buy_serving.BuyServingPresenter;
import interface_adapter.buy_serving.BuyServingViewModel;
import interface_adapter.insight.DayInsightsController;
import interface_adapter.insight.DayInsightsPresenter;
import interface_adapter.insight.InsightsViewModel;
import interface_adapter.insight.PerformanceCalculationController;
import interface_adapter.insight.PerformanceCalculationPresenter;
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
import interface_adapter.review.ReviewController;
import interface_adapter.review.ReviewPresenter;
import interface_adapter.review.ReviewViewModel;
import use_case.buy_serving.BuyServingInteractor;
import use_case.insights.day_calculation.DayInsightsInputBoundary;
import use_case.insights.day_calculation.DayInsightsInteractor;
import use_case.insights.day_calculation.DayInsightsOutputBoundary;
import use_case.insights.performance_calculation.PerformanceCalculationInputBoundary;
import use_case.insights.performance_calculation.PerformanceCalculationInteractor;
import use_case.insights.performance_calculation.PerformanceCalculationOutputBoundary;
import use_case.manage_wage.WageInteractor;
import use_case.product_prices.ProductPricesInteractor;
import use_case.review.ReviewInteractor;
import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInteractor;
import use_case.simulate.SimulateOutputBoundary;
import view.BuyServingView;
import view.DrillDownView;
import view.InsightsView;
import view.ManageWagesView;
import view.OfficeView;
import view.ProductPricesView;
import view.ReviewView;
import view.ViewManager;

public class AppBuilder {
    public static final int INITIAL_BALANCE = 500;

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

    private PlayerDataAccessObject playerDataAccessObject;
    private PantryDataAccessObject pantryDataAccessObject;
    private ReviewDAOHash reviewDataAccessObjectHash;
    private DayRecordsDataAccessObject dayRecordsDataAccessObject;
    private WageDataAccessObject wageDataAccessObject;

    private ManageWagesView wageView;
    private WageViewModel wageViewModel;

    private FileHelperObject fileHelperObject;
    private int customerCount;

    private InsightsViewModel insightsViewModel;
    private InsightsView insightsView;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        fileHelperObject = new FileHelperObject(Constants.FILE_NAME);
        fileHelperObject.loadFromFile();

        playerDataAccessObject = new PlayerDataAccessObject(INITIAL_BALANCE, fileHelperObject);
        pantryDataAccessObject = new PantryDataAccessObject(fileHelperObject);
        reviewDataAccessObjectHash = new ReviewDAOHash(fileHelperObject);
        dayRecordsDataAccessObject = new DayRecordsDataAccessObject(fileHelperObject);
        wageDataAccessObject = new WageDataAccessObject(fileHelperObject);
        customerCount = reviewDataAccessObjectHash.getReviewsByDay(dayRecordsDataAccessObject.getNumberOfDays()).size();
    }

    /**
     * Adds the OfficeView to the application.
     * @return AppBuilder with the OfficeView added.
     */
    public AppBuilder addOfficeView() {
        officeViewModel = new OfficeViewModel();
        officeView = new OfficeView(officeViewModel, viewManagerModel);
        cardPanel.add(officeView, officeView.getViewName());
        return this;
    }

    /**
     * Adds the ProductPricesView to the application.
     * @return AppBuilder with the ProductPricesView added.
     */
    public AppBuilder addProductPricesView() {
        final Map<String, Recipe> recipes = pantryDataAccessObject.getPantry().getPantry();
        productPricesViewModel = new ProductPricesViewModel(recipes);
        final ProductPricesPresenter productPricesPresenter = new ProductPricesPresenter(productPricesViewModel,
                viewManagerModel);
        final ProductPricesInteractor productPricesInteractor = new ProductPricesInteractor(pantryDataAccessObject,
                productPricesPresenter);
        final ProductPricesController productPricesController = new ProductPricesController(productPricesInteractor);
        productPricesView = new ProductPricesView(productPricesViewModel, productPricesController, viewManagerModel);
        cardPanel.add(productPricesView, productPricesView.getViewName());
        return this;
    }

    /**
     * Adds the BuyServingView to the application.
     * @return AppBuilder with the BuyServingView added.
     */
    public AppBuilder addBuyServingViewAndUseCase() {

        buyServingViewModel = new BuyServingViewModel();
        buyServingViewModel.setNewBalance(playerDataAccessObject.getPlayer().getBalance());

        final String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();
        final int[] dishCosts = new int[dishNames.length];
        final int[] dishStocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            dishCosts[i] = pantryDataAccessObject.getPantry().getRecipe(dishNames[i]).getBasePrice();
            dishStocks[i] = pantryDataAccessObject.getPantry().getRecipe(dishNames[i]).getStock();
        }
        buyServingViewModel.setDishNames(dishNames);
        buyServingViewModel.setDishCosts(dishCosts);
        buyServingViewModel.setDishStocks(dishStocks);

        final BuyServingPresenter presenter = new BuyServingPresenter(buyServingViewModel,
                officeViewModel, wageViewModel);
        final BuyServingInteractor interactor = new BuyServingInteractor(playerDataAccessObject,
                pantryDataAccessObject, presenter);
        final BuyServingController controller = new BuyServingController(interactor);

        buyServingView = new BuyServingView(controller, buyServingViewModel, viewManagerModel);
        cardPanel.add(buyServingView, BuyServingViewModel.VIEW_NAME);

        return this;
    }

    /**
     * Adds the ReviewView to the application.
     * @return AppBuilder with the ReviewView added.
     */
    public AppBuilder addReviewViewAndUseCase() {

        // Creates ViewModel
        final ReviewViewModel reviewViewModel = new ReviewViewModel();

        // Creates a presenter
        final ReviewPresenter reviewPresenter = new ReviewPresenter(reviewViewModel);

        // Creates a review interactor
        final ReviewInteractor reviewInteractor = new ReviewInteractor(reviewDataAccessObjectHash, reviewPresenter);

        // Creates a review controller
        final ReviewController reviewController = new ReviewController(reviewInteractor);

        // Initializes View and add it to card panel
        final ReviewView reviewView = new ReviewView(reviewController, reviewViewModel, viewManagerModel);
        cardPanel.add(reviewView, ReviewViewModel.VIEW_NAME);

        return this;
    }

    /**
     * Add the ManageWagesView to the app builder.
     * @return AppBuilder with the ManageWagesView added.
     */
    public AppBuilder addManageWageViewAndUseCase() {
        final Map<String, Employee> employees = wageDataAccessObject.getEmployees();

        // 1) ViewModel + seed initial state so labels are correct immediately
        wageViewModel = new WageViewModel();
        final WageState state = wageViewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());
        state.setCookWageEffect(employees.get("Cook").getWageEffect());
        state.setWaiterWage(employees.get("Waiter").getWage());
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect());
        state.setCurrentBalance(playerDataAccessObject.getPlayer().getBalance());
        wageViewModel.setState(state);
        // fires property change

        // 2) Presenter + Controller
        final WagePresenter presenter = new WagePresenter(wageViewModel);
        final WageController controller =
                new WageController(new WageInteractor(wageDataAccessObject,
                        playerDataAccessObject, presenter, employees));

        // 3) Build the view and inject the controller
        wageView = new ManageWagesView(wageViewModel, viewManagerModel);
        wageView.initializePanels();
        wageView.setPanels();
        wageView.setActionListener();
        wageView.setController(controller);
        // 4) Add view to the cardPanel
        cardPanel.add(wageView, wageViewModel.getViewName());
        return this;
    }

    /**
     * Add Simulate Use Case to the app.
     * @return AppBuilder with Simulate Use Case added.
     */
    public AppBuilder addSimulateUseCase() {
        final SimulateOutputBoundary simulateOutputBoundary = new SimulatePresenter(
                viewManagerModel,
                officeViewModel,
                buyServingViewModel,
                wageViewModel
        );

        final SimulateInputBoundary simulateInteractor = new SimulateInteractor(
                simulateOutputBoundary,
                pantryDataAccessObject,
                reviewDataAccessObjectHash,
                wageDataAccessObject,
                playerDataAccessObject,
                dayRecordsDataAccessObject
        );

        final SimulateController controller = new SimulateController(simulateInteractor);
        officeView.setSimulationController(controller);
        return this;
    }

    /**
     * Add InsightsView to the app.
     * @return AppBuilder with InsightsView added.
     */
    public AppBuilder addInsightsViewAndUseCase() {
        insightsViewModel = new InsightsViewModel();
        final PerformanceCalculationOutputBoundary performancePresenter = new PerformanceCalculationPresenter(
                insightsViewModel, viewManagerModel);
        final PerformanceCalculationInputBoundary performanceInteractor = new PerformanceCalculationInteractor(
                dayRecordsDataAccessObject, performancePresenter);
        final PerformanceCalculationController performanceController = new PerformanceCalculationController(
                performanceInteractor);

        if (officeView != null) {
            officeView.setPerformanceController(performanceController);
        }
        final DayInsightsOutputBoundary dayInsightsPresenter =
                new DayInsightsPresenter(insightsViewModel, viewManagerModel);
        final DayInsightsInputBoundary dayInsightsInteractor =
                new DayInsightsInteractor(dayRecordsDataAccessObject, dayInsightsPresenter);
        final DayInsightsController dayInsightsController =
                new DayInsightsController(dayInsightsInteractor);

        insightsView = new InsightsView(
                insightsViewModel,
                performanceController,
                dayInsightsController,
                viewManagerModel
        );
        cardPanel.add(insightsView, insightsViewModel.getViewName());
        final DrillDownView drillDownView = new DrillDownView(insightsViewModel, viewManagerModel);
        cardPanel.add(drillDownView, DrillDownView.VIEW_NAME);
        return this;

    }

    /**
     * Initiating the View that user will see.
     * @return JFrame with Office View.
     */
    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        application.setResizable(false);

        viewManagerModel.setState(officeView.getViewName());
        officeViewModel.getState().setCurrentBalance(playerDataAccessObject.getPlayer().getBalance());
        officeViewModel.getState().setCurrentDay(dayRecordsDataAccessObject.getNumberOfDays());
        officeViewModel.getState().setCurrentCustomerCount(customerCount);
        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }

}
