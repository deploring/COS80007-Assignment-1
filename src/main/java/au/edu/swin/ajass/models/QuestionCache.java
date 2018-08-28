package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Question Cache gives access to sorted questions through a file.
 * Questions are in a random order and returned differently
 * each test.
 *
 * Questions addressed by the QuestionCache have finite, predefined
 * answers. These questions can be loaded from an external file
 * and retrieved and answered when they are needed.
 *
 * @author Joshua Skinner, Bradley Chick
 * @version 1
 * @since 0.1
 */
public class QuestionCache {

    /**
     * A Map which maps QuestionType(s) and Difficulty(s) to a List of Questions
     */
    private final Map<QuestionType, Map<Difficulty, List<Question>>> questionMap;

    public QuestionCache(File file) {
        questionMap = new HashMap<>();

        //TODO: load in questions
    }

    private void load(File file) {

    }

    public Question retrieveQuestion(QuestionType type, Difficulty diff) {

        return null;
    }
}
