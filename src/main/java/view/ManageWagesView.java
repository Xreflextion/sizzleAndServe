package view;


import interface_adapter.manage_wages.WageController;
import interface_adapter.manage_wages.WageState;
import interface_adapter.manage_wages.WageViewModel;

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

    private JLabel waiterWage;
    private JLabel cookWage;
    private JLabel totalWage;
    private JLabel currentBalance;
    private JButton waiterAdd;
    private JButton cookAdd;
    private JButton waiterMinus;
    private JButton cookMinus;
    private JButton applyButton;

    private final int layOut1 = 20;
    private final int layOut2 = 10;
    private final int font = 18;


    public ManageWagesView(WageViewModel wageViewModel) {
        this.wageViewModel = wageViewModel;
        this.wageViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(ViewName);
        setLayout(new GridLayout(2, 1));
        /*Employee Panel

         */
        JPanel employeePanel = new JPanel();
        /* Cook Panel
         */
        JPanel cookPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, layOut1, layOut2));
        cookPanel.setBorder(BorderFactory.createTitledBorder("Cook Wage"));
        cookMinus = new JButton("−");
        cookAdd = new JButton("+");
        cookWage = new JLabel(String.valueOf(wageViewModel.getState().getCookWage()));
        cookWage.setFont(new Font("Arial", Font.BOLD, font));
        cookPanel.add(cookMinus);
        cookPanel.add(cookWage);
        cookPanel.add(cookAdd);
        /* Waiter Panel
         */
        JPanel waiterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, layOut1, layOut2));
        waiterPanel.setBorder(BorderFactory.createTitledBorder("Waiter Wage"));
        waiterMinus = new JButton("−");
        waiterAdd = new JButton("+");
        waiterWage = new JLabel(String.valueOf(wageViewModel.getState().getWaiterWage()));
        waiterWage.setFont(new Font("Arial", Font.BOLD, 18));
        waiterPanel.add(waiterMinus);
        waiterPanel.add(waiterWage);
        waiterPanel.add(waiterAdd);
        /* Total wage & balance panel
         */
        JPanel totalWagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, layOut1, layOut2));
        totalWagePanel.setBorder(BorderFactory.createTitledBorder("Total Wage"));
        totalWage = new JLabel(String.valueOf(wageViewModel.getState().getTotalWage()));
        totalWage.setFont(new Font("Arial", Font.BOLD, 18));
        currentBalance = new JLabel("Balance: " + wageViewModel.getState().getCurrentBalance());
        currentBalance.setFont(new Font("Arial", Font.BOLD, 18));
        applyButton = new JButton("Apply");

        totalWagePanel.add(new JLabel("Total Wage: "));
        totalWagePanel.add(totalWage);
        totalWagePanel.add(Box.createHorizontalStrut(16));
        totalWagePanel.add(currentBalance);
        totalWagePanel.add(Box.createHorizontalStrut(16));
        totalWagePanel.add(applyButton);


        employeePanel.add(cookPanel);
        employeePanel.add(waiterPanel);
        add(totalWagePanel);
        add(employeePanel);


        // Add listeners
        cookAdd.addActionListener(this);
        cookMinus.addActionListener(this);
        waiterAdd.addActionListener(this);
        waiterMinus.addActionListener(this);
        applyButton.addActionListener(this);
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
        } else if (e.getSource() == applyButton) {
            wageController.apply();
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
