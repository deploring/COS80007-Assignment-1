package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Question;

import java.util.Set;

/**
 * ChoiceQuestion is a sub-type of question that encompasses
 * all question types that are of multiple choice. This means
 * that there can be one or many answers, amongst many choices.
 * This class is designed to automate much of the marking process
 * by comparing a user's answers to the answers that were defined
 * when this question was initialised.
 *
 * @author Bradley Chick, Joshua Skinner
 * @version 1
 * @since 0.1
 */
public class ChoiceQuestion extends Question {

    private final String[] answers; // Required answers
    private final String[] choices; // Question choices (not all are correct)
    private String[] answer; // User answers

    /**
     * Choice-Question specific parameters.
     * Refer to Question superclass for other param descriptions.
     *
     * @param answers Required answers that the user must have.
     * @param choices Choices for this question. Not all are correct.
     * @see Question
     */
    public ChoiceQuestion(QuestionType type, Difficulty difficulty, String prompt, String[] answers, String[] choices) {
        super(type, difficulty, prompt);
        this.answers = answers;
        this.choices = choices;
    }

    /**
     * This method sets the Question's state to correct if:
     * <ul>
     * <li>The amount of user answers equals the amount of required answers.</li>
     * <li>Each answer in the user answers is also inside the required answers.</li>
     * </ul>
     * It should be noted that answers are passed in through a set,
     * so there is no possibility of having two of the same answer.
     */
    @Override
    public void checkAnswer() {
        // check length of arrays
        if (answer.length != answers.length)
            return;

        // count how many user answers are inside required answers
        int temp = 0;
        for (String anAnswer : answer)
            for (String answer1 : answers)
                if (anAnswer.equals(answer1)) {
                    temp++;
                    break;
                }

        // if the amount of correct answers is the same as the amount
        // of required answers, this question was answered corrrectly.
        if (temp == answers.length) {
            correct = true;
        }
    }

    /**
     * Stores the user's answer as an array for marking
     *
     * @param answers set of unique answers to the question
     */
    public void answer(Set<String> answers) {
        // .toArray only requires an empty array
        this.answer = answers.toArray(new String[0]);
    }

    /**
     * @return Array of user answers
     */
    public String[] getAnswer() {
        return answer;
    }

    /**
     * @return Array of question choices (not all are correct)
     */
    public String[] getChoices() {
        return choices;
    }

    /**
     * @return Array of required answers
     */
    public String[] getAnswers() {
        return answers;
    }

}
