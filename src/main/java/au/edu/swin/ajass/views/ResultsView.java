package au.edu.swin.ajass.views;

import javax.swing.*;

/**
 * Created by sky on 21/8/18.
 */
public class ResultsView extends JPanel implements IView {

    private final MainView main;

    public ResultsView(MainView main){
        this.main= main;

        add(new JLabel("results!"));
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
