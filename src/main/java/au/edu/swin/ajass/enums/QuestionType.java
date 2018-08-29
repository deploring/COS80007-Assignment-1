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
    MATHS(10),
    SPELLING(15),
    IMAGE(10),
    WRITING(5),
    LISTENING(10);

    private int maxQuestions;

    QuestionType(int maxQuestions) {
        this.maxQuestions = maxQuestions;
    }

    /**
     * @return Maximum amount of questions that can be issued for this type of test category.
     */
    public int getMaxQuestions() {
        return maxQuestions;
    }
}
