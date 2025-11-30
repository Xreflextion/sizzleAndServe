package view;

import interfaceadapter.insight.InsightsState;
import interfaceadapter.insight.InsightsViewModel;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class DrillDownView extends JPanel implements PropertyChangeListener {

    public static final String VIEW_NAME = "Drill Down Insights";

    private final InsightsViewModel viewModel;


    private JLabel dayNumber;
    private JLabel revenueValue;
    private JLabel expensesValue;
    private JLabel profitValue;
    private JLabel ratingValue;

    private final JButton backButton = new JButton("Back to Insights");

    public DrillDownView(InsightsViewModel viewModel) {
        this.viewModel = viewModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildDrillDownPanel());

//        backButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DrillDownView.this);
//                frame.setContentPane(new InsightsView());
//                frame.revalidate();
//                frame.repaint();
//            }
//        });
    }


    private JPanel buildDrillDownPanel(){
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Day "));
        dayNumber = new JLabel(" - ");
        titlePanel.add(dayNumber);

        JPanel revenuePanel = new JPanel();
        revenuePanel.add(new JLabel("Revenue of the Day: $"));
        revenueValue = new JLabel(" - ");
        revenuePanel.add(revenueValue);

        JPanel expensesPanel = new JPanel();
        expensesPanel.add(new JLabel("Expenses of the Day: $"));
        expensesValue = new JLabel(" - ");
        expensesPanel.add(expensesValue);

        JPanel profitPanel = new JPanel();
        profitPanel.add(new JLabel("Profit of the Day: $"));
        profitValue = new JLabel(" - ");
        profitPanel.add(profitValue);

        JPanel ratingPanel = new JPanel();
        ratingPanel.add(new JLabel("Rating of the Day: "));
        ratingValue = new JLabel(" - ");
        ratingPanel.add(ratingValue);

        JPanel drillDownPanel = new JPanel();
        drillDownPanel.setLayout(new BoxLayout(drillDownPanel, BoxLayout.Y_AXIS));
        drillDownPanel.add(titlePanel);
        drillDownPanel.add(revenuePanel);
        drillDownPanel.add(expensesPanel);
        drillDownPanel.add(profitPanel);
        drillDownPanel.add(ratingPanel);
        drillDownPanel.add(backButton);

        return drillDownPanel;


    }

    @Override
    public void propertyChange (PropertyChangeEvent evt){
        InsightsState state = viewModel.getState();

        if (state == null){
            return;
        }
        dayNumber.setText(String.valueOf(state.getDayNumber()));
        revenueValue.setText(String.valueOf(state.getDayRevenue()));
        expensesValue.setText(String.valueOf(state.getDayExpenses()));
        profitValue.setText(String.valueOf(state.getDayProfits()));
        ratingValue.setText(String.valueOf(state.getDayRating()));

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
