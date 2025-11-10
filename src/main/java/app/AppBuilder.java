package app;

import javax.swing.*;

public class AppBuilder {
    private final JPanel cardPanel = new JPanel();

    public JFrame build() {
        final JFrame application = new JFrame("Sizzle and Serve");
        application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        application.add(cardPanel);

        return application;
    }
}
