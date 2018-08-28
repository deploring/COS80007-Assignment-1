package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.util.LinkedList;

/**
 * This class can represent any type of test based on
 * what question type is given to it at instantiation.
 */
public class Test {

    private LinkedList<Question> questions;
    private QuestionType category;
    private Difficulty currentDifficulty;
    private volatile int elapsed;
    private Thread timer;

    public Test(QuestionType category) {
        this.category = category;
        currentDifficulty = Difficulty.MEDIUM;
    }

    /**
     * @return How much time, in seconds, this thread has been active for.
     * Synchronized guarantees thread-safety.
     */
    public synchronized int getTimeElapsed() {
        return elapsed;
    }

    /**
     * @return The type of questions this test will accept.
     */
    public QuestionType getCategory() {
        return category;
    }

    /**
     * @return Current level of question difficulty.
     */
    public Difficulty getCurrentDifficulty() {
        return currentDifficulty;
    }

    /**
     * Registers a new question with this test, given it is of the correct category.
     *
     * @param question Question from the QuestionBank.
     * @see QuestionBank
     */
    public void newQuestion(Question question) {
        if (question.getType() == category) throw new IllegalArgumentException("Question category does not match test");
        questions.addLast(question);
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

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                synchronized ((Integer) elapsed) {
                    elapsed++;
                }
            } catch (InterruptedException e) {
                // This will never happen in normal operation.
            } finally {
                // Continue running this thread until instructed to stop.
                run();
            }
        }
    }
}
