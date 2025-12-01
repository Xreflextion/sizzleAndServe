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
import data_access.ReviewDAOHash;
import data_access.WageDataAccessObject;
import entity.Employee;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.buy_serving.BuyServingController;
import interface_adapter.buy_serving.BuyServingPresenter;
import interface_adapter.buy_serving.BuyServingViewModel;
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
import use_case.manage_wage.WageInteractor;
import use_case.product_prices.ProductPricesInteractor;
import use_case.review.ReviewInteractor;
import use_case.simulate.SimulateInputBoundary;
import use_case.simulate.SimulateInteractor;
import use_case.simulate.SimulateOutputBoundary;
import view.BuyServingView;
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

    private PlayerDataAccessObject playerDAO;
    private PantryDataAccessObject pantryDAO;
    private ReviewDAOHash reviewDAO;
    private DayRecordsDataAccessObject dayRecordsDAO;
    private WageDataAccessObject wageDataAccessObject;

    private ManageWagesView wageView;
    private WageViewModel wageViewModel;

    private FileHelperObject fileHelperObject;
    private int customerCount = 0;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        fileHelperObject = new FileHelperObject(Constants.FILE_NAME);
        fileHelperObject.loadFromFile();

        playerDAO = new PlayerDataAccessObject(INITIAL_BALANCE, fileHelperObject);
        pantryDAO = new PantryDataAccessObject(fileHelperObject);
        reviewDAO = new ReviewDAOHash(fileHelperObject);
        dayRecordsDAO = new DayRecordsDataAccessObject(fileHelperObject);
        wageDataAccessObject = new WageDataAccessObject(fileHelperObject);
        customerCount = reviewDAO.getReviewsByDay(dayRecordsDAO.getNumberOfDays()).size();
    }

    public AppBuilder addOfficeView() {
        officeViewModel = new OfficeViewModel();
        officeView = new OfficeView(officeViewModel, viewManagerModel);
        officeView.addSaveAllDataListener(() -> this.saveAllData());
        cardPanel.add(officeView, officeView.getViewName());
        return this;
    }

    /**
     * Adds the Product Prices View to the application.
     * @return AppBuilder with the Product Prices View added
     */
    public AppBuilder addProductPricesView() {
        final Map<String, Recipe> recipes = pantryDAO.getPantry().getPantry();
        productPricesViewModel = new ProductPricesViewModel(recipes);
        final ProductPricesPresenter productPricesPresenter = new ProductPricesPresenter(productPricesViewModel,
                viewManagerModel);
        final ProductPricesInteractor productPricesInteractor = new ProductPricesInteractor(pantryDAO,
                productPricesPresenter);
        final ProductPricesController productPricesController = new ProductPricesController(productPricesInteractor);
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

        // Creates a review interactor
        ReviewInteractor  reviewInteractor = new ReviewInteractor(reviewDAO, reviewPresenter);

        // Creates a review controller
        ReviewController  reviewController = new ReviewController(reviewInteractor);

        // Initializes View and add it to card panel
        ReviewView reviewView = new ReviewView(reviewController, reviewViewModel, viewManagerModel);
        cardPanel.add(reviewView, ReviewViewModel.VIEW_NAME);

        return this;
    }

    /**
     * Add the ManageWageView to the app builder.
     * @return Appbuilder of Wage Manager.
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
        state.setCurrentBalance(playerDAO.getPlayer().getBalance());
        wageViewModel.setState(state);
        // fires property change

        // 2) Presenter + Controller
        final WagePresenter presenter = new WagePresenter(wageViewModel);
        final WageController controller =
                new WageController(new WageInteractor(wageDataAccessObject, playerDAO, presenter, employees));

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
     * @return AppBuilder with Simulate Use Case added
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
                pantryDAO,
                reviewDAO,
                wageDataAccessObject,
                playerDAO,
                dayRecordsDAO
        );

        final SimulateController controller = new SimulateController(simulateInteractor);
        officeView.setSimulationController(controller);
        return this;
    }

    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        application.add(cardPanel);
        application.setResizable(false);

        viewManagerModel.setState(officeView.getViewName());
        officeViewModel.getState().setCurrentBalance(playerDAO.getPlayer().getBalance());
        officeViewModel.getState().setCurrentDay(dayRecordsDAO.getNumberOfDays());
        officeViewModel.getState().setCurrentCustomerCount(customerCount);
        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }

}
