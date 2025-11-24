package view;


import interface_adapter.ViewManagerModel;
import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;
import interface_adapter.office.OfficeViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ManageWagesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String ViewName = "Wage Manager";

    private final WageViewModel wageViewModel;
    private WageController wageController = null;
    private final ViewManagerModel viewManagerModel;

    private JLabel waiterWage;
    private JLabel cookWage;
    private JLabel totalWage;
    private JLabel currentBalance;
    private JButton waiterAdd;
    private JButton cookAdd;
    private JButton waiterMinus;
    private JButton cookMinus;
    private JButton backToOffice;

    private final int LAYOUT_1 = 20;
    private final int LAYOUT_2 = 10;
    private final int FONT_1 = 18;
    private final int FONT_2 = 15;


    public ManageWagesView(WageViewModel wageViewModel, ViewManagerModel viewManagerModel) {
        this.wageViewModel = wageViewModel;
        this.wageViewModel.addPropertyChangeListener(this);
        this.viewManagerModel = viewManagerModel;

        final JLabel title = new JLabel(ViewName);
        setLayout(new GridLayout(2, 2));
        /*Employee Panel
         */
        JPanel employeePanel = new JPanel();
        employeePanel.setLayout(new BoxLayout(employeePanel, BoxLayout.Y_AXIS));
        /* Cook Panel
         */
        JPanel cookPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        cookPanel.setBorder(BorderFactory.createTitledBorder("Cook Wage"));
        cookMinus = new JButton("−");
        cookAdd = new JButton("+");
        cookWage = new JLabel(String.valueOf(wageViewModel.getState().getCookWage()));
        cookWage.setFont(new Font("Arial", Font.BOLD, FONT_1));
        cookPanel.add(cookMinus);
        cookPanel.add(cookWage);
        cookPanel.add(cookAdd);
        /* Waiter Panel
         */
        JPanel waiterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        waiterPanel.setBorder(BorderFactory.createTitledBorder("Waiter Wage"));
        waiterMinus = new JButton("−");
        waiterAdd = new JButton("+");
        waiterWage = new JLabel(String.valueOf(wageViewModel.getState().getWaiterWage()));
        waiterWage.setFont(new Font("Arial", Font.BOLD, FONT_1));
        waiterPanel.add(waiterMinus);
        waiterPanel.add(waiterWage);
        waiterPanel.add(waiterAdd);
        /* Total wage & balance panel
         */
        JPanel totalWagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        totalWagePanel.setBorder(BorderFactory.createTitledBorder("Total Wage"));
        totalWage = new JLabel(String.valueOf(wageViewModel.getState().getTotalWage()));
        totalWage.setFont(new Font("Arial", Font.BOLD, FONT_1));
        currentBalance = new JLabel("Balance: " + wageViewModel.getState().getCurrentBalance());
        currentBalance.setFont(new Font("Arial", Font.BOLD, FONT_1));

        JLabel totalWageLabel = new JLabel("Total Wage: ");
        totalWageLabel.setFont(new Font("Arial", Font.BOLD, FONT_1));
        totalWagePanel.add(totalWageLabel);
        totalWagePanel.add(totalWage);
        totalWagePanel.add(Box.createHorizontalStrut(16));
        totalWagePanel.add(currentBalance);
        totalWagePanel.add(Box.createHorizontalStrut(16));
        /* Explanation and wage effect monitoring panel
         */
        JPanel explanationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        explanationPanel.setBorder(BorderFactory.createTitledBorder("Explanation"));
        JLabel explanationLabel = new JLabel(
                "<html>You can manage your employee's wage here.<br>" +
                        "Increasing cook's wage increases number of customers per day.<br>" +
                        "Increasing waiter's wage improves review.</html>"
        );
        explanationLabel.setFont(new Font("Arial", Font.PLAIN, FONT_2));
        explanationPanel.add(explanationLabel);


        /*Back to office button
         */
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, LAYOUT_1, LAYOUT_2));
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        backToOffice= new JButton("Back to Office");
        backToOffice.setFont(new Font("Arial", Font.BOLD, FONT_1));
        bottomPanel.add(backToOffice);


        employeePanel.add(cookPanel);
        employeePanel.add(waiterPanel);
        add(totalWagePanel);
        add(employeePanel);
        add(bottomPanel);
        add(explanationPanel);


        // Add listeners
        cookAdd.addActionListener(this);
        cookMinus.addActionListener(this);
        waiterAdd.addActionListener(this);
        waiterMinus.addActionListener(this);
        backToOffice.addActionListener(this);
    }

    // ✅ Setter for Controller
    public void setController(WageController controller) {
        this.wageController = controller;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (wageController == null) return;
        if (e.getSource() == cookAdd) {
            wageController.cookIncrease();
        } else if (e.getSource() == cookMinus) {
            wageController.cookDecrease();
        } else if (e.getSource() == waiterAdd) {
            wageController.waiterIncrease();
        } else if (e.getSource() == waiterMinus) {
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
            WageState newState = (WageState) evt.getNewValue();
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
