package view;

import interface_adapter.office.OfficeState;
import interface_adapter.office.OfficeViewModel;
//import interface_adapter.office.SimulateController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class OfficeView extends JPanel implements ActionListener, PropertyChangeListener {
    public static final int HEIGHT = 500;
    public static final int WIDTH = 800;
    private final OfficeViewModel officeViewModel;
//    private SimulateController simulationController;

    private final JLabel curDayLabel;
    private final JLabel curBalanceLabel;
    private final JLabel pastCustomerCountLabel;

    public OfficeView(OfficeViewModel officeViewModel) {
        this.officeViewModel = officeViewModel;
        officeViewModel.addPropertyChangeListener(this);

        this.setLayout(new BorderLayout());

        final JPanel headerPanel = new JPanel();
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        final JLabel title = new JLabel(OfficeViewModel.TITLE_LABEL);
        title.setAlignmentX(Component.RIGHT_ALIGNMENT);

        final JPanel curDetails = new JPanel();

        curDayLabel = new JLabel();
        curBalanceLabel = new JLabel();
        pastCustomerCountLabel = new JLabel();

        curDetails.add(curDayLabel);
        curDetails.add(curBalanceLabel);
        curDetails.add(pastCustomerCountLabel);
        curDetails.setLayout(new BoxLayout(curDetails, BoxLayout.Y_AXIS));

        headerPanel.setLayout( new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.add(curDetails);
        headerPanel.add( Box.createHorizontalGlue() );
        headerPanel.add(title);
        headerPanel.add( Box.createHorizontalGlue() );



        final JPanel bottomPanel = new JPanel();
        bottomPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        bottomPanel.add(Box.createHorizontalGlue());
      
        final JButton simulateButton = new JButton(OfficeViewModel.SIMULATE_LABEL);
        simulateButton.addActionListener(
                evt ->
                {
                    if (evt.getSource().equals(simulateButton)) {
                        final OfficeState currentState = officeViewModel.getState();

//                        simulationController.execute(
//                                currentState.getCurrentDay(),
//                                currentState.getCurrentBalance(),
//                                currentState.getPastCustomerCount()
//                        );
                    }
                }
        );

        bottomPanel.add(simulateButton);
        this.add(headerPanel, BorderLayout.PAGE_START);
        this.add(bottomPanel, BorderLayout.PAGE_END);
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
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

//    public void setSimulationController(SimulateController simulationController) {
//        this.simulationController = simulationController;
//    }
}
