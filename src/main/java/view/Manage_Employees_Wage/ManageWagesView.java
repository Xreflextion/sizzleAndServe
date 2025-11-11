package view.Manage_Employees_Wage;


import interface_adapter.ManageWages.WageViewModel;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

public class ManageWagesView extends JPanel implements ActionListener, PropertyChangeListener {
    private final String ViewName = "Wage Manager";

    private final WageViewModel wageViewModel;

    private static final int WIDTH = 300;
    private static final int HEIGHT = 150;


    public ManageWagesView(WageViewModel wageViewModel) {
        this.wageViewModel = wageViewModel;
        this.wageViewModel.addPropertyChangeListener(this);

        final JLabel title = new JLabel(ViewName);

    }

}
