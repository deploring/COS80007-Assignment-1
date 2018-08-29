package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.QuestionType;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Exam represents the state of the Adaptive Test
 * overall, since it is a collection of multiple
 * test categories under an overall time limit.
 *
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public class Exam {

    private volatile int timeRemaining;
    private final LinkedList<Test> tests;
    private final Thread timer;

    private Student student;
    private double possibleMarks;
    private double totalMarks;

    public Exam(int totalTime) {
        this.tests = new LinkedList<>();
        this.timeRemaining = totalTime;

        timer = new Thread(new ExamTimer());
    }

    /**
     * Call this when the user enters their login and starts the exam.
     */
    public void start() {
        if (timer.isAlive() || timer.isInterrupted()) throw new IllegalStateException("Exam has already been started");
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
     * Adds accrued marks onto the exam total.
     *
     * @param total Total possible marks earnt.
     * @param earnt Actual marks earnt.
     */
    public void accrueMarks(double total, double earnt) {
        this.totalMarks += total;
        this.possibleMarks += earnt;
    }

    /**
     * @return The total amount of marks that are possible to get so far.
     */
    public double getPossibleMarks() {
        return possibleMarks;
    }

    /**
     * @return The total amount of marks that have been earnt so far.
     */
    public double getTotalMarks() {
        return totalMarks;
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
                // Continue running this thread until instructed to stop.
                run();
            } catch (InterruptedException e) {
                // The thread should stop once it is interrupted.
            }
        }
    }
}
