package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * WritingQuestions are a type of immutable question.
 * In order for a user's answer to be considered correct,
 * the answer given must not match up against any previous
 * answers from the same question type, and must satisfy
 * all writing criteria necessary for the difficulty.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public final class WritingQuestion extends ImmutableQuestion<String> {

    private static final List<String> commonConjunctions;
    private static final List<String> commonRelativePronouns;

    // Conjunctions sourced from: https://www.englishclub.com/vocabulary/common-conjunctions-25.htm
    // Relative pronouns sourced from: http://grammar.yourdictionary.com/parts-of-speech/pronouns/relative-pronoun.html
    static {
        String[] commonConjList = {"and", "that", "but", "or", "as", "if", "when", "than", "because", "while", "where", "after", "so", "though", "since", "until", "whether", "before", "although", "nor", "like", "once", "unless", "now", "except", "however"};
        commonConjunctions = Arrays.asList(commonConjList);
        String[] commonRelPronounList = {"that", "when", "which", "whichever", "whichsoever", "who", "whoever", "whosoever", "whom", "whomever", "whomsoever", "whose", "whosesoever", "whatever", "whatsoever"};
        commonRelativePronouns = Arrays.asList(commonRelPronounList);
    }

    // Errors made in the user's answer.
    private HashMap<String, Integer> errors;

    // Stores whether a hint has been used yet or not.
    private boolean hinted;

    // Mark reduction coefficient. 0.5 means half marks awarded. 0 means no marks awarded. 1 means full marks.
    private double markDeductionCoeff;

    public WritingQuestion(QuestionType type, Difficulty difficulty, String prompt, Test test) {
        super(type, difficulty, prompt, test);
        hinted = false;
        markDeductionCoeff = 1d;
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
        hinted = true;
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
     * @return True if the user has received feedback from hint(), false otherwise
     */
    public boolean isHinted() {
        return hinted;
    }

    /**
     * When called, only half of the allotted marks will be awarded for this question.
     */
    public void deductHalfMarks() {
        markDeductionCoeff = 0.5d;
        new Thread(() -> {
            try {
                // Deduct full marks if question was not answered in time.
                // Only deduct if the test is still active, too.
                Thread.sleep(10000);
                if (!isAnswered() && test.isActive())
                    deductFullMarks();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * When called, none of the allotted marks will be awarded for this question.
     */
    private void deductFullMarks() {
        markDeductionCoeff = 0d;
    }

    /**
     * Checks if a sentence is classed as "simple".
     * A sentence is simple, in this context, if:
     * <ul>
     * <li>The first letter of the first word is capitalised.</li>
     * <li>No words contain out-of-place capitalisation (except for first character in each word).</li>
     * <li>There is a full stop at the end of the sentence, and only one full stop.</li>
     * <li>There are no numeric characters used.</li>
     * <li>At least five words are used.</li>
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
            // Skip if word is empty.
            if (word.length() == 0) continue;

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

        if (words.length < 5)
            addIncorrect(incorrect, "Word count");

        return incorrect;
    }

    /**
     * Checks if a sentence is classed as "compound".
     * A sentence is compound, in this context, if:
     * <ul>
     * <li>The existing sentence has been classed as "simple".</li>
     * <li>A conjunction is used.</li>
     * <li>A comma is used.</li>
     * <li>At least ten words are used.</li>
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
            // Skip if word is empty.
            if (word.length() == 0) continue;

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

        if (words.length < 10)
            addIncorrect(incorrect, "Word count");

        return incorrect;
    }

    /**
     * Checks if a sentence is classed as "complex".
     * A sentence is complex, in this context, if:
     * <ul>
     * <li>The existing sentence has been classed as "compound".</li>
     * <li>A relative pronoun is used.</li>
     * <li>A word is capitalised that isn't at the beginning of the sentence.</li>\
     * <li>At least fifteen words are used.</li>
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
            // Skip if word is empty.
            if (word.length() == 0) continue;

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

        if (words.length < 15)
            addIncorrect(incorrect, "Word count");

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
     * @return Errors made on specific words, after checking marks.
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
        correct = errors.size() == 0;
    }

    @Override
    public double getMarksEarnt() {
        return getDifficulty().getMarks() * markDeductionCoeff;
    }

}
