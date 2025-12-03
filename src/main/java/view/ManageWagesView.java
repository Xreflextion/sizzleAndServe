package view;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import interface_adapter.office.OfficeViewModel;

public class ManageWagesView extends JPanel implements ActionListener, PropertyChangeListener {
    private static final String VIEW_NAME = "Wage Manager";
    private static final int LAYOUT_1 = 20;
    private static final int LAYOUT_2 = 10;
    private static final int FONT_1 = 18;
    private static final int FONT_2 = 15;
    private static final int WIDTH = 16;
    private static final String FONT = "Arial";

    private final WageViewModel wageViewModel;
    private final ViewManagerModel viewManagerModel;
    private WageController wageController;
    private JLabel waiterWage;
    private JLabel cookWage;
    private JLabel totalWage;
    private JLabel currentBalance;
    private JButton waiterAdd = new JButton("+");
    private JButton waiterMinus = new JButton("-");
    private JButton cookAdd = new JButton("+");
    private JButton cookMinus = new JButton("-");
    private JButton backToOffice;
    private JPanel employeePanel;
    private JPanel cookPanel;
    private JPanel waiterPanel;
    private JPanel totalWagePanel;
    private JPanel explanationPanel;
    private JPanel bottomPanel;

    public ManageWagesView(
            WageViewModel wageViewModel,
            ViewManagerModel viewManagerModel) {
        this.wageViewModel = wageViewModel;
        this.wageViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        setLayout(new GridLayout(2, 2));

        /* Employee Panel
         */
        employeePanel = new JPanel();
        employeePanel.setLayout(new BoxLayout(employeePanel, BoxLayout.Y_AXIS));

        /* Total wage & balance panel
         */
        totalWagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        totalWagePanel.setBorder(BorderFactory.createTitledBorder("Total Wage"));
        totalWage = new JLabel(String.valueOf(wageViewModel.getState().getTotalWage()));
        totalWage.setFont(new Font(FONT, Font.BOLD, FONT_1));
        currentBalance = new JLabel("Balance: " + wageViewModel.getState().getCurrentBalance());
        currentBalance.setFont(new Font(FONT, Font.BOLD, FONT_1));

        final JLabel totalWageLabel = new JLabel("Total Wage: ");
        totalWageLabel.setFont(new Font(FONT, Font.BOLD, FONT_1));
        totalWagePanel.add(totalWageLabel);
        totalWagePanel.add(totalWage);
        totalWagePanel.add(Box.createHorizontalStrut(WIDTH));
        totalWagePanel.add(currentBalance);
        totalWagePanel.add(Box.createHorizontalStrut(WIDTH));

        /* Explanation and wage effect monitoring panel
         */
        explanationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        explanationPanel.setBorder(BorderFactory.createTitledBorder("Explanation"));
        final JLabel explanationLabel = new JLabel(
                "<html>You can manage your employee's wage here.<br>"
                        + "Increasing cook's wage increases number of customers per day.<br>"
                        + "Increasing waiter's wage improves review.</html>"
        );
        explanationLabel.setFont(new Font(FONT, Font.PLAIN, FONT_2));
        explanationPanel.add(explanationLabel);

        /* Back to office button
         */
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        backToOffice = new JButton("Back to Office");
        backToOffice.setFont(new Font(FONT, Font.BOLD, FONT_1));
        bottomPanel.add(backToOffice);
    }

    /**
     * Initializing employee panels.
     */
    public void initializePanels() {
        /* Cook Panel
         */
        cookPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        cookPanel.setBorder(BorderFactory.createTitledBorder("Cook Wage"));
        cookWage = new JLabel(String.valueOf(wageViewModel.getState().getCookWage()));
        cookWage.setFont(new Font(FONT, Font.BOLD, FONT_1));
        cookPanel.add(cookMinus);
        cookPanel.add(cookWage);
        cookPanel.add(cookAdd);

        /* Waiter Panel
         */
        waiterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        waiterPanel.setBorder(BorderFactory.createTitledBorder("Waiter Wage"));
        waiterWage = new JLabel(String.valueOf(wageViewModel.getState().getWaiterWage()));
        waiterWage.setFont(new Font(FONT, Font.BOLD, FONT_1));
        waiterPanel.add(waiterMinus);
        waiterPanel.add(waiterWage);
        waiterPanel.add(waiterAdd);

    }

    /**
     * Setting Panels.
     */
    public void setPanels() {
        employeePanel.add(cookPanel);
        employeePanel.add(waiterPanel);
        add(totalWagePanel);
        add(employeePanel);
        add(bottomPanel);
        add(explanationPanel);
    }

    /**
     * Adding ActionListener to buttons.
     */
    public void setActionListener() {
        cookAdd.addActionListener(this);
        cookMinus.addActionListener(this);
        waiterAdd.addActionListener(this);
        waiterMinus.addActionListener(this);
        backToOffice.addActionListener(this);
    }

    /**
     * Setting Controller.
     *
     * @param controller the controller that has been created.
     */
    public void setController(WageController controller) {
        this.wageController = controller;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cookAdd) {
            wageController.cookIncrease();
        }
        else if (e.getSource() == cookMinus) {
            wageController.cookDecrease();
        }
        else if (e.getSource() == waiterAdd) {
            wageController.waiterIncrease();
        }
        else if (e.getSource() == waiterMinus) {
            wageController.waiterDecrease();
        }
        else if (e.getSource() == backToOffice) {
            viewManagerModel.setState(OfficeViewModel.VIEW_NAME);
            viewManagerModel.firePropertyChange();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("state".equals(evt.getPropertyName())) {
            final WageState newState = (WageState) evt.getNewValue();
            cookWage.setText(String.valueOf(newState.getCookWage()));
            waiterWage.setText(String.valueOf(newState.getWaiterWage()));
            totalWage.setText(String.valueOf(newState.getTotalWage()));
            currentBalance.setText("Balance: " + newState.getCurrentBalance());
            if (newState.getWarningMessage() != null) {
                JOptionPane.showMessageDialog(
                        this,
                        newState.getWarningMessage(),
                        "Warning",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
    }
}
