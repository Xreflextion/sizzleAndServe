package view;

import app.AppBuilder;
import interface_adapter.ViewManagerModel;
import interface_adapter.office.OfficeViewModel;
import interface_adapter.review.ReviewController;
import interface_adapter.ViewModel;
import interface_adapter.review.ReviewController;
import interface_adapter.review.ReviewState;
import interface_adapter.review.ReviewViewModel;
import use_case.review.ReviewOutputData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;


public class ReviewView extends JPanel implements ActionListener, PropertyChangeListener {

    // Constants for UI formatting
    private static final String FONT = "Arial";
    private static final int TITLE_FONT_SIZE = 28;
    private static final int SUBTITLE_FONT_SIZE = 22;
    private static final int BUTTON_FONT_SIZE = 20;
    private static final int LABEL_FONT_SIZE = 32;
    private static final int EMOJI_FONT_SIZE = 48;


    // Panel names for CardLayout
    private static final String MENU_PANEL = "menu";
    private static final String OVERALL_PANEL = "overall";
    private static final String DAY_PANEL = "day";


    private final interface_adapter.review.ReviewController controller;
    private final ReviewViewModel viewModel;
    private final ViewManagerModel viewManagerModel;

    // UI components
    private JButton overallButton;
    private JButton dayButton;
    private JButton backOverall;
    private JButton backDay;
    private JComboBox<String> daySelect;
    private JLabel dayEmoji;
    private JLabel dayLabel;
    private JLabel overallEmoji;
    private JLabel overallLabel;
    private JButton backToOfficeButton;



    public ReviewView(interface_adapter.review.ReviewController controller, ReviewViewModel viewModel,
                      ViewManagerModel viewManagerModel) {
        this.controller = controller;
        this.viewModel = viewModel;
        this.viewManagerModel = viewManagerModel;

        this.viewModel.addPropertyChangeListener(this);

        // uses card layout to easily swap between the panels
        setLayout(new CardLayout());
        buildGUI();
    }


