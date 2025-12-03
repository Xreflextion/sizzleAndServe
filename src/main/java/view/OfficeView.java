package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.buy_serving.BuyServingViewModel;
import interface_adapter.insight.InsightsViewModel;
import interface_adapter.insight.PerformanceCalculationController;
import interface_adapter.manage_wages.WageViewModel;
import interface_adapter.office.OfficeState;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.office.SimulateController;
import interface_adapter.product_prices.ProductPricesViewModel;
import interface_adapter.review.ReviewViewModel;

public class OfficeView extends JPanel implements ActionListener, PropertyChangeListener {
    private final OfficeViewModel officeViewModel;
    private final ViewManagerModel viewManagerModel;
    private final JLabel curDayLabel;
    private final JLabel curBalanceLabel;
    private final JLabel pastCustomerCountLabel;
    private final JButton inventoryButton;
    private final JButton reviewButton;
    private final JButton insightsButton;
    private final JButton priceButton;
    private final JButton employeeButton;
    private final JButton simulateButton;
    private SimulateController simulationController;
    private PerformanceCalculationController performanceController;

    public OfficeView(OfficeViewModel officeViewModel, ViewManagerModel viewManagerModel) {
        this.officeViewModel = officeViewModel;
        officeViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        this.setLayout(new GridBagLayout());

        curDayLabel = new JLabel();
        curBalanceLabel = new JLabel();
        pastCustomerCountLabel = new JLabel();

        inventoryButton = createButton(OfficeViewModel.INVENTORY_BUTTON_LABEL);
        addViewMovementActionListener(inventoryButton, BuyServingViewModel.VIEW_NAME);

        priceButton = createButton(OfficeViewModel.PRICE_MANAGER_BUTTON_LABEL);
        addViewMovementActionListener(priceButton, ProductPricesViewModel.VIEW_NAME);

        reviewButton = createButton(OfficeViewModel.REVIEW_MANAGER_BUTTON_LABEL);
        addViewMovementActionListener(reviewButton, ReviewViewModel.VIEW_NAME);

        insightsButton = createButton(OfficeViewModel.INSIGHTS_BUTTON_LABEL);
        insightsButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(insightsButton)) {
                        if (performanceController != null) {
                            performanceController.displayInsights();
                        }
                        this.viewManagerModel.setState(InsightsViewModel.VIEW_NAME);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );

        employeeButton = createButton(OfficeViewModel.EMPLOYEE_BUTTON_LABEL);
        addViewMovementActionListener(employeeButton, WageViewModel.VIEW_NAME);

        simulateButton = createButton(OfficeViewModel.SIMULATE_BUTTON_LABEL);
        simulateButton.addActionListener(
                evt -> {
                    if (evt.getSource().equals(simulateButton)) {
                        final OfficeState currentState = officeViewModel.getState();

                        simulationController.execute(
                                currentState.getCurrentDay(),
                                currentState.getPastCustomerCount()
                        );
                    }
                }
        );

