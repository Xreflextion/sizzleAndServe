package app;

import constants.Constants;
import dataaccess.*;
import entity.Employee;
import interfaceadapter.ViewManagerModel;
import entity.Recipe;
import interfaceadapter.manage_wages.WageController;
import interfaceadapter.manage_wages.WagePresenter;
import interfaceadapter.manage_wages.WageState;
import interfaceadapter.manage_wages.WageViewModel;
import interfaceadapter.office.OfficeViewModel;
import interfaceadapter.office.SimulateController;
import interfaceadapter.office.SimulatePresenter;
import interfaceadapter.product_prices.ProductPricesController;
import interfaceadapter.product_prices.ProductPricesPresenter;
import interfaceadapter.product_prices.ProductPricesViewModel;
import interfaceadapter.buy_serving.BuyServingController;
import interfaceadapter.buy_serving.BuyServingPresenter;
import interfaceadapter.buy_serving.BuyServingViewModel;
import usecase.buy_serving.BuyServingInteractor;
import interfaceadapter.review.ReviewController;
import interfaceadapter.review.ReviewPresenter;
import interfaceadapter.review.ReviewViewModel;
import usecase.manage_wage.WageInteractor;
import usecase.product_prices.ProductPricesInteractor;
import usecase.review.ReviewInteractor;
import usecase.simulate.SimulateInputBoundary;
import usecase.simulate.SimulateInteractor;
import usecase.simulate.SimulateOutputBoundary;
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

    private PlayerDataAccessObject playerDAO;
    private PantryDataAccessObject pantryDAO;
    private ReviewDAOHash reviewDAO;
    private DayRecordsDataAccessObject dayRecordsDAO;
    private WageDataAccessObject wageDAO;

    private ManageWagesView wageView;
    private WageViewModel wageViewModel;
    private Map<String, Employee> employees = new HashMap<>();

    private FileHelperObject fileHelperObject;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
        fileHelperObject = new FileHelperObject(Constants.FILE_NAME);
        fileHelperObject.loadFromFile();

        playerDAO = new PlayerDataAccessObject(INITIAL_BALANCE, fileHelperObject);
        pantryDAO = new PantryDataAccessObject();
        reviewDAO = new ReviewDAOHash(new HashMap<>());
        dayRecordsDAO = new DayRecordsDataAccessObject();
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

        // Creates a review interactor
        ReviewInteractor  reviewInteractor = new ReviewInteractor(reviewDAO, reviewPresenter);

        // Creates a review controller
        ReviewController  reviewController = new ReviewController(reviewInteractor);

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
        wageView = new ManageWagesView(wageViewModel,viewManagerModel);
        wageView.setController(controller);
        // 5) Add view to the cardPanel
        cardPanel.add(wageView,wageViewModel.getViewName());
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
                dayRecordsDAO
        );

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
        officeViewModel.getState().setCurrentBalance(playerDAO.getPlayer().getBalance());
        officeViewModel.getState().setCurrentDay(INITIAL_DAY);
        officeViewModel.getState().setCurrentCustomerCount(INITIAL_PAST_CUSTOMER_COUNT);
        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }
}
