package view;

import data_access.PantryDataAccessObject;
import entity.Recipe;
import interface_adapter.ViewManagerModel;
import interface_adapter.manage_wages.WageViewModel;
import interface_adapter.office.OfficeState;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.office.SimulateController;
import interface_adapter.product_prices.ProductPricesState;
import interface_adapter.product_prices.ProductPricesViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

public class OfficeView extends JPanel implements ActionListener, PropertyChangeListener {
    private final OfficeViewModel officeViewModel;
    private SimulateController simulationController;
    private final ViewManagerModel viewManagerModel;

    private final PantryDataAccessObject pantryDataAccessObject = new PantryDataAccessObject();

    private final JLabel curDayLabel;
    private final JLabel curBalanceLabel;
    private final JLabel pastCustomerCountLabel;

    public OfficeView(OfficeViewModel officeViewModel, ViewManagerModel viewManagerModel) {
        this.officeViewModel = officeViewModel;
        officeViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(OfficeViewModel.WIDTH, OfficeViewModel.HEIGHT));

        final JLabel title = new JLabel(OfficeViewModel.TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(OfficeViewModel.TITLE_SIZE));
        final JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        final JPanel curDetailsPanel = new JPanel();

        curDayLabel = new JLabel();
        curBalanceLabel = new JLabel();
        pastCustomerCountLabel = new JLabel();

        curDetailsPanel.add(curDayLabel);
        curDetailsPanel.add(curBalanceLabel);
        curDetailsPanel.add(pastCustomerCountLabel);
        curDetailsPanel.setLayout(new BoxLayout(curDetailsPanel, BoxLayout.Y_AXIS));

        final JPanel midLeftPanel = new JPanel();
        midLeftPanel.setLayout(new BoxLayout(midLeftPanel, BoxLayout.Y_AXIS));
        final JPanel centerPanel = new JPanel();
        final JPanel midRightPanel = new JPanel();
        midRightPanel.setLayout(new BoxLayout(midRightPanel, BoxLayout.Y_AXIS));

        final JButton inventoryButton = new JButton(OfficeViewModel.INVENTORY_BUTTON_LABEL);
        editButtonSize(inventoryButton);
        inventoryButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(inventoryButton)) {
                        System.out.println("Go to inventory");
//                        this.viewManagerModel.setState(BuyServingViewModel.VIEW_NAME);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );
        final JButton priceButton = new JButton(OfficeViewModel.PRICE_MANAGER_BUTTON_LABEL);
        editButtonSize(priceButton);
        priceButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(priceButton)) {

                        Map<String, Recipe> recipes = pantryDataAccessObject.getPantry().getPantry();

                        ProductPricesState productPricesState = new ProductPricesState(recipes);

                        ProductPricesViewModel productPricesViewModel = new ProductPricesViewModel(recipes);
                        productPricesViewModel.setState(productPricesState);

                        this.viewManagerModel.setState(ProductPricesViewModel.VIEW_NAME);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );
        final JButton reviewButton = new JButton(OfficeViewModel.REVIEW_MANAGER_BUTTON_LABEL);
        editButtonSize(reviewButton);
        reviewButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(reviewButton)) {
                        System.out.println("Go to reviews");
//                        this.viewManagerModel.setState(ReviewViewModel.VIEW_NAME);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );
        final JButton insightsButton = new JButton(OfficeViewModel.INSIGHTS_BUTTON_LABEL);
        editButtonSize(insightsButton);
        insightsButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(insightsButton)) {
                        System.out.println("Go to insights");
//                        this.viewManagerModel.setState(InsightsViewModel.VIEW_NAME);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );
        final JButton employeeButton = new JButton(OfficeViewModel.EMPLOYEE_BUTTON_LABEL);
        editButtonSize(employeeButton);
        employeeButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(employeeButton)) {
                        System.out.println("Go to wages");
//                        this.viewManagerModel.setState(WageViewModel.VIEW_NAME);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );

        midLeftPanel.add(inventoryButton);
        midLeftPanel.add(reviewButton);
        centerPanel.add(employeeButton);
        midRightPanel.add(priceButton);
        midRightPanel.add(insightsButton);

        final JPanel botRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        final JButton simulateButton = new JButton(OfficeViewModel.SIMULATE_BUTTON_LABEL);
        editButtonSize(simulateButton);
        simulateButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(simulateButton)) {
                        final OfficeState currentState = officeViewModel.getState();

                        simulationController.execute(
                                currentState.getCurrentDay(),
                                currentState.getCurrentBalance(),
                                currentState.getPastCustomerCount()
                        );
                    }
                }
        );

        botRightPanel.add(simulateButton);
        this.add(titlePanel, new GridBagConstraintBuilder().setGridX(1).setGridY(0));
        this.add(curDetailsPanel, new GridBagConstraintBuilder().setGridX(0).setGridY(0).setAnchor(GridBagConstraints.NORTH));
        this.add(centerPanel, new GridBagConstraintBuilder().setGridX(1).setGridY(1));
        this.add(midLeftPanel, new GridBagConstraintBuilder().setGridX(0).setGridY(1).setAnchor(GridBagConstraints.WEST));
        this.add(midRightPanel, new GridBagConstraintBuilder().setGridX(2).setGridY(1).setAnchor(GridBagConstraints.EAST));
        this.add(botRightPanel, new GridBagConstraintBuilder().setGridX(2).setGridY(2));

    }

    public String getViewName() {
        return officeViewModel.getViewName();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("state")) {
            final OfficeState state = (OfficeState) evt.getNewValue();
            curDayLabel.setText(OfficeViewModel.CURRENT_DAY_LABEL + state.getCurrentDay());
            curBalanceLabel.setText(OfficeViewModel.CURRENT_BALANCE_LABEL + state.getCurrentBalance());
            pastCustomerCountLabel.setText(OfficeViewModel.PAST_CUSTOMER_COUNT_LABEL + state.getPastCustomerCount());
        }
    }

    public void editButtonSize(JButton button) {
        button.setMargin(
                (
                        new Insets(
                                OfficeViewModel.BUTTON_PADDING,
                                OfficeViewModel.BUTTON_PADDING,
                                OfficeViewModel.BUTTON_PADDING,
                                OfficeViewModel.BUTTON_PADDING
                        )
                )
        );
    }

    public void setSimulationController(SimulateController simulationController) {
        this.simulationController = simulationController;
    }
}

