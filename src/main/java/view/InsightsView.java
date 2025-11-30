package view;

import interfaceadapter.insight.DayInsightsController;
import interfaceadapter.insight.InsightsState;
import interfaceadapter.insight.InsightsViewModel;
import interfaceadapter.insight.PerformanceCalculationController;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.List;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Color;


public class InsightsView extends JPanel implements PropertyChangeListener {

    public static final String viewName = "Insights";

    private final InsightsViewModel viewModel;
    private final PerformanceCalculationController controller;
    private final DayInsightsController dayInsightsController;

    private JLabel revenueValueLabel;
    private JLabel expensesValueLabel;
    private JLabel profitValueLabel;
    private JLabel averageRatingValueLabel;

    private JPanel drillDownPanel;
    private TrendChartPanel trendChartPanel;

    public InsightsView(InsightsViewModel viewModel, PerformanceCalculationController controller, DayInsightsController dayInsightsController) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.dayInsightsController = dayInsightsController;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildPerformanceSummaryPanel());
        add(buildChartPanel());
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

    private JPanel buildChartPanel() {
        trendChartPanel = new TrendChartPanel();
        trendChartPanel.setPreferredSize(new Dimension(400, 200));

        JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));

        JLabel chartTitleLabel = new JLabel("Trend Chart");
        chartTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        chartPanel.add(chartTitleLabel);
        chartPanel.add(trendChartPanel);

        return chartPanel;
    }

    private JPanel buildDrillDownPanel() {
        drillDownPanel = new JPanel();
        drillDownPanel.setLayout(new BoxLayout(drillDownPanel, BoxLayout.Y_AXIS));
        drillDownPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        drillDownPanel.add(new JLabel("Drill Down by Day"));

        updateDrillDownButtons(viewModel.getState());
        return drillDownPanel;
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

        if (trendChartPanel != null) {
            trendChartPanel.setTrends(
                    state.getRevenueTrend(),
                    state.getExpensesTrend(),
                    state.getProfitTrend()
            );
        }
    }

    private void updateDrillDownButtons(InsightsState state) {
        if (drillDownPanel == null || state == null) {
            return;
        }
        drillDownPanel.removeAll();
        drillDownPanel.add(new JLabel("Drill Down by Day"));

        int numberOfDays = state.getNumberOfDays();
        if (numberOfDays <= 0) {
            drillDownPanel.add(new JLabel("No days to show yet"));
        } else {
            for (int i = 1; i <= numberOfDays; i++) {
                final int dayNumber = i;
                JButton drillDownDayButton = new JButton("Day " + i);
                drillDownDayButton.addActionListener(e -> dayInsightsController.displayDayInsights(dayNumber));

                drillDownPanel.add(drillDownDayButton);
            }
        }
        drillDownPanel.revalidate();
        drillDownPanel.repaint();
    }


    private static class TrendChartPanel extends JPanel {
        private List<Double> revenueTrend;
        private List<Double> expensesTrend;
        private List<Double> profitTrend;

        public void setTrends(List<Double> revenueTrend, List<Double> expensesTrend, List<Double> profitTrend) {
            this.revenueTrend = revenueTrend;
            this.expensesTrend = expensesTrend;
            this.profitTrend = profitTrend;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (revenueTrend == null || expensesTrend == null || profitTrend == null) {
                g.drawString("No data available", 10, 10);
                return;
            }
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();
            int padding = 30;
            int chartWidth = width - padding * 2;
            int chartHeight = height - padding * 2;

            int maxPoints = 0;
            double maxValue = 0.0;

            if (revenueTrend != null) {
                maxPoints = Math.max(maxPoints, revenueTrend.size());
                for (double currvalue : revenueTrend) {
                    maxValue = Math.max(maxValue, currvalue);
                }
            }
            if (expensesTrend != null) {
                maxPoints = Math.max(maxPoints, expensesTrend.size());
                for (double currvalue : expensesTrend) {
                    maxValue = Math.max(maxValue, currvalue);
                }
            }
            if (profitTrend != null) {
                maxPoints = Math.max(maxPoints, profitTrend.size());
                for (double currvalue : profitTrend) {
                    maxValue = Math.max(maxValue, currvalue);
                }
            }

            if (maxPoints < 2) {
                g2d.drawString("There must be at least 2 days of data to draw trendline", 10, 10);
                return;
            }

            if (maxValue <= 0) {
                maxValue = 1;
            }

            int originX = padding;
            int originY = height - padding;

            g2d.setColor(Color.BLACK);
            g2d.drawLine(originX, originY, originX + chartWidth, originY);
            g2d.drawLine(originX, originY, originX, originY - chartHeight);

            int finalMaxPoints = maxPoints;
            double finalMaxValue = maxValue;

            java.util.function.BiConsumer<List<Double>, Color> drawTrendLine = (list, color) -> {
                if (list == null || list.size() < 2) {
                    return;
                }
                g2d.setColor(color);
                for (int i = 0; i < list.size() -1; i++) {
                    double value1 = list.get(i);
                    double value2 = list.get(i + 1);

                    double point1 = (double) i / (finalMaxPoints - 1);
                    double point2 = (double) (i + 1) / (finalMaxPoints - 1);

                    int x1 = originX + (int) (point1 * chartWidth);
                    int x2 = originX + (int) (point2 * chartWidth);

                    int y1 = originY - (int) ((value1 / finalMaxValue) * chartHeight);
                    int y2 = originY - (int) ((value2 / finalMaxValue) * chartHeight);

                    g2d.drawLine(x1, y1, x2, y2);
                }

            };

            drawTrendLine.accept(revenueTrend, Color.RED);
            drawTrendLine.accept(expensesTrend, Color.BLUE);
            drawTrendLine.accept(profitTrend, Color.GREEN);

            int legendY = padding;
            g2d.setColor(Color.RED);
            g2d.drawString("Revenue", originX + 5, legendY);
            g2d.setColor(Color.BLUE);
            g2d.drawString("Expenses", originX + 80, legendY);
            g2d.setColor(Color.GREEN);
            g2d.drawString("Profit", originX + 160, legendY);


        }
    }

}