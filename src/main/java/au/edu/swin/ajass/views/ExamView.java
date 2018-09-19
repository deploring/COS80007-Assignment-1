package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Test;
import au.edu.swin.ajass.util.Utilities;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sky on 21/8/18.
 */
public class ExamView extends JPanel implements IView {

    // Reference back to JFrame, other Views
    private final MainView main;

    // Sub-views contained in this view.
    private final IView examNavbar, test, results;

    public ExamView(MainView main) {
        this.main = main;
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        // Sub-views
        examNavbar = new ExamNavbarView(main, this);
        test = new TestView(main, this);
        results = new ResultsView(main);

        // Fill the panels to the very edges.
        c.fill = GridBagConstraints.BOTH;
        // Take up as little screen height as possible.
        c.weighty = 0.15;
        c.weightx = 1;
        add(examNavbar.getPanel(), c);

        c.gridy = 1;
        // Take rest of available screen height
        c.weighty = 1;
        add(test.getPanel(), c);
    }

    /**
     * Called by MainView when
     */
    public void onExamFinish(){
        // Remove old test panel.
        removeAll();

        // Instate new results panel.
        GridBagConstraints c = new GridBagConstraints();

        c.fill = GridBagConstraints.BOTH;
        c.weighty = 0.05;
        c.weightx = 1;
        add(examNavbar.getPanel(), c);


        c.gridy = 1;
        c.weighty = 1;
        add(results.getPanel(), c);
        results.onDisplay();
        revalidate();
        repaint();

        // Hide global timer.
        main.globalTimer.setVisible(false);
        testCompleted(true);
    }

    /**
     * Used by the Exam Navbar View to make Exam View
     * notify everything else that a new test is about
     * to be undertaken.
     *
     * @param category Category of the test.
     */
    public void startNewTest(QuestionType category) {
        main.exam().beginTest(category, newTestTimer());
        test.onDisplay();
    }

    /**
     * Used by the Exam Navbar View to repeat a test.
     * Specifically in this case, only the listening test.
     */
    public void repeatTest() {
        main.exam().repeatTest(newTestTimer());
        test.onDisplay();
    }

    /**
     * Called by TestView when a user submits their
     * answer to a question. It must be recorded and
     * then a new Question must be shown if possible.
     */
    public void finaliseQuestionResponse() {
        main.exam().finaliseAnswer();
        Test current = main.exam().getExamModel().getCurrentTest();
        if (current.isComplete())
            testCompleted(false);
        else {
            main.exam().nextQuestion();
            test.onDisplay();
        }
    }

    /**
     * Called by MainView when a user has answered
     * all of the questions in a test. Also called
     * by TestView when the "Done" button is pressed
     * to finish the test early. Stops the test and
     * makes a new one available to undertake.
     *
     * @param incomplete Was the test incomplete?
     */
    public void testCompleted(boolean incomplete) {
        ((ExamNavbarView) examNavbar).onFinishTest();
        ((TestView)test).displayFillerPanel();
        main.exam().finishTest(incomplete);
        // Make test timer invisible
        main.testTimer.setVisible(false);
    }

    @Override
    public void onDisplay() {
        examNavbar.onDisplay();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    /**
     * Updates the test time remaining display.
     *
     * @param newTime New amount of test seconds remaining.
     */
    private void updateTimeDisplay(int newTime) {
        // Turn global timer into red font if there is less than 30 seconds remaining.
        SwingUtilities.invokeLater(() -> main.testTimer.setText(String.format("Test Time Remaining: %s", Utilities.digitalTime(newTime))));
        // Play a warning message if 30 seconds remain.
        if (newTime == 30)
            Utilities.playSound("warningtesttime.mp3");
        if (newTime == 3)
            Utilities.playSound("metronome.mp3");
    }

    /**
     * @return A new instance of the test timer.
     */
    private Runnable newTestTimer(){
        return new TestTimer();
    }

    /**
     * This timer keeps track of how long this test
     * has been active for.
     *
     * @author Joshua Skinner
     * @version 1
     * @since 0.1
     */
     private class TestTimer implements Runnable {

        // Force the recursive loop to stop once interrupted.
        boolean interrupted = false;
        Test current;

        TestTimer() {
            SwingUtilities.invokeLater(() -> current = main.exam().getExamModel().getCurrentTest());
        }

        @Override
        public void run() {
            try {
                updateTimeDisplay(main.exam().tickTest());
                // Continue running this thread until interrupted.
                Thread.sleep(1000);
                if (!interrupted)
                    run();
            } catch (InterruptedException e) {
                // The thread should be interrupted once the test is no longer active.
                interrupted = true;
                // We can assume the test was incomplete.
                testCompleted(true);
            }
        }
    }
}
