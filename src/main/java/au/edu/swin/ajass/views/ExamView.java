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

        JButton test1 = new JButton("lel");
        JButton test2 = new JButton("lel");

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 1;
        c.gridheight = 1;
        c.gridwidth = 1;
        add(test1, c);

        c.gridy = 1;
        c.gridheight = 9;
        add(test2, c);

    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
