package app;

import data_access.PlayerDataAccessObject;
import interface_adapter.BuyServing.BuyServingController;
import interface_adapter.BuyServing.BuyServingPresenter;
import interface_adapter.BuyServing.BuyServingViewModel;
import view.BuyServingView;
import data_access.PantryDataAccessObject;

import use_case.BuyServing.BuyServingInteractor;

import javax.swing.JFrame;

public class BuyServingAppBuilder {
    public static void main(String[] args) {
        // Initialize DAO, ViewModel, Presenter, Interactor
        PlayerDataAccessObject playerDAO = new PlayerDataAccessObject();
        PantryDataAccessObject pantryDAO = new PantryDataAccessObject();

        BuyServingViewModel viewModel = new BuyServingViewModel();
        // Set initial balance from player
        double initialBalance = playerDAO.getPlayer().getBalance();
        viewModel.setNewBalance(initialBalance);

        // Initialize ViewModel state with dish names, costs, and stocks
        String[] dishNames = pantryDAO.getPantry().getDishNames();
        int[] dishCosts = new int[dishNames.length];
        int[] dishStocks = new int[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            dishCosts[i] = pantryDAO.getPantry().getRecipe(dishNames[i]).getBasePrice();
            dishStocks[i] = pantryDAO.getPantry().getRecipe(dishNames[i]).getStock();
        }
        viewModel.setDishNames(dishNames);
        viewModel.setDishCosts(dishCosts);
        viewModel.setDishStocks(dishStocks);

        BuyServingPresenter presenter = new BuyServingPresenter(viewModel);
        BuyServingInteractor interactor = new BuyServingInteractor(playerDAO, pantryDAO, presenter);
        BuyServingController controller = new BuyServingController(interactor);

        // Create BuyServingView
        BuyServingView view = new BuyServingView(controller, viewModel);

        // Wrap JPanel in JFrame
        JFrame frame = new JFrame("Sizzle and Serve");
        frame.setContentPane(view);
        frame.pack(); // Adjust frame size to fit contents
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
