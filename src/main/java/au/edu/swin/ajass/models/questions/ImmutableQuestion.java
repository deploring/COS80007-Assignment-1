package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Question;
import au.edu.swin.ajass.models.Test;

import java.util.Iterator;

/**
 * Immutable Questions are a generic kind of question where
 * the answer should be checked against previous questions
 * to ensure that the same correct answer has not been given
 * twice.
 *
 * @author Bradley Chick
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public abstract class ImmutableQuestion<T> extends Question implements Comparable<ImmutableQuestion> {

    protected T answer;

    // Immutable questions need to compare their answers against previous questions in the current test.
    private Test test;

    ImmutableQuestion(QuestionType type, Difficulty difficulty, String prompt, Test test) {
        super(type, difficulty, prompt);
        this.test = test;
    }

    /**
     * Cross-references answers from previously-presented questions on the current test.
     * If an answer from a previous question is the same as an answer for this question,
     * this method returns true.
     *
     * @return True if one or more questions share the same answer with this question, false otherwise.
     */
    boolean isUniqueAnswer(){
        Iterator<Question> previous = test.getQuestions();
        while (previous.hasNext()) {
            // We can safely assume all questions in this test are spelling questions.
            ImmutableQuestion prev = (ImmutableQuestion) previous.next();
            if (prev == this) continue;
            if (prev.compareTo(this) == 1) return false;
        }
        return true;
    }

    /**
     * @return User answer
     */
    public T getAnswer() {
        return answer;
    }

    /**
     * Compares this Question's answer to another question's answer.
     * This method assumes that both questions have been answered already.
     *
     * @param o Different question.
     * @return 1 if the answers are the same, 0 otherwise.
     */
    @Override
    public int compareTo(ImmutableQuestion o) {
        if (o.getAnswer().equals(getAnswer())) return 1;
        else return 0;
    }
}
