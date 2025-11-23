package view;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrillDownView extends JPanel {

    private final JButton backButton = new JButton("Back to Insights");

    public DrillDownView() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        add(buildDrillDownPanel());

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(DrillDownView.this);
                frame.setContentPane(new InsightsView());
                frame.revalidate();
                frame.repaint();
            }
        });
    }


    private JPanel buildDrillDownPanel(){
        JPanel titlePanel = new JPanel();
        titlePanel.add(new JLabel("Day "));
        JLabel dayNum = new JLabel(" n ");
        titlePanel.add(dayNum);

        JPanel revenuePanel = new JPanel();
        revenuePanel.add(new JLabel("Revenue of the Day: $"));
        JLabel revenueValue = new JLabel(" - ");
        revenuePanel.add(revenueValue);

        JPanel expensesPanel = new JPanel();
        expensesPanel.add(new JLabel("Expenses of the Day: $"));
        JLabel expensesValue = new JLabel(" - ");
        expensesPanel.add(expensesValue);

        JPanel profitPanel = new JPanel();
        profitPanel.add(new JLabel("Profit of the Day: $"));
        JLabel profitValue = new JLabel(" - ");
        profitPanel.add(profitValue);

        JPanel ratingPanel = new JPanel();
        ratingPanel.add(new JLabel("Rating of the Day: "));
        JLabel ratingValue = new JLabel(" - ");
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
