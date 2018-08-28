package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.QuestionType;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Exam represents the state of the Adaptive Test
 * overall, since it is a collection of multiple
 * test categories under an overall time limit.
 *
 * @author Joshua Skinner,
 * @version 1
 * @since 0.1
 */
public class Exam {

    private volatile int timeRemaining;
    private LinkedList<Test> tests;
    private Thread timer;

    public Exam(int totalTime) {
        this.tests = new LinkedList<>();
        this.timeRemaining = timeRemaining;

        // TODO: Call this when the first test is commenced.
        //new Thread(new ExamTimer()).run();
    }

    /**
     * @return Most recently added test to the Linked List of tests.
     * This is always the current test, as order is guaranteed.
     */
    public Test getCurrentTest() {
        return tests.getLast();
    }

    /**
     * @return An iterator object over the LinkedList of tests.
     */
    public Iterator<Test> getTests() {
        return tests.iterator();
    }

    /**
     * Creates a new test of a given category, stores it, and
     * then puts it at the end of the Linked List. This is now
     * the most recent test and should be displayed by the view.
     *
     * @param category Test category of choice.
     * @see Test
     */
    public Test newTest(QuestionType category) {
        Test result = new Test(category);
        tests.addLast(result);
        return result;
    }

    /**
     * This timer decrements the amount of
     * time remaining for the whole exam.
     *
     * @author Joshua Skinner
     * @version 1
     * @since 0.1
     */
    private class ExamTimer implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                synchronized ((Integer) timeRemaining) {
                    timeRemaining--;
                }
            } catch (InterruptedException e) {
                // This will never happen in normal operation.
            } finally {
                // Continue running this thread until instructed to stop.
                //TODO: Will infinitely call itself. It should terminate the exam when time reaches zero.
                run();
            }
        }
    }
}