    private void buildGUI(){
        JPanel dayPanel;
        JPanel overallPanel;

        // Creates the panel for the menu
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));

        // Sets the title for the panel the shows the options for reviews and formats it
        JLabel title = new JLabel("Office - Reviews");
        title.setFont(new Font(FONT, Font.BOLD, TITLE_FONT_SIZE));
        title.setAlignmentX(CENTER_ALIGNMENT);

        // Sets the subtitle for select an option
        JLabel subtitle = new JLabel("Select an option:");
        subtitle.setFont(new Font(FONT, Font.PLAIN, SUBTITLE_FONT_SIZE));
        subtitle.setAlignmentX(CENTER_ALIGNMENT);

        // Button to select an overall review for restaurant
        overallButton = new JButton("Overall");
        overallButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        overallButton.setFont(new Font(FONT, Font.PLAIN, BUTTON_FONT_SIZE));
        overallButton.addActionListener(this);

        // Day Button
        dayButton = new JButton("Day");
        dayButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        dayButton.setFont(new Font(FONT, Font.PLAIN, BUTTON_FONT_SIZE));
        dayButton.addActionListener(this);

        // Adding the back to office button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backToOfficeButton = new JButton("Back to Office");
        backToOfficeButton.setFont(new Font(FONT, Font.PLAIN, BUTTON_FONT_SIZE));
        backToOfficeButton.addActionListener(this);
        bottomPanel.add(backToOfficeButton);

        // Provides spacing for the menuPanel
        menuPanel.add(Box.createVerticalStrut(30));
        menuPanel.add(title);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(subtitle);
        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(overallButton);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(dayButton);
        menuPanel.add(bottomPanel);


        // Creating the overall panel
        overallPanel = new JPanel();
        overallPanel.setLayout(new BoxLayout(overallPanel, BoxLayout.Y_AXIS));

        // Creating the label for the overall review with formatting
        overallLabel = new JLabel("Loading...");
        overallLabel.setFont(new Font(FONT, Font.PLAIN, LABEL_FONT_SIZE));
        overallLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Creating and formating the overall emoji
        overallEmoji = new JLabel();
        overallEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, EMOJI_FONT_SIZE));
        overallEmoji.setAlignmentX(CENTER_ALIGNMENT);

        // Creating the back button
        backOverall = new JButton("Back");

        // Formatting the back button
        backOverall.setFont(new Font(FONT, Font.PLAIN, BUTTON_FONT_SIZE));
        backOverall.setAlignmentX(Component.CENTER_ALIGNMENT);
        backOverall.addActionListener(this);

        // Setting the spacing for the overall panel
        overallPanel.add(Box.createVerticalStrut(20));
        overallPanel.add(overallLabel);
        overallPanel.add(Box.createVerticalStrut(10));
        overallPanel.add(overallEmoji);
        overallPanel.add(Box.createVerticalStrut(10));
        overallPanel.add(backOverall);



        // Creating the day panel
        dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));

        // Creating the day label and formatting it
        dayLabel = new JLabel("Loading...");
        dayLabel.setFont(new Font(FONT, Font.PLAIN, LABEL_FONT_SIZE));
        dayLabel.setAlignmentX(CENTER_ALIGNMENT);

        // Emoji used for rating
        dayEmoji = new JLabel();

        //Formating the Emoji
        dayEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, EMOJI_FONT_SIZE));
        dayEmoji.setAlignmentX(Component.CENTER_ALIGNMENT);

        // formatting the dropdown
        daySelect = new JComboBox<>();
        daySelect.setMaximumSize(new Dimension(100, 30));
        // This listener changes the day for the average review
        daySelect.addActionListener(e -> {
            if (daySelect.getSelectedItem() != null) {
                int day = Integer.parseInt((String) daySelect.getSelectedItem());
                controller.getReview(day);
            }
        });

        // Creating the back button
        backDay = new JButton("Back");

        // Formatting the back button
        backDay.setFont(new Font(FONT, Font.PLAIN, BUTTON_FONT_SIZE));
        backDay.addActionListener(this);

        // Formats the back button and dropdown so they inline with each-other
        JPanel inLine = new JPanel();
        inLine.setLayout(new BoxLayout(inLine, BoxLayout.X_AXIS));
        inLine.add(daySelect);
        inLine.add(Box.createHorizontalStrut(10));
        inLine.add(backDay);

        dayPanel = new JPanel();
        dayPanel.setLayout(new BoxLayout(dayPanel, BoxLayout.Y_AXIS));

        // Formats the day panel to have vertical spacing
        dayPanel.add(Box.createVerticalStrut(20));
        dayPanel.add(dayLabel);
        dayPanel.add(Box.createVerticalStrut(10));
        dayPanel.add(dayEmoji);
        dayPanel.add(Box.createVerticalStrut(10));
        dayPanel.add(inLine);



        // adds the panels to the card layout
        add(menuPanel, MENU_PANEL);
        add(overallPanel, OVERALL_PANEL);
        add(dayPanel, DAY_PANEL);


    }


    @Override
    public void actionPerformed(ActionEvent e) {

        CardLayout cl = (CardLayout) this.getLayout();

        // Makes the overall button work
        if(e.getSource() == overallButton){
            controller.getReview(null);
            cl.show(this, OVERALL_PANEL);

        }

        // Makes the day button work
        if (e.getSource() == dayButton) {
            ArrayList<Integer> availableDays = (ArrayList<Integer>) controller.getAvailableDays();
            if(availableDays.isEmpty()){
                // placeholder
                availableDays.add(0);
            }
            daySelect.removeAllItems();
            for(int d: availableDays){
                daySelect.addItem(String.valueOf(d));
            }

            cl.show(this, DAY_PANEL);
            controller.getReview(availableDays.get(0));
        }

        // Makes the back buttons work
        if (e.getSource() == backOverall || e.getSource() == backDay) {
            cl.show(this, MENU_PANEL);
        }

        // Makes the back to office button work
        if (e.getSource() == backToOfficeButton) {
            viewManagerModel.setState(OfficeViewModel.VIEW_NAME);
            viewManagerModel.firePropertyChange();
        }

    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        ReviewState state = viewModel.getState();
        if (state == null) return;

        // Use the same data for whichever panel was just updated
        String text = "Currently " + state.getRating() + " out of 5 stars";
        String emoji = state.getEmoji();

        overallLabel.setText(text);
        overallEmoji.setText(emoji);

        dayLabel.setText(text);
        dayEmoji.setText(emoji);
    }



}