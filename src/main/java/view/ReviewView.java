package view;


import javax.swing.*;
import java.awt.*;
import java.sql.Array;


public class ReviewView{

    final String FONT = "Arial";
    //private ReviewInteractor interactor;
    private int currentDay;


    /**
     public ReviewView(ReviewInteractor interactor, int currentDay) {
     this.interactor = interactor;
     this.currentDay = currentDay;
     GUI();
     }
     */
    public ReviewView() {
        GUI();
    }

    private void GUI(){
        JPanel dayPanel;
        JPanel overallPanel;
        JPanel menuPanel;
        JFrame frame;

        // Creates new frame
        frame = new JFrame("Office - Reviews");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.setLayout(new BorderLayout());

        // Use a simple container that uses CardLayout — it resizes automatically
        JPanel container = new JPanel(new CardLayout());
        frame.add(container, BorderLayout.CENTER);

        // Menu Panel to determine if the user wants overall or just day

        // The menu panel
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBounds(0, 0, 400, 400);

        // Setting the title
        JLabel title = new JLabel("Office - Reviews");
        title.setFont(new Font(FONT, Font.BOLD, 28));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(title);

        // The "Select option below" subtitle
        JLabel choiceLabel = new JLabel("Select an option: ");
        choiceLabel.setFont(new Font(FONT, Font.BOLD, 22));
        choiceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(Box.createVerticalStrut(15));
        menuPanel.add(choiceLabel);

        // Overall Button
        JButton overallButton = new JButton("Overall");
        overallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        overallButton.setFont(new Font(FONT, Font.PLAIN, 20));
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(overallButton);

        // Day Button
        JButton dayButton = new JButton("Day");
        dayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayButton.setFont(new Font(FONT, Font.PLAIN, 20));
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(dayButton);




        // This is the overall panel
        overallPanel = new JPanel();
        overallPanel.setLayout(new BoxLayout(overallPanel, BoxLayout.Y_AXIS));
        overallPanel.setBounds(0, 0, 400, 400);

        // Label that will contain statement saying how much stars
        JLabel overallLabel = new JLabel();

        // Does the formatting for the overallLabel
        overallLabel.setFont(new Font(FONT, Font.PLAIN, 36));
        overallLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Emoji used
        JLabel overallEmoji = new JLabel("😐");

        // Formatting for emoji
        overallEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        overallEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Creating the back button
        JButton backOverall = new JButton("Back");

        // Formatting the back button
        backOverall.setFont(new Font(FONT, Font.PLAIN, 20));
        backOverall.setAlignmentX(Component.CENTER_ALIGNMENT);


        // This parts adds all the new elements to the overallPanel
        // Vertical strut is for spacing for the upper and lower elements
        overallPanel.add(overallLabel);
        overallPanel.add(Box.createVerticalStrut(10));
        overallPanel.add(overallEmoji);
        overallPanel.add(Box.createVerticalStrut(10));
        overallPanel.add(backOverall);

        // Making sure the panel doesn't appear right away
        overallPanel.setVisible(false);




        // This is the day panel
        dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));
        dayPanel.setBounds(0, 0, 400, 400);


        // Label for the rating: currently ___ out of 5 stars
        JLabel dayLabel = new JLabel();

        // Formatting the day label
        dayLabel.setFont(new Font(FONT, Font.PLAIN, 36));
        dayLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Emoji used for rating
        JLabel dayEmoji = new JLabel("😐");

        //Formating the Emoji
        dayEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        dayEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Setting the formatting for the emoji
        dayEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 46));

        // Creating the back button
        JButton backDay = new JButton("Back");

        // Formatting the back button
        backDay.setFont(new Font(FONT, Font.PLAIN, 20));

        // Create another getter method to get the days as a list of strings
        // Placeholder creating the values for the dropdown
        String[] days = {"1.0","2.0","3.0","4.0"};

        // Creates the dropdown
        JComboBox cb = new JComboBox(days);

        // Formats the dropdown
        cb.setMaximumSize(new Dimension(100, 35));
        cb.setAlignmentX(Component.RIGHT_ALIGNMENT);

        // Formats the dropdown and the back button on the same line
        JPanel inLine = new JPanel();
        inLine.setLayout(new BoxLayout(inLine, BoxLayout.X_AXIS));
        inLine.add(cb);
        inLine.add(backDay);



        // Making sure the dropdown is visible in the dayPanel
        cb.setVisible(true);



        // Adds all elements to the dayPanel
        // With vertical strut providing the spacing for the between the elements vertical
        dayPanel.add(dayLabel);
        dayPanel.add(Box.createVerticalStrut(10));
        dayPanel.add(dayEmoji);
        dayPanel.add(Box.createVerticalStrut(10));
        dayPanel.add(inLine);



        // Making the panel not visible until its selected
        dayPanel.setVisible(false);



        // Adding all panels to the container so it can handle how to display them
        container.add(menuPanel);
        container.add(overallPanel);
        container.add(dayPanel);



        // Applying the overall reviews
        // To the overall reviews panel
        overallButton.addActionListener(e -> {
            menuPanel.setVisible(false);
            int rating = 4;
            //ReviewOutputData output = controller.getOverallReview();
            overallLabel.setText("Currently " + rating + " out of 5 stars ");
            overallEmoji.setText("\uD83D\uDE22");
            // overallEmoji.setText(output.getEmoji());
            overallPanel.setVisible(true);

        });

        // Applying the day
        dayButton.addActionListener(e -> {
            menuPanel.setVisible(false);
            int rating = 3;
            //int selectedDay = Integer.parseInt((String) cb.getSelectedItem());
            //ReviewOutputData output = controller.getDayReview(selectedDay);
            dayLabel.setText("Currently " + rating + " out of 5 stars");
            dayEmoji.setText("\uD83D\uDE02");
            // overallEmoji.setText(output.getEmoji());
            dayPanel.setVisible(true);
        });

        // Implementing auto update once the user switches day
        /**

         cb.addActionListener( e -> {
         if (cb.getSelectedItem() != null){

         int selectedDay = Integer.parse((String) cb.selectedItem());
         ReviewOutputData output = controller.getReview(selectedDay);
         dayLabel.setText("Currently " + output.getRating() + " out of 5 stars");
         dayEmoji.setText(output.getEmoji());
         {
         )};
         */

        // Implementing the back button for overall
        backOverall.addActionListener(e -> {
            overallPanel.setVisible(false);
            menuPanel.setVisible(true);

        });

        // Implementing the back button for day
        backDay.addActionListener(e -> {
            dayPanel.setVisible(false);
            menuPanel.setVisible(true);

        });

        frame.setLocationRelativeTo(null);

        frame.setVisible(true);


    }



    public static void main(String[] args) {
        new ReviewView();
    }

}
