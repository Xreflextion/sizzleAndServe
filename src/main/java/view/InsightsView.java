package view;

// import org.json.JSONArray;
// import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;


public class InsightsView extends JPanel {


    public InsightsView() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildPerformanceSummaryPanel());
        add(buildChartPanel());
        add(buildDrillDownPanel());
        add(buildBackToMainPanel());
    }

    private JPanel buildPerformanceSummaryPanel() {

        JPanel revenuePanel = new JPanel();
        revenuePanel.add(new JLabel("Revenue: $"));
        JLabel revenueValue = new JLabel(" - ");
        revenuePanel.add(revenueValue);

        JPanel expensesPanel = new JPanel();
        expensesPanel.add(new JLabel("Expenses: $"));
        JLabel expensesValue = new JLabel(" - ");
        expensesPanel.add(expensesValue);

        JPanel profitPanel = new JPanel();
        profitPanel.add(new JLabel("Profit: $"));
        JLabel profitValue = new JLabel(" - ");
        profitPanel.add(profitValue);

        JPanel averageRatingPanel = new JPanel();
        averageRatingPanel.add(new JLabel("Average Rating: "));
        JLabel averageRatingValue = new JLabel(" - ");
        averageRatingPanel.add(averageRatingValue);

        JPanel performanceSummaryPanel = new JPanel();
        JLabel titleLabel = new JLabel("Today's Performance Summary");
        performanceSummaryPanel.add(titleLabel);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        performanceSummaryPanel.setLayout(new BoxLayout(performanceSummaryPanel, BoxLayout.Y_AXIS));
        performanceSummaryPanel.add(revenuePanel);
        performanceSummaryPanel.add(expensesPanel);
        performanceSummaryPanel.add(profitPanel);
        performanceSummaryPanel.add(averageRatingPanel);

        return performanceSummaryPanel;
    }

    private JPanel buildChartPanel() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("TO BE COMPLETED -- ADD TREND CHART"));
        return panel;
    }

    private JPanel buildDrillDownPanel() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        // to be corrected to LOOP based on # of days passed
        panel.add(new JButton("Day 1"));
        panel.add(new JButton("Day 2"));
        panel.add(new JButton("Day 3"));
        return panel;
    }

    private JPanel buildBackToMainPanel() {
        JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(new JButton("Back to Main Menu"));
        return panel;

    }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             JFrame frame = new JFrame("Insights View");
//             frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//             InsightsView insightsView = new InsightsView();
//             frame.setContentPane(insightsView);
//
//             frame.setSize(900, 600);
//             frame.setLocationRelativeTo(null); // center on screen
//             frame.setVisible(true);
//         });
//     }
}