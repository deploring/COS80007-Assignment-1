package au.edu.swin.ajass;

import au.edu.swin.ajass.views.MainView;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;
import java.awt.*;

/**
 * Serves the express purpose of instantiating the Main View.
 *
 * @author Joshua Skinner
 * @version 1
 * @see MainView
 * @since 0.1
 */
public class Main extends Application {

    public static void main(String[] args) {
        MainView displayFrame = new MainView();
        // Set JFrame attributes
        displayFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        displayFrame.setSize(650, 500);
        displayFrame.setResizable(false);
        displayFrame.setVisible(true);
        // Set the frame to display in the center of the screen.
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        displayFrame.setLocation(dim.width / 2 - displayFrame.getSize().width / 2, dim.height / 2 - displayFrame.getSize().height / 2);

        // JavaFX media player will not work unless the Application is "launched".
        Application.launch();
    }

    public void start(Stage primaryStage) throws Exception {
        // Unused.
    }
}
