package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import interface_adapter.ViewManagerModel;
import interface_adapter.insight.DayInsightsController;
import interface_adapter.insight.InsightsState;
import interface_adapter.insight.InsightsViewModel;
import interface_adapter.insight.PerformanceCalculationController;
import interface_adapter.office.OfficeViewModel;

public class InsightsView extends JPanel implements PropertyChangeListener {

    public static final String DASH = " - ";
    public static final int WIDTH = 400;
    public static final int HEIGHT = 450;
    private static final int NO_DATA_MESSAGE_X = 10;
    private static final int NO_DATA_MESSAGE_Y = 20;
    private static final int LEGEND_REVENUE_X_OFFSET = 5;
    private static final int LEGEND_EXPENSES_X_OFFSET = 80;
    private static final int LEGEND_PROFIT_X_OFFSET = 160;
    private final InsightsViewModel viewModel;
    private final PerformanceCalculationController controller;
    private final DayInsightsController dayInsightsController;
    private final ViewManagerModel viewManagerModel;

    private JLabel revenueValueLabel;
    private JLabel expensesValueLabel;
    private JLabel profitValueLabel;
    private JLabel averageRatingValueLabel;

    private JPanel drillDownPanel;
    private TrendChartPanel trendChartPanel;

    public InsightsView(InsightsViewModel viewModel, PerformanceCalculationController controller,
                        DayInsightsController dayInsightsController, ViewManagerModel viewManagerModel) {
        this.viewModel = viewModel;
        this.controller = controller;
        this.dayInsightsController = dayInsightsController;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildPerformanceSummaryPanel());
        add(buildChartPanel());
        add(buildDrillDownPanel());
        add(buildBackToMainPanel());

