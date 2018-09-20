package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
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
public final class Exam {

    private final LinkedList<Test> tests;
    private volatile int timeRemaining;
    private Student student;
    private double totalMarksEarnt;

    // The maximum amount of marks that can be obtained in the exam.
    public static int MAXIMUM_POSSIBLE_MARKS = 0;

    public Exam(int totalTime) {
        this.tests = new LinkedList<>();
        this.timeRemaining = totalTime;

        // Calculate maximum possible marks in an exam.
        for (QuestionType category : QuestionType.values())
            // Subtract 1, because tests start on medium, not hard.
            MAXIMUM_POSSIBLE_MARKS += category.getMaxQuestions() - 1;
        MAXIMUM_POSSIBLE_MARKS *= Difficulty.HARD.getMarks();
        // Factor in at least one medium question before all-hard all-correct questions.
        MAXIMUM_POSSIBLE_MARKS += (Difficulty.MEDIUM.getMarks() * QuestionType.values().length);
    }

    /**
     * This method is called upon by the controller every
     * second to keep track of how much time is remaining.
     *
     * @return Time remaining, in seconds.
     */
    public int decrementTime() {
        synchronized ((Integer) timeRemaining) {
            return timeRemaining--;
        }
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
     * @return Number of tests taken.
     */
    public int getNumberOfTestsTaken() {
        return tests.size();
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
     * Makes an old test the current test, if it is being repeated.
     *
     * @param oldTest An old, unfinished test.
     */
    public void bringOldTestForward(Test oldTest) {
        if(!tests.contains(oldTest))
            throw new IllegalArgumentException("Old test was not found in set of tests?");
        tests.remove(oldTest);
        tests.add(oldTest);
    }

    /**
     * Registers and stores student information with the exam.
     *
     * @param student Student model.
     */
    public void registerStudent(Student student) {
        this.student = student;
    }

    /**
     * @return Stored student information.
     */
    public Student getStudent() {
        return student;
    }

    /**
     * Adds accrued marks onto the exam total.
     *
     * @param total Marks earnt.
     */
    public void accrueMarks(double total) {
        this.totalMarksEarnt += total;
        getCurrentTest().accrueMarks(total);
    }

    /**
     * @return The total amount of marks that have been earnt so far.
     */
    public double getTotalMarks() {
        return totalMarksEarnt;
    }
}
