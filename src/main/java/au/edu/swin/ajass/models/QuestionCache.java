package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Question Cache gives access to questions through a JAR resource file.
 * Questions are in a random order and returned differently each time.
 * <p>
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
        try {
            load(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException(String.format("Unable to load questions into cache due to %s: %s", e.getClass().getTypeName(), e.getMessage()));
        }
    }

    /**
     * Searches for the JAR resource that contains the
     * pre-loaded questions under JSON format.
     *
     * @param config JAR resource name/location.
     * @throws IOException Input file may not be valid.
     */
    private void load(String config) throws IOException {
        JSONParser parser = new JSONParser();
        JSONObject root;
        try {
            root = (JSONObject) parser.parse(new InputStreamReader(QuestionCache.class.getResourceAsStream(String.format("%s%s", File.separator, config)), "UTF-8"));
        } catch (IOException | ParseException e) {
            throw new IOException(String.format("Unable to load properties from %s", config));
        }

        for (Object element : root.keySet()) {
            if (!(element instanceof String))
                throw new IOException(String.format("Expected String, got %s", element.getClass().getTypeName()));
            QuestionType category = QuestionType.valueOf((String) element);

            // Parse each individual question, then place it into the correct mapped List.
            for (Object question : (JSONArray) root.get(element))
                placeQuestion(parseQuestion(category, (JSONObject) question));

        }
    }

    /**
     * Parses a JSONObject into a specific subtype of Question,
     * given the category that is passed in also.
     *
     * @param category Type of question.
     * @param question JSONObject representing a Question.
     * @return Resulting Question.
     */
    private Question parseQuestion(QuestionType category, JSONObject question) {
        Question result;
        switch (category) {
            case MATHS:
                String prompt = (String) question.get("prompt");
                Difficulty difficulty = Difficulty.valueOf((String) question.get("difficulty"));
                String[] options = convertJSONArrayToStringArray((JSONArray) question.get("options"));
                String[] answers = convertJSONArrayToStringArray((JSONArray) question.get("answer"));
                result = new ChoiceQuestion(category, difficulty, prompt, options, answers);
                break;
            default:
                throw new IllegalArgumentException("invalid/unsupported category of question");
        }
        return result;
    }

    /**
     * Maps a question to the correct list.
     * Map QuestionType -> Difficulty -> Specific Question
     *
     * @param toPlace The question to place in the list.
     */
    private void placeQuestion(Question toPlace) {
        // If the map inside the QuestionType map doesn't exist, create it first.
        if (!questionMap.containsKey(toPlace.getType()))
            questionMap.put(toPlace.getType(), new HashMap<>());

        // If the list inside the Difficulty map inside the QuestionType doesn't exist, create it first.
        if (!questionMap.get(toPlace.getType()).containsKey(toPlace.getDifficulty()))
            questionMap.get(toPlace.getType()).put(toPlace.getDifficulty(), new ArrayList<>());

        // Add to the List that contains Questions of a specific category and difficulty.
        questionMap.get(toPlace.getType()).get(toPlace.getDifficulty()).add(toPlace);
    }

    /**
     * Converts a JSONArray into a String array.
     *
     * @param input JSONArray object.
     * @return String array object.
     * @throws IllegalArgumentException JSONArray may not contain strings.
     */
    private String[] convertJSONArrayToStringArray(JSONArray input) {
        String[] result = new String[input.size()];
        for (int i = 0; i < input.size(); i++) {
            Object element = input.get(i);
            if (!(element instanceof String))
                throw new IllegalArgumentException("expected JSONArray of String");
            result[i] = (String) element;
        }
        return result;
    }

    public Question retrieveQuestion(QuestionType category, Difficulty diff) {
        return null;
    }
}
