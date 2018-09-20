package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

/**
 * QuestionBank holds instances of QuestionCache and
 * QuestionFactory, which are used to return questions
 * for the test.
 * Makes a decision to ask either based on type of question
 * required.
 *
 * @author Joshua Skinner, Bradley Chick
 * @version 1
 * @see QuestionCache
 * @see QuestionFactory
 * @since 0.1
 */
public final class QuestionBank {

    private final QuestionCache cache;
    private final QuestionFactory factory;

    /**
     * Set instances of QuestionCache and QuestionFactory
     */
    public QuestionBank(String config) {
        cache = new QuestionCache(config);
        factory = new QuestionFactory();
    }

    /**
     * @return a Question of required type and difficulty
     * If the question must be generated (Spelling, Writing),
     * it asks QuestionFactory, else, other questions (Maths,
     * Listening, Image) are retrieved from QuestionCache
     */
    public Question retrieveQuestion(QuestionType category, Difficulty difficulty, Test currentTest) {
        switch (category) {
            case WRITING:
            case SPELLING:
                return factory.retrieveQuestion(category, difficulty, currentTest);
            case MATHS:
            case LISTENING:
            case IMAGE:
                return cache.retrieveQuestion(category, difficulty);
            default:
                throw new IllegalArgumentException("Invalid category supplied");
        }
    }
}
