package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

/**
 * A question that can appear on any test at any
 * given point. This superclass houses common
 * attributes between the different types of
 * questions.
 *
 * @author Bradley Chick
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public abstract class Question {

    private final QuestionType type;
    private final Difficulty difficulty;
    protected boolean correct;
    private boolean answered;
    private final String prompt;

    protected Question(QuestionType type, Difficulty difficulty, String prompt) {
        this.type = type;
        this.difficulty = difficulty;
        this.prompt = prompt;

        // By default, a new Question is not answered and not marked.
        this.answered = false;
        this.correct = false;
    }

    /**
     * A question will call upon its subclass to
     * check if, based on the input from the user,
     * the answer is correct. This should only be
     * called once the question is ready to be marked.
     */
    public abstract void checkAnswer();

    /**
     * @return The test category of this question.
     */
    public QuestionType getType() {
        return type;
    }

    /**
     * @return Level of difficulty of this question.
     */
    public Difficulty getDifficulty() {
        return difficulty;
    }

    /**
     * @return Question prompt. Every question has a prompt.
     */
    public String getPrompt() {
        return prompt;
    }

    /**
     * @return After marking, whether the answer was correct.
     */
    public boolean isCorrectlyAnswered() {
        return correct;
    }

    /**
     * This method should be overridden in the case that
     * a question's marks can be modified, such as when a
     * SpellingQuestion offers a hint to the user.
     *
     * @return Amount of marks this question is worth.
     * @see au.edu.swin.ajass.models.questions.SpellingQuestion
     */
    public double getMarksEarnt() {
        return difficulty.getMarks();
    }

    /**
     * Marks that this question has been answered, finalised, and marked.
     */
    public void setAnswered() {
        if (answered) throw new IllegalStateException("Cannot re-finalise question");
        this.answered = true;
    }

    /**
     * @return Whether this has had its answer checked.
     */
    public boolean isAnswered() {
        return answered;
    }

    /**
     * Empty method that can be overridden for any purpose.
     * Called when the Question is prompted on screen.
     */
    public void onPrompted(){
    }
}
