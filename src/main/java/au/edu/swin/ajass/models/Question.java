package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

/**
 * A question that can appear on any test at any
 * given point. This superclass houses common
 * attributes between the different types of
 * questions.
 *
 * @author Bradley Chick, Joshua Skinner
 * @version 1
 * @since 0.1
 */
public abstract class Question {

    private final QuestionType type;
    private final Difficulty difficulty;

    private boolean issued;
    private boolean correct;

    public Question(QuestionType type, Difficulty difficulty) {
        this.type = type;
        this.difficulty = difficulty;

        // By default, a new Question is non-issued and not marked.
        this.issued = false;
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
     * This method is called when the Question is
     * presented to the user on the screen.
     */
    public abstract void onPresent();

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
     * @return After marking, whether the answer was correct.
     */
    public boolean isCorrectlyAnswered() {
        return correct;
    }

    /**
     * @return Whether this Question has been taken out of the Question Bank.
     */
    public boolean isIssued(){
        return issued;
    }
}
