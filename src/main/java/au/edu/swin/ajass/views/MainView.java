package au.edu.swin.ajass.views;


import javax.swing.*;
import java.awt.*;

/**
 * This view is an extension of JFrame and serves the
 * express purpose of allowing other, more significant
 * Containers to attach on to. These containers include:
 * T&C View, Login View, and the Exam View.
 *
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public class MainView extends JFrame {


    private final TCView terms;
    private final PINCreationView pinCreation;
    private final LoginView login;
    private final ExamView exam;

    public MainView() {
        super("Advanced Java Assignment One - Adaptive Test");
        setLayout(new BorderLayout());

        terms = new TCView();
        pinCreation = new PINCreationView();
        login = new LoginView();
        exam = new ExamView();

        add(terms, BorderLayout.CENTER);
    }
}
