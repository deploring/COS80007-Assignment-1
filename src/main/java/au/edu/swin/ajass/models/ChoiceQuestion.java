package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

import java.util.Set;

public abstract class ChoiceQuestion extends Question {

    private final String[] answers; // Correct Choices
    private String[] answer; // Student's answers
    private final String[] choices; // Question choices

    public ChoiceQuestion(QuestionType type, Difficulty difficulty, String prompt, String[] answers, String[] choices) {
        super(type, difficulty, prompt);
        this.answers = answers;
        this.choices = choices;
    }

    @Override
    public void checkAnswer() {

        // Sets have no duplicate values, therefore cannot have duplicate answers

        // check length of arrays
        if(answer.length != answers.length)
            return;

        int temp = 0;
        for (String anAnswer : answer)
            for (String answer1 : answers)
                if (anAnswer.equals(answer1)) {
                    temp++;
                    break;
                }

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

    public String[] getAnswer() {
        return answer;
    }

    public String[] getChoices() {
        return choices;
    }

    public String[] getAnswers() {
        return answers;
    }

}
