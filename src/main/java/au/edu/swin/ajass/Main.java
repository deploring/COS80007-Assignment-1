package au.edu.swin.ajass;

import au.edu.swin.ajass.views.MainView;

import javax.swing.*;

/**
 * Serves the express purpose of instantiating the Main View.
 *
 * @author Joshua Skinner
 * @version 1
 * @see MainView
 * @since 0.1
 */
public class Main {

    public static void main(String[] args) {
        MainView displayFrame = new MainView();
        displayFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        displayFrame.setSize(800, 600);
        displayFrame.setResizable(false);
        displayFrame.setVisible(true);
    }
}
