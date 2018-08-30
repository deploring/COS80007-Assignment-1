package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.util.LinkedList;

/**
 * This class can represent any type of test based on
 * what question type is given to it at instantiation.
 * Tests are responsible for keeping track of questions,
 * time, difficulty, and category.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @see Question
 * @since 0.1
 */
public final class Test {

    private final LinkedList<Question> questions;
    private final QuestionType category;
    private final Thread timer;
    private Difficulty currentDifficulty;
    private volatile int elapsed;
    private boolean finishedEarly;

    public Test(QuestionType category) {
        this.category = category;
        questions = new LinkedList<>();
        currentDifficulty = Difficulty.MEDIUM;
        timer = new Thread(new TestTimer());
        finishedEarly = false;
    }

    /**
     * A test is active if there are questions remaining
     * or if the final question has not been answered.
     * It is always inactive if the user exited the test early.
     *
     * @return Whether test is active.
     */
    public boolean isActive() {
        return finishedEarly || (questions.size() < getMaxQuestions() || !questions.getLast().isAnswered());
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
     * @return The most recently issued/unanswered (current) question.
     */
    public Question getCurrentQuestion() {
        return questions.getLast();
    }

    /**
     * @return Number of questions this test receives before terminating.
     */
    public int getMaxQuestions() {
        return category.getMaxQuestions();
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
                // Continue running this thread until interrupted.
                run();
            } catch (InterruptedException e) {
                // The thread should be interrupted once the test is no longer active.
            }
        }
    }
}
