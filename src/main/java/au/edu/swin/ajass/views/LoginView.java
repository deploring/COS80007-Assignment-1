package au.edu.swin.ajass.views;

import au.edu.swin.ajass.controllers.ExamController;
import au.edu.swin.ajass.enums.UIState;
import au.edu.swin.ajass.models.Student;
import au.edu.swin.ajass.util.JTextFieldLimiter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.TimeUnit;

/**
 * This view allows a user with a generated ID and
 * PIN number to log into the exam and begin their
 * testing. The user can go back if they need to
 * generate another PIN.
 *
 * @author Bradley Chick
 * @author Joshua Skinner
 * @version 1.0
 * @since 0.1
 */
public class LoginView extends JPanel implements IView {

    // Reference back to JFrame, ExamController
    private final MainView main;

    // UI Elements
    private final JTextField studentID;
    private final JPasswordField PIN;
    private final JButton start;
    private final JButton back;

    LoginView(MainView main) {
        this.main = main;
        setLayout(new FlowLayout());

        // Elements
        studentID = new JTextField(10);
        studentID.setToolTipText("Your Student ID");
        JLabel studentIDLabel = new JLabel("Student ID: ");

        JPanel studentIDPanel = new JPanel();
        studentIDPanel.setLayout(new BorderLayout());
        studentIDPanel.add(studentID, BorderLayout.CENTER);
        studentIDPanel.add(studentIDLabel, BorderLayout.WEST);

        PIN = new JPasswordField(4);
        PIN.setToolTipText("Your School's Name");
        PIN.setDocument(new JTextFieldLimiter(4));
        JLabel PINLabel = new JLabel("Your Temporary PIN: ");

        JPanel PINPanel = new JPanel();
        PINPanel.setLayout(new BorderLayout());
        PINPanel.add(PIN, BorderLayout.CENTER);
        PINPanel.add(PINLabel, BorderLayout.WEST);

        start = new JButton("Login & Start Exam");
        start.setEnabled(false);

        back = new JButton("Go Back");

        // Event listeners
        back.addActionListener(e -> main.update(UIState.PINGEN));

        FormInputListener formInputListener = new FormInputListener();
        studentID.addKeyListener(formInputListener);
        PIN.addKeyListener(formInputListener);

        start.addActionListener(e -> {
            Student student = main.exam().getStudentInfo();

            // Check login details against stored details.
            if (student.getStudentID().equals(studentID.getText()) && student.getLoginPin().equals(new String(PIN.getPassword()))) {
                // Calculate required numbers.
                long minuteDifference = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - student.getTimeCreated());
                long timeRemaining = TimeUnit.SECONDS.toMinutes(ExamController.EXAM_TIME);

                // Check if PIN has expired.
                if (minuteDifference >= 20)
                    JOptionPane.showMessageDialog(null, "Unfortunately, your temporary PIN has expired.\nPlease go back and re-generate your PIN.", "PIN Expired", JOptionPane.ERROR_MESSAGE);
                else {
                    // Give them one final warning before they go in.
                    int result = JOptionPane.showConfirmDialog(null, String.format("You are about to enter and start the exam.\nYou have %d minutes to complete all the tests.\nIf not, your PIN expires in %d minute(s).", timeRemaining, 20 - minuteDifference), "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (result == JOptionPane.YES_OPTION)
                        main.update(UIState.EXAM);
                }
            } else
                JOptionPane.showMessageDialog(null, "Your login details were not correct.\nTry again.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        });

        // Layout
        //TODO: this
        add(studentIDPanel);
        add(PINPanel);
        add(start);
        add(back);
    }

    /**
     * Serves the express purpose of enabling the proceed button
     * if inputs are valid, though they may not successfully log
     * in the user.
     */
    private class FormInputListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // TextField's getText() does not update until after the keyPressed() event fires. We must wait for AWT.
            SwingUtilities.invokeLater(() -> start.setEnabled(studentID.getText().length() >= 5 && PIN.getPassword().length == 4));
        }
    }

    @Override
    public void onDisplay() {
        // The user may return to this page more than once, so reset relevant UI elements.
        studentID.setText("");
        PIN.setText("");
        start.setEnabled(false);
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
