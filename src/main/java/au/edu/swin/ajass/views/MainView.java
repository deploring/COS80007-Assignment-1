package au.edu.swin.ajass.views;

import au.edu.swin.ajass.models.ExamState;

import javax.swing.*;
import java.awt.*;

/**
 * This view is an extension of JFrame and serves the
 * express purpose of allowing other, more significant
 * Containers to attach on to. These containers include:
 * T&C View, Login View, and the Exam View.
 *
 * @author Joshua Skinner
 * @since 0.1
 * @version 1
 */
public class MainView extends JFrame {

    private final ExamState state;
    private final TCView terms;

    public MainView() {
        super("Advanced Java Assignment One - Adaptive Test");
        setLayout(new BorderLayout());

        terms = new TCView();

        add(terms, BorderLayout.CENTER);

        state = new ExamState(15 * 60);
    }
}
