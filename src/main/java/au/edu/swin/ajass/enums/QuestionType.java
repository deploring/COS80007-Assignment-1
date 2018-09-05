package au.edu.swin.ajass.enums;

/**
 * This enumerated type allows Questions and
 * Tests to identify what type of question
 * they are or are going to issue respectively.
 *
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public enum QuestionType {
    MATHS(10, 3 * 60),
    SPELLING(15, 3 * 60),
    IMAGE(10, 3 * 60),
    WRITING(5, 3 * 60),
    LISTENING(8, 3 * 60);

    private int maxQuestions;
    private int maxTime;

    QuestionType(int maxQuestions, int maxTime) {
        this.maxQuestions = maxQuestions;
        this.maxTime = maxTime;
    }

    /**
     * @return Amount of questions that must be issued to complete this type of test category.
     */
    public int getMaxQuestions() {
        return maxQuestions;
    }

    /**
     * @return Maximum amount of time that can be taken in this test category.
     */
    public int getMaxTime() {
        return maxTime;
    }
}
