package view;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import interface_adapter.ViewManagerModel;
import interface_adapter.buy_serving.BuyServingController;
import interface_adapter.buy_serving.BuyServingViewModel;
import interface_adapter.office.OfficeViewModel;

public class BuyServingView extends JPanel {

    private static final int NUM_DISHES = 3;
    private static final int BOX_WIDTH = 10;

    private final BuyServingController controller;
    private final BuyServingViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private JLabel balanceLabel;

    private JLabel[] dishNameLabels;
    private JLabel[] stockLabels;
    private JLabel[] costLabels;
    private JLabel[] quantityLabels;

    private int[] quantities;

    public BuyServingView(BuyServingController controller, BuyServingViewModel viewModel,
                          ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        quantities = new int[NUM_DISHES];

        setupUserInterface();

        viewModel.addPropertyChangeListener(evt -> {
            updateView();
        });
    }

    private void setupUserInterface() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        setupTitle();
        setupBalancePanel();
        setupDishPanels();
        setupConfirmButton();
        setupBackButton();

        viewModel.addPropertyChangeListener(evt -> updateView());
    }

    private void setupTitle() {
        final JLabel storeLabel = new JLabel("Welcome to the Store!");
        storeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(storeLabel);
    }

    private void setupBalancePanel() {
        final JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.X_AXIS));
        balanceLabel = new JLabel("Current Balance: " + viewModel.getState().getBalance());
        balancePanel.add(balanceLabel);
        this.add(balancePanel);
    }

    private void setupDishPanels() {
        dishNameLabels = new JLabel[NUM_DISHES];
        stockLabels = new JLabel[NUM_DISHES];
        costLabels = new JLabel[NUM_DISHES];
        quantityLabels = new JLabel[NUM_DISHES];

        for (int i = 0; i < NUM_DISHES; i++) {
            this.add(createDishPanel(i));
        }
    }

    private JPanel createDishPanel(int index) {
        final JPanel dishPanel = new JPanel();
        dishPanel.setLayout(new BoxLayout(dishPanel, BoxLayout.Y_AXIS));

        // first line
        final JPanel line1 = new JPanel();
        line1.setLayout(new BoxLayout(line1, BoxLayout.X_AXIS));
        dishNameLabels[index] = new JLabel(viewModel.getState().getDishNames()[index]);
        stockLabels[index] = new JLabel("Amount in Stock: " + viewModel.getState().getDishStocks()[index]);
        stockLabels[index].setHorizontalAlignment(SwingConstants.LEFT);
        line1.add(dishNameLabels[index]);
        line1.add(Box.createRigidArea(new Dimension(BOX_WIDTH, 0)));
        line1.add(stockLabels[index]);
        dishPanel.add(line1);

        // second line
        final JPanel line2 = new JPanel();
        line2.setLayout(new BoxLayout(line2, BoxLayout.X_AXIS));
        costLabels[index] = new JLabel("Cost: " + viewModel.getState().getDishCosts()[index]);
        quantityLabels[index] = new JLabel("0");
        final JButton minusButton = new JButton("-");
        final JButton plusButton = new JButton("+");

        minusButton.addActionListener(event -> {
            if (quantities[index] > 0) {
                quantities[index]--;
            }
            updateView();
        });
        plusButton.addActionListener(event -> {
            quantities[index]++;
            updateView();
        });

        line2.add(costLabels[index]);
        line2.add(Box.createRigidArea(new Dimension(BOX_WIDTH, 0)));
        line2.add(new JLabel("Buy "));
        line2.add(minusButton);
        line2.add(quantityLabels[index]);
        line2.add(plusButton);
        dishPanel.add(line2);

        return dishPanel;
    }

    private void setupConfirmButton() {
        final JPanel confirmPanel = new JPanel();
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.X_AXIS));
        confirmPanel.add(Box.createHorizontalGlue());
        final JButton confirmButton = new JButton("Confirm Purchasing");

        confirmButton.addActionListener(event -> handleConfirmPurchase());
        confirmPanel.add(confirmButton);
        this.add(confirmPanel);
    }

    private void handleConfirmPurchase() {
        boolean allZero = true;
        for (int i = 0; i < NUM_DISHES; i++) {
            if (quantities[i] != 0) {
                allZero = false;
                break;
            }
        }
        if (!allZero) {
            controller.confirmPurchase(viewModel.getState().getDishNames(), quantities);
            final var state = viewModel.getState();
            showTransactionResult(state.isSuccess());
            for (int i = 0; i < NUM_DISHES; i++) {
                quantities[i] = 0;
            }
            updateView();
        }
    }

    private void setupBackButton() {
        final JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        final JButton backButton = new JButton("Back to Office");
        backButton.addActionListener(event -> {
            viewManagerModel.setState(OfficeViewModel.VIEW_NAME);
            viewManagerModel.firePropertyChange();
        });
        backPanel.add(backButton);
        this.add(backPanel);
    }

    /**
     * Updates the user interface to reflect the current state of the view model.
     * This method refreshes:
     * the balance label to show the user's current balance
     * the stock labels to show the current stock for each dish
     * the quantity labels to show the currently selected quantities for purchase
     */
    public void updateView() {
        balanceLabel.setText("Current Balance: " + viewModel.getState().getBalance());

        for (int i = 0; i < NUM_DISHES; i++) {
            stockLabels[i].setText("Amount in Stock: " + viewModel.getState().getDishStocks()[i]);
            quantityLabels[i].setText(String.valueOf(quantities[i]));
        }
    }

    /**
     * Displays a pop-up message to inform the user of the result of a purchase transaction.
     *
     * @param success true if the transaction succeeded, false if it failed (e.g., insufficient funds)
     */
    public void showTransactionResult(boolean success) {
        if (success) {
            JOptionPane.showMessageDialog(this,
                    "Transaction succeeded!",
                    "Message", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Insufficient fund. Transactions failed.",
                    "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
