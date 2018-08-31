package au.edu.swin.ajass.views;


import au.edu.swin.ajass.controllers.ExamController;
import au.edu.swin.ajass.enums.UIState;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 * This view is an extension of JFrame and serves the
 * express purpose of allowing other, more significant
 * Containers to attach on to. These containers include:
 * <ul>
 * <li>T&C View</li>
 * <li>PinCreationView</li>
 * <li>LoginView</li>
 * <li>ExamView</li>
 * <li>ResultsView</li>
 * </ul>
 *
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public class MainView extends JFrame {

    // UI State
    private UIState state;

    // Hold individual view instances.
    private final MainView main;
    private final IView terms, pinCreation, login, exam;

    // Controller instances.
    private final ExamController examController;

    // Dialog strings.
    private final String aboutText;
    private final String authorsText;

    /**
     * @throws IOException Image may not exist or may not load properly.
     */
    public MainView() throws IOException {
        super("Advanced Java Assignment One - Adaptive Test");
        setLayout(new BorderLayout());

        // Load image icons.
        Icon authorHeart = new ImageIcon(ImageIO.read(MainView.class.getResourceAsStream("/heart.png")));
        Image programIcon =ImageIO.read(MainView.class.getResourceAsStream("/icon.png"));

        setIconImage(programIcon);

        // Hard-coded dialog texts.
        authorsText = "COS80007 Advanced Java: Assignment 1\n\"The Adaptive Test\"\n\nAuthors:\nJoshua Skinner (101601828)\nEmail: josh@rpg.solar\nWebsite: https://rpg.solar/\n\nBradley Chick (101626151)\nEmail: 101626151@student.swin.edu.au\nWebsite: None";
        aboutText = "";

        // Create view instances.
        main = this;
        terms = new TCView(this);
        pinCreation = new PINCreationView(this);
        login = new LoginView(this);
        exam = new ExamView(this);

        // Create controller instances.
        examController = new ExamController();

        // Create global menu elements.
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Help");
        // Make this menu item accessible through Alt + H
        menu.setMnemonic(KeyEvent.VK_H);
        menu.getAccessibleContext().setAccessibleDescription("A list of helpful items.");
        menuBar.add(menu);

        // Menu items.
        JMenuItem about = new JMenuItem("About");
        JMenuItem authors = new JMenuItem("Software Authors");
        JSeparator sep1 = new JSeparator();
        menu.add(about);
        menu.add(authors);
        menu.add(sep1);

        // Create menu action listeners and display.
        about.addActionListener(e -> JOptionPane.showMessageDialog(null, aboutText, "About This Program", JOptionPane.INFORMATION_MESSAGE, authorHeart));
        authors.addActionListener(e -> JOptionPane.showMessageDialog(null, authorsText, "About the Authors", JOptionPane.INFORMATION_MESSAGE, authorHeart));
        setJMenuBar(menuBar);

        // Land user on Terms & Conditions View
        update(UIState.TERMS);
    }

    /**
     * @return An instance of the ExamController so any View may pass
     * on user actions to it for the purpose of manipulating the models
     * in a desired way.
     */
    ExamController exam() {
        return examController;
    }

    /**
     * This method is called upon by views within MainView (JFrame)
     * when a new view needs to be displayed. An example of this being
     * when the state changes from UIState.TERMS to UIState.PINGEN when
     * the user has accepted the Terms and Conditions and submitted.
     *
     * @param state New UI State (what view to display)
     * @throws IllegalArgumentException State cannot change to the state MainView is already in
     * @see UIState
     */
    public void update(UIState state) {
        if (this.state == state) throw new IllegalArgumentException("Cannot to swap to view that is already displayed");
        else this.state = state;

        switch (this.state) {
            case TERMS:
                show(terms);
                break;
            case PINGEN:
                show(pinCreation);
                break;
            case LOGIN:
                show(login);
                break;
            case EXAM:
                show(exam);
                break;
            case RESULTS:
                show(null); //TODO Add this
                break;
            default:
                throw new IllegalArgumentException("New UI state not supported or null");
        }
    }

    /**
     * Shows a JPanel, or in this software's context, a view.
     * Views should be passed in through update(UIState).
     *
     * @param view The new View to display.
     * @see #update(UIState)
     * @see IView#onDisplay()
     */
    private void show(IView view) {
        // Remove existing view and add new view.
        getContentPane().removeAll();
        view.onDisplay();
        add(view.getPanel());
        // Re-paint the JFrame as stuff has been removed.
        repaint();
        // Re-layout the container.
        validate();
    }
}
