package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Question Cache gives access to questions through a JAR resource file.
 * Questions are in a random order and returned differently each time.
 *
 * Questions addressed by the QuestionCache have finite, predefined
 * answers. These questions can be loaded from an internal file, then
 * retrieved and answered when they are needed.
 *
 * @author Joshua Skinner, Bradley Chick
 * @version 1
 * @since 0.1
 */
public class QuestionCache {

    // Map which maps QuestionType(s) and Difficulty(s) to a specific List.
    private final Map<QuestionType, Map<Difficulty, List<Question>>> questionMap;

    public QuestionCache(String config) {
        questionMap = new HashMap<>();

        //TODO: load in questions
    }

    private void load(String config) {

    }

    public Question retrieveQuestion(QuestionType category, Difficulty diff) {

        return null;
    }
}
