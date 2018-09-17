package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Heartist on 17/09/2018.
 */
public final class WritingQuestion extends ImmutableQuestion<String> {

    static List<String> commonConjunctions;
    static List<String> commonRelativePronouns;

    // Conjunctions sourced from: https://www.englishclub.com/vocabulary/common-conjunctions-25.htm
    // Relative pronouns sourced from: http://grammar.yourdictionary.com/parts-of-speech/pronouns/relative-pronoun.html
    static {
        String[] commonConjList = {"and", "that", "but", "or", "as", "if", "when", "than", "because", "while", "where", "after", "so", "though", "since", "until", "whether", "before", "although", "nor", "like", "once", "unless", "now", "except"};
        commonConjunctions = Arrays.asList(commonConjList);
        String[] commonRelPronounList = {"that", "when", "which", "whichever", "whichsoever", "who", "whoever", "whosoever", "whom", "whomever", "whomsoever", "whose", "whosesoever", "whatever", "whatsoever"};
        commonRelativePronouns = Arrays.asList(commonRelPronounList);
    }

    private HashMap<String, Integer> errors;

    public WritingQuestion(QuestionType type, Difficulty difficulty, String prompt, Test test) {
        super(type, difficulty, prompt, test);
    }

    /**
     * Stores the user's answer as a String for marking
     *
     * @param answer User's answer
     */
    public void answer(String answer) {
        this.answer = answer;
    }

    /**
     * Once the user has pressed submit, any errors will be discovered and recorded.
     * A word may have multiple errors, which is why words are mapped to an integer.
     *
     * @param answer The answer given.
     * @return Map of incorrect words and how many errors they attracted.
     */
    public HashMap<String, Integer> hint(String answer) {
        switch (getDifficulty()) {
            case EASY:
                return checkSimple(answer);
            case MEDIUM:
                return checkCompound(answer);
            case HARD:
                return checkComplex(answer);
            default:
                throw new IllegalArgumentException("Unsupported question difficulty");
        }
    }

    /**
     * Checks if a sentence is classed as "simple".
     * A sentence is simple, in this context, if:
     * <ul>
     * <li>The first letter of the first word is capitalised.</li>
     * <li>No words contain out-of-place capitalisation (except for first character in each word).</li>
     * <li>There is a full stop at the end of the sentence, and only one full stop.</li>
     * <li>There are no numeric characters used.</li>
     * </ul>
     *
     * @param answer Answer given.
     * @return Map of incorrect words, and how many errors they attracted.
     */
    private HashMap<String, Integer> checkSimple(String answer) {
        HashMap<String, Integer> incorrect = new HashMap<>();

        String[] words = answer.split(" ");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            // Check if first word is capitalised.
            if (i == 0 && !Character.isUpperCase(word.charAt(0))) {
                addIncorrect(incorrect, String.format("%s[%d]", word, i));
                continue;
            }

            char[] chars = word.toCharArray();
            // Check if word is properly formatted.
            for (int j = 0; j < chars.length; j++) {
                Character charr = chars[j];
                if ((j > 0 && Character.isUpperCase(charr))
                        || Character.isDigit(charr)
                        || (charr.equals('.') && i + 1 < words.length)) {
                    addIncorrect(incorrect, String.format("%s[%d]", word, i));
                    break;
                }
            }

            // Check if there is a full stop at the end of a sentence.
            Character lastChar = word.charAt(word.length() - 1);
            if (!lastChar.equals('.') && i + 1 == words.length)
                addIncorrect(incorrect, "Structure");
        }

        return incorrect;
    }

    /**
     * Checks if a sentence is classed as "compound".
     * A sentence is compound, in this context, if:
     * <ul>
     * <li>The existing sentence has been classed as "simple".</li>
     * <li>A conjunction is used.</li>
     * <li>A comma is used.</li>
     * </ul>
     *
     * @param answer Answer given.
     * @return Map of incorrect words, and how many errors they attracted.
     */
    private HashMap<String, Integer> checkCompound(String answer) {
        // Get errors from simple sentence check.
        HashMap<String, Integer> incorrect = checkSimple(answer);

        boolean usedConjunction = false;
        boolean usedComma = false;

        String[] words = answer.split(" ");

        for (String word : words) {
            // Check if word is conjunction.
            if (!usedConjunction && commonConjunctions.contains(word))
                usedConjunction = true;

            char[] chars = word.toCharArray();
            // Check if word uses a comma.
            for (int j = 0; j < chars.length; j++) {
                Character charr = chars[j];
                if (j + 1 == word.length() && charr.equals(',')) {
                    usedComma = true;
                    break;
                }
            }
        }

        if (!usedConjunction)
            // Sentence structure is incorrect, as a conjunction was not used.
            addIncorrect(incorrect, "Structure");

        if (!usedComma)
            // Sentence structure is incorrect, as a comma was not used correctly.
            addIncorrect(incorrect, "Structure");

        return incorrect;
    }

    /**
     * Checks if a sentence is classed as "complex".
     * A sentence is complex, in this context, if:
     * <ul>
     * <li>The existing sentence has been classed as "compound".</li>
     * <li>A relative pronoun is used.</li>
     * <li>A word is capitalised that isn't at the beginning of the sentence.</li>
     * </ul>
     *
     * @param answer Answer given.
     * @return Map of incorrect words, and how many errors they attracted.
     */
    private HashMap<String, Integer> checkComplex(String answer) {
        // Get errors from compound sentence check.
        HashMap<String, Integer> incorrect = checkCompound(answer);

        boolean usedRelPronoun = false;
        boolean usedCapWord = false;

        String[] words = answer.split(" ");

        for (int i = 0; i < words.length; i++) {
            String word = words[i];
            // Check if word is conjunction.
            if (!usedRelPronoun && commonRelativePronouns.contains(word))
                usedRelPronoun = true;

            if (!usedCapWord && i != 0 && Character.isUpperCase(word.charAt(0)))
                usedCapWord = true;
        }

        if (!usedRelPronoun)
            // Sentence structure is incorrect, as a relative pronoun was not used.
            addIncorrect(incorrect, "Structure");

        if (!usedCapWord)
            // Sentence structure is incorrect, as a capitalised word was not used correctly.
            addIncorrect(incorrect, "Structure");

        return incorrect;
    }

    /**
     * Puts a word in the incorrect words map.
     * If it already exists in the map, the amount of mistakes for the word increases.
     *
     * @param incorrect Map of incorrect words and number of mistakes.
     * @param word      Word to add or increment.
     */
    private void addIncorrect(HashMap<String, Integer> incorrect, String word) {
        int mistakes = incorrect.getOrDefault(word, 0);
        incorrect.put(word, mistakes + 1);
    }

    /**
     * @return Errors made on specific words.
     */
    public HashMap<String, Integer> getErrors() {
        return errors;
    }

    /**
     * This method sets the Question's state to correct if:
     * <ul>
     * <li>The user's answer is in the list of correct "positive words".</li>
     * <li>The answer given has not been given in a previous question.</li>
     * </ul>
     * Answers are case insensitive.
     */
    @Override
    public void checkAnswer() {
        if (!isUniqueAnswer()) return;
        // If the answer doesn't match any previous questions, then the answer is correct, if it has no errors.
        errors = hint(getAnswer());
        for (Map.Entry<String, Integer> entry : errors.entrySet())
            System.out.println(entry.getKey() + "," + entry.getValue());
        correct = errors.size() == 0;
    }
}
