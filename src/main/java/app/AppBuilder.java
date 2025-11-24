package app;

import data_access.*;
import entity.Employee;
import interface_adapter.ViewManagerModel;
import interface_adapter.ViewModel;
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
import use_case.manage_wage.WageInteractor;
import use_case.manage_wage.WagePlayerDataAccessInterface;
import use_case.manage_wage.WageUserDataAccessInterface;
import use_case.product_prices.ProductPricesInteractor;
import use_case.product_prices.ProductPricesOutputBoundary;
import use_case.review.ReviewInteractor;
import use_case.review.ReviewOutputData;
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

    PlayerDataAccessObject playerDAO = new PlayerDataAccessObject(INITIAL_BALANCE);
    PantryDataAccessObject pantryDAO = new PantryDataAccessObject();
    private ReviewDAOHash reviewDAO;


    public AppBuilder() {
        cardPanel.setLayout(cardLayout);
    }

    public AppBuilder addOfficeView() {
        officeViewModel = new OfficeViewModel();
        officeView = new OfficeView(officeViewModel, viewManagerModel);
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

        BuyServingPresenter presenter = new BuyServingPresenter(buyServingViewModel);
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
        ReviewInteractor  reviewInteractor = new ReviewInteractor(reviewDAO, reviewPresenter);

        // Creates a review controller
        ReviewController  reviewController = new ReviewController(reviewInteractor);

        // Initializes View and add it to card panel
        ReviewView reviewView = new ReviewView(reviewController, reviewViewModel, viewManagerModel);
        cardPanel.add(reviewView, ReviewViewModel.VIEW_NAME);

        return this;
    }

    // Adds the ManageWageView to the app builder
    public AppBuilder addManageWageUseCase() {
        // 1) Initialize the playerDAO
        WagePlayerDataAccessInterface playerDataAccess= new PlayerDataAccessObject();
        // 2) Initialize the two employees with wage=0, effect=1 on every run
        Map<String, Employee> employees = new HashMap<>() {{
            put("Cook", new Employee(1, "Cook"));
            put("Waiter", new Employee(1, "Waiter"));
        }};
        // 3) ViewModel + seed initial state so labels are correct immediately
        WageViewModel wageViewModel = new WageViewModel();
        WageState state = wageViewModel.getState();
        state.setCookWage(employees.get("Cook").getWage());                // 0
        state.setCookWageEffect(employees.get("Cook").getWageEffect());    // 1.0
        state.setWaiterWage(employees.get("Waiter").getWage());            // 0
        state.setWaiterWageEffect(employees.get("Waiter").getWageEffect());// 1.0
        state.setCurrentBalance(playerDataAccess.getPlayer().getBalance());
        wageViewModel.setState(state); // fires property change

        // 4) WageDataAccess + Presenter + Controller
        WageUserDataAccessInterface dataAccess = new data_access.WageDataAccessObject(employees);
        WagePresenter presenter = new WagePresenter(wageViewModel);
        WageController controller =
                new WageController(new WageInteractor(dataAccess, playerDataAccess, presenter, employees));

        // 5) Build the view and inject the controller
        ManageWagesView wageView = new ManageWagesView(wageViewModel,viewManagerModel);
        wageView.setController(controller);
        // 6) Add view to the cardPanel
        cardPanel.add(wageView,wageViewModel.getViewName());
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
