package au.edu.swin.ajass.views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sky on 21/8/18.
 */
public class ExamView extends JPanel implements IView {

    // Reference back to JFrame, other Views
    private final MainView main;

    private final ExamNavbarView examNavbar;
    private final TestView test;

    public ExamView(MainView main) {
        this.main = main;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Sub-views
        examNavbar = new ExamNavbarView();
        test = new TestView();

        // Fill the panels to the very edges.
        c.fill = GridBagConstraints.BOTH;
        // Take up to 10% of vertical screen height
        c.weighty = 0.1f;
        add(examNavbar, c);

        c.gridy = 1;
        // Take rest of available screen height
        c.weighty = 1;
        add(test, c);

    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
