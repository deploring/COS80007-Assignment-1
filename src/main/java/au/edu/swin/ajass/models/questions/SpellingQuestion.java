package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Test;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Spelling Questions are a type of Immutable Question.
 * In order for a user's answer to be considered correct,
 * it must not match up against and previous answers of
 * the same question type and must be in one of the pre-
 * defined arrays.
 *
 * @author Bradley Chick
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public final class SpellingQuestion extends ImmutableQuestion<String> {

    // Hard-coded Spelling Question answers to check functionality.
    private static final HashMap<Difficulty, List<String>> answers = new HashMap<>();

    // User's answer
    private String answer;

    // Whether marks should be deducted. (changed after 10 seconds)
    private boolean deductHalfMarks;

    // Words sourced from: http://positivewordsresearch.com/list-of-positive-words/
    static {
        String[] easyList = {"gather", "generous", "genius", "genuine", "give", "glad", "glow", "good", "gorgeous", "grace", "graceful", "gratitude", "green", "grin", "group", "grow"};
        List<String> easy = Arrays.asList(easyList);
        String[] mediumList = {"joined", "jovial", "joyful", "jubilant", "justice", "joke"};
        List<String> medium = Arrays.asList(mediumList);
        String[] hardList = {"zealous", "zest", "zippy", "zing", "zappy", "zany", "zesty"};
        List<String> hard = Arrays.asList(hardList);
        answers.put(Difficulty.EASY, easy);
        answers.put(Difficulty.MEDIUM, medium);
        answers.put(Difficulty.HARD, hard);
    }

    public SpellingQuestion(QuestionType type, Difficulty difficulty, String prompt, Test test) {
        super(type, difficulty, prompt, test);
        deductHalfMarks = false;
    }

    /**
     * Stores the user's answer as a String for marking
     *
     * @param answer User's answer
     */
    public void answer(String answer) {
        this.answer = answer.toLowerCase();
    }

    /**
     * @return User answer
     */
    public String getAnswer() {
        return answer;
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
        // If the answer doesn't match any previous questions, then the answer is correct, if it exists within the list.
        correct = answers.get(getDifficulty()).contains(answer.toLowerCase());
    }

    @Override
    public double getMarksEarnt() {
        // Divide by two if deductHalfMarks = true.
        return getDifficulty().getMarks() / (deductHalfMarks ? 2d : 1d);
    }

    @Override
    public void onPrompted() {
        // User only has ten seconds to receive full marks.
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                if (!isAnswered() && test.isActive()) {
                    // They have taken too long to answer.
                    deductHalfMarks = true;
                    List<String> toPick = answers.get(getDifficulty());
                    String hint = toPick.get((int) Math.floor(Math.random() * toPick.size())).substring(0, 3);
                    JOptionPane.showMessageDialog(null, "You have taken too long to respond.\nBelow is a hint of a desired positive word.\nIf answered correctly, you will only receive half of your allotted marks.\nHint: " + hint, "Hint", JOptionPane.QUESTION_MESSAGE);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