        this.add(
                createTitlePanel(),
                new GridBagConstraintBuilder()
                        .setGridX(1)
                        .setGridY(0)
                        .build()
        );
        this.add(
                createCurDetailsPanel(),
                new GridBagConstraintBuilder()
                        .setGridX(0)
                        .setGridY(0)
                        .setAnchor(GridBagConstraints.NORTH)
                        .build()
        );
        this.add(
                createCenterPanel(),
                new GridBagConstraintBuilder()
                        .setGridX(1)
                        .setGridY(1)
                        .build()
        );
        this.add(
                createMidLeftPanel(),
                new GridBagConstraintBuilder()
                        .setGridX(0)
                        .setGridY(1)
                        .setAnchor(GridBagConstraints.WEST)
                        .build()
        );
        this.add(
                createMidRightPanel(),
                new GridBagConstraintBuilder()
                        .setGridX(2)
                        .setGridY(1)
                        .setAnchor(GridBagConstraints.EAST)
                        .build()
        );
        this.add(
                createBotRightPanel(),
                new GridBagConstraintBuilder()
                        .setGridX(2)
                        .setGridY(2)
                        .build()
        );

    }

    /**
     * Create title panel.
     *
     * @return JPanel with title
     */
    public JPanel createTitlePanel() {
        final JLabel title = new JLabel(OfficeViewModel.TITLE_LABEL);
        title.setFont(title.getFont().deriveFont(OfficeViewModel.TITLE_SIZE));
        final JPanel titlePanel = new JPanel();
        titlePanel.add(title);
        return titlePanel;
    }

    /**
     * Create panel with current day details.
     *
     * @return JPanel with current day, current balance, and today's customer count
     */
    public JPanel createCurDetailsPanel() {
        final JPanel curDetailsPanel = new JPanel();

        curDetailsPanel.add(curDayLabel);
        curDetailsPanel.add(curBalanceLabel);
        curDetailsPanel.add(pastCustomerCountLabel);
        curDetailsPanel.setLayout(new BoxLayout(curDetailsPanel, BoxLayout.Y_AXIS));
        return curDetailsPanel;
    }

    /**
     * Create panel in the middle left.
     *
     * @return JPanel with inventory and review button
     */
    public JPanel createMidLeftPanel() {
        final JPanel midLeftPanel = new JPanel();
        midLeftPanel.setLayout(new BoxLayout(midLeftPanel, BoxLayout.Y_AXIS));

        midLeftPanel.add(inventoryButton);
        midLeftPanel.add(reviewButton);
        return midLeftPanel;
    }

    /**
     * Create panel in the center.
     *
     * @return JPanel with employee button
     */
    public JPanel createCenterPanel() {
        final JPanel centerPanel = new JPanel();
        centerPanel.add(employeeButton);
        return centerPanel;
    }

    /**
     * Create panel in the middle right.
     *
     * @return JPanel with price and insights button
     */
    public JPanel createMidRightPanel() {
        final JPanel midRightPanel = new JPanel();
        midRightPanel.setLayout(new BoxLayout(midRightPanel, BoxLayout.Y_AXIS));

        midRightPanel.add(priceButton);
        midRightPanel.add(insightsButton);
        return midRightPanel;
    }

    /**
     * Create panel at the bottom right.
     *
     * @return JPanel with the simulate button
     */
    public JPanel createBotRightPanel() {
        final JPanel botRightPanel = new JPanel();

        botRightPanel.add(simulateButton);
        return botRightPanel;
    }

    /**
     * Create a button with default size.
     *
     * @param label the label of the button
     * @return the button
     */
    public JButton createButton(String label) {
        final JButton button = new JButton(label);
        editButtonSize(button);
        return button;
    }

    /**
     * Add an action listener to the button for changing views.
     *
     * @param button the button to add the action listener to
     * @param state  the state to move to
     */
    public void addViewMovementActionListener(JButton button, String state) {
        button.addActionListener(
                evt -> {
                    if (evt.getSource().equals(button)) {
                        this.viewManagerModel.setState(state);
                        this.viewManagerModel.firePropertyChange();
                    }
                }
        );
    }

    /**
     * Apply default margins to button.
     *
     * @param button The button of which to change margins
     */
    public void editButtonSize(JButton button) {
        button.setMargin(
                new Insets(
                        OfficeViewModel.BUTTON_PADDING,
                        OfficeViewModel.BUTTON_PADDING,
                        OfficeViewModel.BUTTON_PADDING,
                        OfficeViewModel.BUTTON_PADDING
                )
        );
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

            if (state.getErrorMessage() != null) {
                JOptionPane.showMessageDialog(this,
                        state.getErrorMessage(),
                        OfficeViewModel.BANKRUPT_ERROR_TITLE, JOptionPane.ERROR_MESSAGE
                );
            }
            else {
                curDayLabel.setText(OfficeViewModel.CURRENT_DAY_LABEL + state.getCurrentDay());
                curBalanceLabel.setText(OfficeViewModel.CURRENT_BALANCE_LABEL + state.getCurrentBalance());
                pastCustomerCountLabel.setText(
                        OfficeViewModel.PAST_CUSTOMER_COUNT_LABEL + state.getPastCustomerCount()
                );
            }
        }
    }

    public void setSimulationController(SimulateController simulationController) {
        this.simulationController = simulationController;
    }

    public void setPerformanceController(PerformanceCalculationController performanceController) {
        this.performanceController = performanceController;
    }
}
