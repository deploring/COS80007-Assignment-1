package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.util.Iterator;
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
    private Difficulty currentDifficulty;
    private volatile int timeRemaining;
    private boolean finishedEarly;
    private boolean repeatable;
    private double marksEarnt;

    public int MAXIMUM_POSSIBLE_MARKS = 0;

    public Test(QuestionType category) {
        this.category = category;
        timeRemaining = category.getMaxTime();
        questions = new LinkedList<>();
        currentDifficulty = Difficulty.MEDIUM;
        finishedEarly = false;
        repeatable = category == QuestionType.LISTENING;

        // Calculate maximum possible marks for this test's category.
        MAXIMUM_POSSIBLE_MARKS += (category.getMaxQuestions() - 1) * Difficulty.HARD.getMarks();
        MAXIMUM_POSSIBLE_MARKS += Difficulty.MEDIUM.getMarks();
    }

    /**
     * A test is active if it has not been finished
     * early and it has not been completed.
     *
     * @return Whether test is active.
     */
    public boolean isActive() {
        return !finishedEarly && !isComplete();
    }

    /**
     * @return Whether the test has been fully completed.
     */
    public boolean isComplete() {
        return questions.size() == getMaxQuestions() && questions.getLast().isAnswered();
    }

    /**
     * Called when the test is repeated. Adds an additional 120 seconds.
     */
    public void repeat(){
        finishedEarly = false;
        repeatable = false;
        timeRemaining += 120;
    }

    /**
     * @return True if this test is repeatable.
     */
    public boolean isRepeatable(){
        return repeatable;
    }

    /**
     * Marks this test as having finished it, but not completing all the questions.
     * @param finishedEarly Whether this test is incomplete.
     */
    public void setFinishedEarly(boolean finishedEarly){
        this.finishedEarly = finishedEarly;
    }

    /**
     * This method is called upon by TestView's TestTimer every
     * second to keep track of how much time the user has before
     * the test will prematurely exit.
     *
     * @return Time remaining for this test.
     */
    public int decrementTime(){
        synchronized ((Integer) timeRemaining) {
            return timeRemaining--;
        }
    }

    /**
     * @return How much time, in seconds, this test has been active for.
     */
    public int getTimeElapsed() {
        synchronized ((Integer) timeRemaining) {
            return category.getMaxTime() - timeRemaining;
        }
    }

    /**
     * @return How many marks have been earnt from this individual test.
     */
    public double getMarksEarnt(){
        return marksEarnt;
    }

    /**
     * Adds marks from a correct question onto this test's total.
     *
     * @param marks Marks earnt from a correct question.
     */
    public void accrueMarks(double marks) {
        this.marksEarnt += marks;
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
     * Changes the Test's difficulty. (adaptive!)
     *
     * @param newDifficulty New difficulty level.
     */
    public void adjustDifficulty(Difficulty newDifficulty) {
        currentDifficulty = newDifficulty;
    }

    /**
     * @return An iterator object over the LinkedList of issued questions.
     */
    public Iterator<Question> getQuestions() {
        return questions.iterator();
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
     * @return Number of issued questions in the test so far.
     */
    public int getQuestionsIssued() {
        return questions.size();
    }

    /**
     * Registers a new question with this test, given it is of the correct category.
     *
     * @param question Question from the QuestionBank.
     * @see QuestionBank
     */
    public void newQuestion(Question question) {
        if (question.getType() != category)
            throw new IllegalArgumentException(String.format("Question category does not match test: %s vs %s", category, question.getType()));
        questions.addLast(question);
    }
}
