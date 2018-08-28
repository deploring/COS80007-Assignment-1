package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Exam;
import au.edu.swin.ajass.models.Question;
import au.edu.swin.ajass.models.QuestionBank;
import au.edu.swin.ajass.models.Test;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import au.edu.swin.ajass.models.questions.ImageQuestion;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.geom.Point2D;
import java.util.Set;

/**
 * -write purpose here-
 *
 * @author Joshua Skinner
 * @version 1
 * @see Exam
 * @since 0.1
 */
public final class ExamController {

    private final Exam exam;
    private final QuestionBank questionBank;

    private static String QUESTION_CONFIG_NAME = "questions.json";
    private static int EXAM_TIME = 15 /*minutes*/ * 60 /*seconds*/;

    public ExamController() {
        exam = new Exam(EXAM_TIME);
        questionBank = new QuestionBank(QUESTION_CONFIG_NAME);
    }

    /**
     * Begins a new test.
     *
     * @param category Category of the test.
     */
    public void beginTest(QuestionType category) {
        Test result = exam.newTest(category);
        // Give it a question!
        nextQuestion();
    }

    /**
     * Attempts to answer the current test's current question.
     * Object varargs is needed since every Question takes a
     * different type as an answer. Object is the root of <em>everything.</em>
     * <strong>The answer may not necessarily be checked yet.</strong>
     *
     * @param answer Answer(s).
     * @throws IllegalArgumentException Types passed in may not be the type expected by the question.
     */
    public void attemptAnswer(Object... answer) throws IllegalArgumentException {
        Test test = exam.getCurrentTest();
        Question current = test.getCurrentQuestion();
        switch (test.getCategory()) {
            case IMAGE:
                ImageQuestion image = (ImageQuestion) current;
                // Answer expects Point2D and nothing more
                if (answer.length != 1 || !(answer[0] instanceof Point2D))
                    throw new IllegalArgumentException("ImageQuestion expects Point2D[]");
                else image.answer((Point2D) answer[0]);
                break;
            case MATHS:
            case LISTENING:
                ChoiceQuestion choice = (ChoiceQuestion) current;
                // Answer expects String[] and nothing more
                if (answer.length != 1 || !(answer[0] instanceof Set))
                    throw new IllegalArgumentException("ChoiceQuestion expects Set");
                else if (((Set<?>) answer[0]).isEmpty())
                    throw new IllegalArgumentException("ChoiceQuestion expects non-empty Set");
                else {
                    // Just go straight ahead and try to cast the unknown Set to a Set of String.
                    // If it doesn't work, we know that it is a Set, but not a Set of String.
                    try {
                        choice.answer((Set<String>) answer[0]);
                    } catch (ClassCastException ex) {
                        throw new IllegalArgumentException("ChoiceQuestion expects Set<String>");
                    }
                }
                break;
            case WRITING:
                //TODO: this
                throw new NotImplementedException();
            case SPELLING:
                //TODO: this
                throw new NotImplementedException();
            default:
                throw new IllegalArgumentException("Invalid test category");
        }
    }

    /**
     * Once a question has been fully answered, it can be finalised and corrected.
     * This process must be separate because Questions like ImageQuestion require
     * multiple answer inputs before it can ever be marked as actually correct.
     *
     * @throws IllegalStateException Prevents question from being re-marked
     * @see ImageQuestion
     */
    public void finaliseAnswer() {
        // local variables and state check
        Test test = exam.getCurrentTest();
        Question current = test.getCurrentQuestion();
        if (current.isAnswered()) throw new IllegalStateException("Cannot finalise answered Question");

        // get the question to mark itself
        current.checkAnswer();
        current.setAnswered();

        // accrue marks
        exam.accrueMarks(current.getDifficulty().getMarks(), current.getMarksEarnt());

        // do next question
        nextQuestion();
    }

    /**
     * Attempts to hand another question to the currently active test.
     * Current test may become inactive and not accept a new question.
     */
    public void nextQuestion() {
        // Check active state first
        Test test = exam.getCurrentTest();
        if (!test.isActive()) return;

        Question nextQuestion = questionBank.retrieveQuestion(test.getCategory(), test.getCurrentDifficulty());
        test.newQuestion(nextQuestion);
    }
}
