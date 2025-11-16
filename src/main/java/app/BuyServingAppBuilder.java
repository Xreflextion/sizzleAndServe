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

        BuyServingViewModel viewModel = new BuyServingViewModel(
                playerDAO.getPlayer(),
                pantryDAO.getPantry(),
                pantryDAO.getPantry().getMenuList()
        );
        BuyServingPresenter presenter = new BuyServingPresenter(viewModel);
        BuyServingInteractor interactor = new BuyServingInteractor(playerDAO, pantryDAO, presenter);

        // Get dish names and dish prices and create Controller
        String[] dishNames = pantryDAO.getPantry().getDishNames();
        double[] dishPrices = new double[dishNames.length];
        for (int i = 0; i < dishNames.length; i++) {
            dishPrices[i] = pantryDAO.getPantry().getRecipe(dishNames[i]).getCost();
        }
        BuyServingController controller = new BuyServingController(interactor, dishNames, dishPrices);

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
