package view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.insight.InsightsState;
import interface_adapter.insight.InsightsViewModel;

public class DrillDownView extends JPanel implements PropertyChangeListener {
    public static final String DASH = " - ";
    public static final String VIEW_NAME = "Drill Down Insights";

    private final InsightsViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    private JLabel dayNumber;
    private JLabel revenueValue;
    private JLabel expensesValue;
    private JLabel profitValue;
    private JLabel ratingValue;

    // private final JButton backButton = new JButton("Back to Insights");

    public DrillDownView(InsightsViewModel viewModel, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildDrillDownPanel());
        add(buildBackButtonPanel());

//      backButton.addActionListener(new ActionListener() {
//          @Override
//          public void actionPerformed(ActionEvent e) {
//              JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DrillDownView.this);
//              frame.setContentPane(new InsightsView());
//              frame.revalidate();
//              frame.repaint();
//          }
//      });
    }

    private JPanel buildDrillDownPanel() {
        final JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Day "));
        dayNumber = new JLabel(DASH);
        titlePanel.add(dayNumber);

        final JPanel revenuePanel = new JPanel();
        revenuePanel.add(new JLabel("Revenue of the Day: $"));
        revenueValue = new JLabel(DASH);
        revenuePanel.add(revenueValue);

        final JPanel expensesPanel = new JPanel();
        expensesPanel.add(new JLabel("Expenses of the Day: $"));
        expensesValue = new JLabel(DASH);
        expensesPanel.add(expensesValue);

        final JPanel profitPanel = new JPanel();
        profitPanel.add(new JLabel("Profit of the Day: $"));
        profitValue = new JLabel(DASH);
        profitPanel.add(profitValue);

        final JPanel ratingPanel = new JPanel();
        ratingPanel.add(new JLabel("Rating of the Day: "));
        ratingValue = new JLabel(DASH);
        ratingPanel.add(ratingValue);

        final JPanel drillDownPanel = new JPanel();
        drillDownPanel.setLayout(new BoxLayout(drillDownPanel, BoxLayout.Y_AXIS));
        drillDownPanel.add(titlePanel);
        drillDownPanel.add(revenuePanel);
        drillDownPanel.add(expensesPanel);
        drillDownPanel.add(profitPanel);
        drillDownPanel.add(ratingPanel);
        // drillDownPanel.add(backButton);

        return drillDownPanel;
    }

    private JPanel buildBackButtonPanel() {
        final JPanel backPanel = new JPanel();
        final JButton backButton = new JButton("Back to Insights");
        backPanel.add(backButton);

        backButton.addActionListener(event -> {
            System.out.println("Back to Insights Button Clicked");
            viewManagerModel.setState(viewModel.getViewName());
            viewManagerModel.firePropertyChange();

        });
        return backPanel;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final InsightsState state = viewModel.getState();

        if (state != null) {
            dayNumber.setText(String.valueOf(state.getDayNumber()));
            revenueValue.setText(String.valueOf(state.getDayRevenue()));
            expensesValue.setText(String.valueOf(state.getDayExpenses()));
            profitValue.setText(String.valueOf(state.getDayProfits()));
            ratingValue.setText(String.valueOf(state.getDayRating()));
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("DrillDown Demo");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setContentPane(new DrillDownView());
//            frame.pack();
//            frame.setSize(400, 300);
//            frame.setLocationRelativeTo(null);
//            frame.setVisible(true);
//        });
//    }
}
