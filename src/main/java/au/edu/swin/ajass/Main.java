package au.edu.swin.ajass;

import au.edu.swin.ajass.views.MainView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Serves the express purpose of instantiating the Main View.
 *
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 * @see MainView
 */
public class Main {

    public static void main(String[] args) {
        try {
        MainView displayFrame = new MainView();
        // Set JFrame attributes
        displayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        displayFrame.setSize(600, 450);
        displayFrame.setResizable(false);
        displayFrame.setVisible(true);
        // Get dimension of user's screen.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        // Set the frame to display in the center of the screen.
        displayFrame.setLocation(dim.width/2-displayFrame.getSize().width/2, dim.height/2-displayFrame.getSize().height/2);
        } catch (IOException e) {
            // *shrug*, something didn't load. This shouldn't happen so just print stack trace.
            e.printStackTrace();
        }
    }
}
