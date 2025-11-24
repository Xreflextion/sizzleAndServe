package view;

import interface_adapter.buy_serving.BuyServingViewModel;
import interface_adapter.buy_serving.BuyServingController;
import java.awt.*;
import javax.swing.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.office.OfficeViewModel;

public class BuyServingView extends JPanel {

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

        quantities = new int[3];

        setupUI();

        viewModel.addPropertyChangeListener(evt -> {
            updateView();
        });
    }

    private void setupUI() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // Title
        JLabel storeLabel = new JLabel("Welcome to the Store!");
        storeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(storeLabel);

        // Current Balance
        JPanel balancePanel = new JPanel();
        balancePanel.setLayout(new BoxLayout(balancePanel, BoxLayout.X_AXIS));
        balanceLabel = new JLabel("Current Balance: "
                                  + viewModel.getState().balance);
        balancePanel.add(balanceLabel);
        this.add(balancePanel);

        // Dishes
        dishNameLabels = new JLabel[3];
        stockLabels = new JLabel[3];
        costLabels = new JLabel[3];
        quantityLabels = new JLabel[3];

        for (int i = 0; i < dishNameLabels.length; i++) {
            JPanel dishPanel = new JPanel();
            dishPanel.setLayout(new BoxLayout(dishPanel, BoxLayout.Y_AXIS));

            // First line of each dish serving
            JPanel line1 = new JPanel();
            line1.setLayout(new BoxLayout(line1, BoxLayout.X_AXIS));
            dishNameLabels[i] = new JLabel(viewModel.getState().dishNames[i]);
            stockLabels[i] = new JLabel("Amount in Stock: " + viewModel.getState().dishStocks[i]);
            stockLabels[i].setHorizontalAlignment(SwingConstants.LEFT); // Left-align stock label
            line1.add(dishNameLabels[i]);
            line1.add(Box.createRigidArea(new Dimension(10, 0)));
            line1.add(stockLabels[i]);

            // Second line of each dish serving
            JPanel line2 = new JPanel();
            line2.setLayout(new BoxLayout(line2, BoxLayout.X_AXIS));
            costLabels[i] = new JLabel("Cost: " + viewModel.getState().dishCosts[i]);
            quantityLabels[i] = new JLabel("0");
            JButton minusButton = new JButton("-");
            JButton plusButton = new JButton("+");

            int index = i;
            minusButton.addActionListener(e -> {
                if (quantities[index] > 0) {
                    quantities[index]--;
                    updateView();
                }
            });
            plusButton.addActionListener(e -> {
                quantities[index]++;
                updateView();
            });

            line2.add(costLabels[i]);
            line2.add(Box.createRigidArea(new Dimension(10, 0)));
            line2.add(new JLabel("Buy "));
            line2.add(minusButton);
            line2.add(quantityLabels[i]);
            line2.add(plusButton);

            dishPanel.add(line1);
            dishPanel.add(line2);

            this.add(dishPanel);
        }

        // Confirmation Button
        JPanel confirmPanel = new JPanel();
        //Confirm Button at the bottom right
        confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.X_AXIS));
        confirmPanel.add(Box.createHorizontalGlue());
        JButton confirmButton = new JButton("Confirm Purchasing");

        confirmButton.addActionListener(e -> {
            boolean allZero = true;
            for (int i = 0; i < quantities.length; i++) {
                if(quantities[i] != 0) {
                    allZero = false;
                    break;
                }
            }
            if (!allZero) {
                controller.confirmPurchase(viewModel.getState().dishNames, quantities);
                var state = viewModel.getState();
                showTransactionResult(state.success);
                // Reset all quantities to 0 after confirming purchase
                for (int i = 0; i < quantities.length; i++) {
                    quantities[i] = 0;
                }
                updateView();
            }
        });
        confirmPanel.add(confirmButton);
        this.add(confirmPanel);

        // Back to Office Button Panel
        JPanel backPanel = new JPanel();
        backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.X_AXIS));
        JButton backButton = new JButton("Back to Office");
        backButton.addActionListener(e -> {
            viewManagerModel.setState(OfficeViewModel.VIEW_NAME);
            viewManagerModel.firePropertyChange();
        });
        backPanel.add(backButton);
        this.add(backPanel);
    }

    public void updateView() {
        balanceLabel.setText("Current Balance: " + viewModel.getState().balance);

        for (int i = 0; i < dishNameLabels.length; i++) {
            stockLabels[i].setText("Amount in Stock: " + viewModel.getState().dishStocks[i]);
            quantityLabels[i].setText(String.valueOf(quantities[i]));
        }
    }

    public void showTransactionResult(boolean success) {
        if (success) {
            JOptionPane.showMessageDialog(this,
                                 "Transaction succeeded!",
                                     "Message", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                                 "Insufficient fund. Transactions failed.",
                                     "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
