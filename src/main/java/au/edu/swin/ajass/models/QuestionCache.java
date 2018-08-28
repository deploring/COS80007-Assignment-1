package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sky on 24/8/18.
 */
public class QuestionCache {

    private final Map<QuestionType, Map<Difficulty, List<Question>>> questionMap;

    public QuestionCache(File file) {
        questionMap = new HashMap<>();

        //TODO: load in questions
    }

    private void load(File file) {

    }
}
