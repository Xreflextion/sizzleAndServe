package view;

// import org.json.JSONArray;
// import org.json.JSONObject;

import interface_adapter.insight.InsightsState;
import interface_adapter.insight.InsightsViewModel;
import interface_adapter.insight.PerformanceCalculationController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class InsightsView extends JPanel implements PropertyChangeListener {

    private final InsightsViewModel viewModel;
    private final PerformanceCalculationController controller;

    private JLabel revenueValueLabel;
    private JLabel expensesValueLabel;
    private JLabel profitValueLabel;
    private JLabel averageRatingValueLabel;

    private JPanel drillDownPanel;

    public InsightsView(InsightsViewModel viewModel, PerformanceCalculationController controller) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildPerformanceSummaryPanel());
        // add(buildChartPanel());
        add(buildDrillDownPanel());
        add(buildBackToMainPanel());

        controller.displayInsights();
    }

    private JPanel buildPerformanceSummaryPanel() {

        JPanel revenuePanel = new JPanel();
        revenuePanel.add(new JLabel("Revenue: $"));
        revenueValueLabel = new JLabel(" - ");
        revenuePanel.add(revenueValueLabel);

        JPanel expensesPanel = new JPanel();
        expensesPanel.add(new JLabel("Expenses: $"));
        expensesValueLabel = new JLabel(" - ");
        expensesPanel.add(expensesValueLabel);

        JPanel profitPanel = new JPanel();
        profitPanel.add(new JLabel("Profit: $"));
        profitValueLabel = new JLabel(" - ");
        profitPanel.add(profitValueLabel);

        JPanel averageRatingPanel = new JPanel();
        averageRatingPanel.add(new JLabel("Average Rating: "));
        averageRatingValueLabel = new JLabel(" - ");
        averageRatingPanel.add(averageRatingValueLabel);

        JPanel performanceSummaryPanel = new JPanel();
        JLabel titleLabel = new JLabel("Overall Performance Summary");
        performanceSummaryPanel.add(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        performanceSummaryPanel.setLayout(new BoxLayout(performanceSummaryPanel, BoxLayout.Y_AXIS));
        performanceSummaryPanel.add(revenuePanel);
        performanceSummaryPanel.add(expensesPanel);
        performanceSummaryPanel.add(profitPanel);
        performanceSummaryPanel.add(averageRatingPanel);

        return performanceSummaryPanel;
    }

//    private JPanel buildChartPanel() {
//        JPanel panel = new JPanel();
//        panel.add(new JLabel("TO BE COMPLETED -- ADD TREND CHART"));
//        return panel;
//    }

    private JPanel buildDrillDownPanel() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        updateDrillDownButtons(viewModel.getState());
        return panel;
    }

    private JPanel buildBackToMainPanel() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(new JButton("Back to Main Menu"));
        return panel;

    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        InsightsState state = viewModel.getState();
        updateFromState(state);
    }

    private void updateFromState(InsightsState state) {
        if (state == null) {
            return;
        }

        revenueValueLabel.setText(String.valueOf(state.getTotalRevenue()));
        expensesValueLabel.setText(String.valueOf(state.getTotalExpenses()));
        profitValueLabel.setText(String.valueOf(state.getTotalProfits()));
        averageRatingValueLabel.setText(String.valueOf(state.getAverageRating()));

        updateDrillDownButtons(state);
    }

    private void updateDrillDownButtons(InsightsState state) {
        if (drillDownPanel == null||state==null) {
            return;
        }
        drillDownPanel.removeAll();
        int numberOfDays = state.getNumberOfDays();
        if (numberOfDays <= 0) {
            drillDownPanel.add(new JLabel("No days to show yet"));
        } else{
            for (int i=1; i<=numberOfDays; i++) {
                JButton drillDownDayButton = new JButton("Day "+i);
                drillDownPanel.add(drillDownDayButton);
            }
        }
        drillDownPanel.revalidate();
        drillDownPanel.repaint();
    }

}