        controller.displayInsights();
    }

    private JPanel buildPerformanceSummaryPanel() {

        final JPanel revenuePanel = new JPanel();
        revenuePanel.add(new JLabel("Revenue: $"));
        revenueValueLabel = new JLabel(DASH);
        revenuePanel.add(revenueValueLabel);

        final JPanel expensesPanel = new JPanel();
        expensesPanel.add(new JLabel("Expenses: $"));
        expensesValueLabel = new JLabel(DASH);
        expensesPanel.add(expensesValueLabel);

        final JPanel profitPanel = new JPanel();
        profitPanel.add(new JLabel("Profit: $"));
        profitValueLabel = new JLabel(DASH);
        profitPanel.add(profitValueLabel);

        final JPanel averageRatingPanel = new JPanel();
        averageRatingPanel.add(new JLabel("Average Daily Rating: "));
        // Note: the rating here may be slightly different from Reviews use case rating.
        // The Insights use case Average Daily rating calculates the average of daily averages
        // While Review calculates the overall average of all individual reviews
        averageRatingValueLabel = new JLabel(DASH);
        averageRatingPanel.add(averageRatingValueLabel);

        final JPanel performanceSummaryPanel = new JPanel();
        final JLabel titleLabel = new JLabel("Overall Performance Summary");
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
        trendChartPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        final JPanel chartPanel = new JPanel();
        chartPanel.setLayout(new BoxLayout(chartPanel, BoxLayout.Y_AXIS));

        final JLabel chartTitleLabel = new JLabel("Trend Chart");
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
        final JPanel panel = new JPanel();
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        final JButton backToOffice = new JButton("Back to Office");
        panel.add(backToOffice);
        backToOffice.addActionListener(event -> {
            viewManagerModel.setState(OfficeViewModel.VIEW_NAME);
            viewManagerModel.firePropertyChange();
        });
        return panel;

    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        final InsightsState state = viewModel.getState();
        updateFromState(state);
    }

    private void updateFromState(InsightsState state) {
        if (state != null) {
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
    }

    private void updateDrillDownButtons(InsightsState state) {
        if (drillDownPanel != null && state != null) {
            drillDownPanel.removeAll();
            drillDownPanel.add(new JLabel("Drill Down by Day"));
            final int numberOfDays = state.getNumberOfDays();
            if (numberOfDays <= 0) {
                drillDownPanel.add(new JLabel("No days to show yet"));
            }
            else {
                for (int i = 1; i <= numberOfDays; i++) {
                    final int dayNumber = i;
                    final JButton drillDownDayButton = new JButton("Day " + i);
                    drillDownDayButton.addActionListener(event -> {
                        System.out.println("Drill Down Button Clicked: Day " + dayNumber);
                        dayInsightsController.displayDayInsights(dayNumber);
                    });

                    drillDownPanel.add(drillDownDayButton);
                }
            }
            drillDownPanel.revalidate();
            drillDownPanel.repaint();
        }
    }

    private static final class TrendChartPanel extends JPanel {
        private List<Double> revenueTrend;
        private List<Double> expensesTrend;
        private List<Double> profitTrend;

        public void setTrends(List<Double> newRevenueTrend, List<Double> newExpensesTrend,
                              List<Double> newProfitTrend) {
            this.revenueTrend = newRevenueTrend;
            this.expensesTrend = newExpensesTrend;
            this.profitTrend = newProfitTrend;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (revenueTrend == null || expensesTrend == null || profitTrend == null) {
                g.drawString("No data available", NO_DATA_MESSAGE_X, NO_DATA_MESSAGE_Y);
            }
            else {
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                final int width = getWidth();
                final int height = getHeight();
                final int padding = 30;
                final int chartWidth = width - 2 * padding;
                final int chartHeight = height - 2 * padding;

                int maxPoints = 0;
                double maxValue = Double.NEGATIVE_INFINITY;
                double minValue = Double.POSITIVE_INFINITY;

                if (revenueTrend != null) {
                    maxPoints = Math.max(maxPoints, revenueTrend.size());
                    for (double currvalue : revenueTrend) {
                        maxValue = Math.max(maxValue, currvalue);
                        minValue = Math.min(minValue, currvalue);
                    }
                }
                if (expensesTrend != null) {
                    maxPoints = Math.max(maxPoints, expensesTrend.size());
                    for (double currvalue : expensesTrend) {
                        maxValue = Math.max(maxValue, currvalue);
                        minValue = Math.min(minValue, currvalue);
                    }
                }
                if (profitTrend != null) {
                    maxPoints = Math.max(maxPoints, profitTrend.size());
                    for (double currvalue : profitTrend) {
                        maxValue = Math.max(maxValue, currvalue);
                        minValue = Math.min(minValue, currvalue);
                    }
                }

                if (maxPoints < 2 || minValue == Double.POSITIVE_INFINITY) {
                    g2d.drawString("There must be at least 2 days of data to draw trendline", NO_DATA_MESSAGE_X,
                            NO_DATA_MESSAGE_Y);
                    return;
                }

                if (maxValue == minValue) {
                    maxValue = maxValue + 1;
                    minValue = minValue - 1;
                }

                final int originX = padding;
                final int originY = height - padding;

                final int zeroY;
                if (0 < minValue) {
                    zeroY = originY;
                } else if (0 > maxValue) {
                    zeroY = originY - chartHeight;
                } else {
                    zeroY = originY - (int) (((0 - minValue) / (maxValue - minValue)) * chartHeight);
                }

                g2d.setColor(Color.BLACK);
                g2d.drawLine(originX, zeroY, originX + chartWidth, zeroY);
                g2d.drawLine(originX, originY, originX, originY - chartHeight);

                final int finalMaxPoints = maxPoints;
                final double finalMinValue = minValue;
                final double finalRange = maxValue - minValue;

                final java.util.function.BiConsumer<List<Double>, Color> drawTrendLine = (List<Double> list,
                                                                                          Color color) -> {
                    if (list != null && list.size() >= 2) {
                        g2d.setColor(color);
                        for (int i = 0; i < list.size() - 1; i++) {
                            final double value1 = list.get(i);
                            final double value2 = list.get(i + 1);

                            final double point1 = (double) i / (finalMaxPoints - 1);
                            final double point2 = (double) (i + 1) / (finalMaxPoints - 1);

                            final int x1 = originX + (int) (point1 * chartWidth);
                            final int x2 = originX + (int) (point2 * chartWidth);

                            final int y1 = originY - (int) (((value1 - finalMinValue) / finalRange) * chartHeight);
                            final int y2 = originY - (int) (((value2 - finalMinValue) / finalRange) * chartHeight);

                            g2d.drawLine(x1, y1, x2, y2);
                        }
                    }
                };

                drawTrendLine.accept(revenueTrend, Color.RED);
                drawTrendLine.accept(expensesTrend, Color.BLUE);
                drawTrendLine.accept(profitTrend, Color.GREEN);

                final int legendY = padding;
                g2d.setColor(Color.RED);
                g2d.drawString("Revenue", originX + LEGEND_REVENUE_X_OFFSET, legendY);
                g2d.setColor(Color.BLUE);
                g2d.drawString("Expenses", originX + LEGEND_EXPENSES_X_OFFSET, legendY);
                g2d.setColor(Color.GREEN);
                g2d.drawString("Profit", originX + LEGEND_PROFIT_X_OFFSET, legendY);
            }
        }
    }
}
