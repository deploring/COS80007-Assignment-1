package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Test;
import au.edu.swin.ajass.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This view is responsible for creating, displaying, and listening
 * to the buttons that the user will select to begin tests. Actual
 * test presentation is handled by TestView. ExamView mediates the
 * interactions between MainView and its two sub-views, including
 * this one.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @see ExamView
 * @see TestView
 * @since 0.1
 */
public class ExamNavbarView extends JPanel implements IView {

    // Reference back to JFrame, upper View.
    private final MainView main;
    private final ExamView exam;

    private final HashMap<QuestionType, JButton> categories;

    // Other buttons.
    private final JButton extraListeningTime;
    private final JButton finishExam;

    public ExamNavbarView(MainView main, ExamView exam) {
        this.main = main;
        this.exam = exam;
        setLayout(new FlowLayout());

        categories = new HashMap<>();

        categories.put(QuestionType.MATHS, new JButton("Maths Test"));
        categories.put(QuestionType.LISTENING, new JButton("Listening Test"));
        categories.put(QuestionType.IMAGE, new JButton("Image Recognition Test"));
        categories.put(QuestionType.SPELLING, new JButton("Spelling Test"));
        categories.put(QuestionType.WRITING, new JButton("Writing Test"));

        NavbarButtonListener listener = new NavbarButtonListener();

        for (JButton toAdd : categories.values()) {
            toAdd.addActionListener(listener);
            toAdd.setToolTipText("Not Completed");
            add(toAdd);
        }

        // Misc buttons
        extraListeningTime = new JButton("Listening Test Resit");
        finishExam = new JButton("Finish Exam");

        extraListeningTime.setEnabled(false);
        extraListeningTime.setVisible(false);
        finishExam.setEnabled(false);

        extraListeningTime.addActionListener((e) -> {
            // Even though buttons are disabled, check if previous test has finished.
            if (!main.exam().canBeginTest()) return;

            int result = JOptionPane.showConfirmDialog(null, "Warning: Repeating the Listening test will give you an additional\n120 seconds to answer any questions you did not answer.\n5 marks from your total will be deducted if you choose to re-sit.", "Repeat Listening Test", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (result == JOptionPane.YES_OPTION) {
                // Disable all test buttons.
                for (JButton toDisable : categories.values())
                    toDisable.setEnabled(false);
                finishExam.setEnabled(false);
                extraListeningTime.setEnabled(false);
                extraListeningTime.setVisible(false);

                exam.repeatTest();
                categories.get(QuestionType.LISTENING).setToolTipText("In Progress");
            }
        });

        finishExam.addActionListener((e) -> finishExam());

        add(extraListeningTime);
        add(finishExam);
    }

    /**
     * Called when the exam is finished, either forcibly or once the time runs out.
     */
    private void finishExam() {
        exam.onExamFinish();
        removeAll();
        JLabel title = new JLabel("You have finished the exam. Congratulations!");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        add(title);
        repaint();
        revalidate();
    }

    /**
     * Starts a new test of a specific category.
     * Notifies Exam View, which begins the test.
     *
     * @param category The test category.
     */
    private void startNewTest(QuestionType category) {
        // Even though buttons are disabled, check if previous test has finished.
        if (!main.exam().canBeginTest()) return;

        // Disable all test buttons.
        for (JButton toDisable : categories.values())
            toDisable.setEnabled(false);
        finishExam.setEnabled(false);
        extraListeningTime.setEnabled(false);
        extraListeningTime.setVisible(false);

        categories.get(category).setToolTipText("In Progress");

        exam.startNewTest(category);
    }

    /**
     * Re-enables the test buttons after a test has been taken.
     * Tests that have already been taken will not re-enable.
     */
    private void reEnableTestSelection() {
        // Check each button category to see if it should be re-enabled.
        for (QuestionType type : QuestionType.values()) {
            Iterator<Test> tests = main.exam().getExamModel().getTests();
            boolean enable = true;
            while (tests.hasNext()) {
                // This test has already been taken, keep disabled.
                if (tests.next().getCategory().equals(type)) enable = false;
            }
            categories.get(type).setEnabled(enable);
        }
    }

    /**
     * Do routines that need to be done when a test has finished.
     */
    public void onFinishTest() {
        // User can finish exam at any time, or when time runs out.
        finishExam.setEnabled(true);
        // Repeat should be available even if all tests have been completed.
        boolean canRepeat = main.exam().canRepeatTest();
        extraListeningTime.setEnabled(canRepeat);
        extraListeningTime.setVisible(canRepeat);

        // Don't do anything if the user has finished all the tests.
        if (main.exam().getExamModel().getNumberOfTestsTaken() == QuestionType.values().length) return;

        Test oldTest = main.exam().getExamModel().getCurrentTest();
        categories.get(oldTest.getCategory()).setToolTipText(String.format("Completed! Time Taken: %s", Utilities.digitalTime(oldTest.getTimeElapsed())));

        reEnableTestSelection();
    }

    /**
     * Listens for button clicks on the test areas.
     * Starts tests if a test is available to start.
     */
    private class NavbarButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // We can safely assume JButton is the source.
            JButton source = (JButton) e.getSource();

            // Check through each value in the map and start the appropriate test.
            for (Map.Entry<QuestionType, JButton> entry : categories.entrySet())
                if (entry.getValue().equals(source))
                    startNewTest(entry.getKey());
        }
    }

    /**
     * Updates the exam time remaining display.
     *
     * @param newTime New amount of exam seconds remaining.
     */
    private void updateTimeDisplay(int newTime) {
        // Turn global timer into red font if there is less than 30 seconds remaining.
        SwingUtilities.invokeLater(() -> main.globalTimer.setText(String.format("Exam Time Remaining: %s", Utilities.digitalTime(newTime))));
        // Play a warning message if 30 seconds remain.
        if (newTime == 30)
            Utilities.playSound("warningexamtime.mp3");
        if (newTime == 3)
            Utilities.playSound("metronome.mp3");
    }

    /**
     * This timer tells the ExamController to
     * 'tick' once a second, performing duties
     * such as decrementing the timer and ending
     * the exam prematurely if needed.
     *
     * @author Joshua Skinner
     * @version 1
     * @since 0.1
     */
    private class ExamTimer implements Runnable {

        // Force the recursive loop to stop once interrupted.
        boolean interrupted = false;

        @Override
        public void run() {
            try {
                // Do a tick. Update display.
                int tick = main.exam().tickExam();
                updateTimeDisplay(tick);
                if (tick < 0) return;
                // Continue running this thread until instructed to stop.
                Thread.sleep(1000);
                if (!interrupted)
                    run();
            } catch (InterruptedException e) {
                // Perform actions that need to be performed once the exam flow stops.
                interrupted = true;
                finishExam();
            }
        }
    }

    @Override
    public void onDisplay() {
        main.exam().beginExam(new ExamTimer());
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
