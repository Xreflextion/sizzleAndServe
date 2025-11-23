package app;

import data_access.*;
import entity.Employee;
import interface_adapter.ViewManagerModel;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.office.SimulateController;
import interface_adapter.office.SimulatePresenter;
import interface_adapter.product_prices.ProductPricesController;
import interface_adapter.product_prices.ProductPricesPresenter;
import interface_adapter.product_prices.ProductPricesViewModel;
import interface_adapter.review.ReviewController;
import interface_adapter.review.ReviewPresenter;
import interface_adapter.review.ReviewViewModel;
import use_case.product_prices.ProductPricesInteractor;
import use_case.review.ReviewInteractor;
import use_case.simulate.*;
import view.OfficeView;
import view.ProductPricesView;
import view.ViewManager;
import view.ReviewView;
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

    private PantryDataAccessObject pantryDataAccessObject;
    private WageDataAccessObject wageDataAccessObject;
    private PlayerDataAccessObject playerDataAccessObject;
    private ReviewDAOHash reviewDAO;

    public AppBuilder() {
        cardPanel.setLayout(cardLayout);

        // Initialize data access objects
        pantryDataAccessObject = new PantryDataAccessObject();
        wageDataAccessObject = new WageDataAccessObject(new HashMap<>());
        playerDataAccessObject = new PlayerDataAccessObject();
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
        ReviewView reviewView = new ReviewView(reviewController, reviewViewModel);
        cardPanel.add(reviewView, ReviewViewModel.VIEW_NAME);

        return this;
    }


    public AppBuilder addSimulateUseCase() {
        final SimulateOutputBoundary simulateOutputBoundary = new SimulatePresenter(viewManagerModel,
                officeViewModel);
        // hardcoded stuff
        String[] dishNames = pantryDataAccessObject.getPantry().getDishNames();
        Map<String, Integer> stock = new HashMap<>();
        for (String dishName: dishNames) {
            stock.put(dishName, 50);
        }
        pantryDataAccessObject.saveStock(stock);
        Employee employee = new Employee(3, "Cook");
        Employee employee1 = new Employee(3, "Waiter");
        wageDataAccessObject.save(employee);
        wageDataAccessObject.save(employee1);
        final SimulateInputBoundary simulateInteractor = new SimulateInteractor(
                simulateOutputBoundary,
                pantryDataAccessObject,
                reviewDAO,
                wageDataAccessObject,
                playerDataAccessObject
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
        officeViewModel.getState().setCurrentBalance(playerDataAccessObject.getPlayer().getBalance());
        officeViewModel.getState().setCurrentDay(INITIAL_DAY);
        officeViewModel.getState().setPastCustomerCount(INITIAL_PAST_CUSTOMER_COUNT);
        officeViewModel.firePropertyChange();
        viewManagerModel.firePropertyChange();

        return application;
    }
}
