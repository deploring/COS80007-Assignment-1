package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.UIState;
import au.edu.swin.ajass.models.Student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This View is responsible for generating the data the
 * Student model needs to generate a PIN and persist in
 * the software. The data generated in this View is used
 * in the next view, LoginView.
 *
 * @author Bradley Chick
 * @author Joshua Skinner
 * @version 1
 * @see LoginView
 * @see Student
 * @since 0.1
 */
public class PINCreationView extends JPanel implements IView {

    // Reference back to JFrame, ExamController
    private final MainView main;

    // UI Elements
    private final JTextField studentID;
    private final JTextField schoolName;
    private final JTextField output;
    private final JButton submit;
    private final JButton next;

    public PINCreationView(MainView main) {
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

        schoolName = new JTextField(10);
        schoolName.setToolTipText("Your School's Name");
        JLabel schoolNameLabel = new JLabel("School Name: ");

        JPanel schoolNamePanel = new JPanel();
        schoolNamePanel.setLayout(new BorderLayout());
        schoolNamePanel.add(schoolName, BorderLayout.CENTER);
        schoolNamePanel.add(schoolNameLabel, BorderLayout.WEST);

        output = new JTextField("XXXX", 4);
        output.setEnabled(false);
        output.setEditable(false);
        output.setToolTipText("Your Generated PIN");
        JLabel outputLabel = new JLabel("Your Temporary PIN: ");

        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(output, BorderLayout.CENTER);
        outputPanel.add(outputLabel, BorderLayout.WEST);

        submit = new JButton("Generate Login PIN");
        submit.setEnabled(false);
        submit.setToolTipText("Enter your information to get your login PIN");

        JButton reset = new JButton("Reset");
        reset.setToolTipText("Reset Form");

        next = new JButton("Login");
        next.setEnabled(false);
        next.setToolTipText("Proceed to Login");


        // Event listeners
        FormInputListener formInputListener = new FormInputListener();
        studentID.addKeyListener(formInputListener);
        schoolName.addKeyListener(formInputListener);

        submit.addActionListener(e -> {
            // Generate result using Controller, store in Model, show to user!
            Student result = main.exam().registerStudentInfo(studentID.getText(), schoolName.getText());
            output.setText(result.getLoginPin());

            // Disable appropriate tools unless form is reset.
            studentID.setEnabled(false);
            schoolName.setEnabled(false);
            submit.setEnabled(false);
            next.setEnabled(true);

            // Show an important message.
            JOptionPane.showMessageDialog(null, "Your PIN has been generated.\nIt will expire in 20 minutes.\nDo NOT share this PIN with anyone.", "PIN Generated", JOptionPane.INFORMATION_MESSAGE);
        });

        reset.addActionListener(e -> reset());
        next.addActionListener(e -> main.update(UIState.LOGIN));

        // Layout
        //TODO: Layout this
        add(studentIDPanel);
        add(schoolNamePanel);
        add(outputPanel);
        add(submit);
        add(next);
        add(reset);
    }

    /**
     * Serves the express purpose of enabling the generate PIN
     * button if inputs are valid and a PIN has not already been
     * generated (if the next button is enabled).
     */
    private class FormInputListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // TextField's getText() does not update until after the keyPressed() event fires. We must wait for AWT.
            SwingUtilities.invokeLater(() -> {
                if (!next.isEnabled())
                    submit.setEnabled(studentID.getText().length() >= 5 && schoolName.getText().length() >= 5);
            });
        }
    }

    /**
     * Resets the relevant form elements to their original state.
     * Also clears the stored student information.
     */
    private void reset() {
        schoolName.setText("");
        schoolName.setEnabled(true);
        studentID.setText("");
        studentID.setEnabled(true);
        output.setText("XXXX");
        submit.setEnabled(false);
        next.setEnabled(false);
        main.exam().clearStudentInfo();
    }

    @Override
    public void onDisplay() {
        // Reset the elements if the user comes back to this view.
        reset();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
