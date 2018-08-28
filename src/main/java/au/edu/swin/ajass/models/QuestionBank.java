package au.edu.swin.ajass.models;

import java.io.File;

/**
 * QuestionBank holds instances of QuestionCache and
 * QuestionFactory, which are used to return questions
 * for the test.
 * Makes a decision to ask either based on type of question
 * required.
 *
 * @author Joshua Skinner, Bradley Chick
 * @version 1
 * @since 0.1
 *
 * @see QuestionCache
 * @see QuestionFactory
 */
public class QuestionBank {

    private QuestionCache cache;
    private QuestionFactory factory;

    /**
     * Set instances of QuestionCache and QuestionFactory
     */
    public QuestionBank(File file) {
        cache = new QuestionCache(file);
        factory = new QuestionFactory();
    }

    /**
     * @return a Question of required type and difficulty
     * If the question must be generated (Spelling, Writing),
     * it asks QuestionFactory, else, other questions (Maths,
     * Listening, Image) are retrieved from QuestionCache
     */
    public Question retrieve() {
        return null;
    }
}
