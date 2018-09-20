package au.edu.swin.ajass.controllers;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.*;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import au.edu.swin.ajass.models.questions.ImageQuestion;
import au.edu.swin.ajass.models.questions.SpellingQuestion;
import au.edu.swin.ajass.models.questions.WritingQuestion;

import java.awt.*;
import java.util.Iterator;
import java.util.Set;

/**
 * This controller grants one-way easy modifications to the
 * exam, test, and question models. It automatically handles
 * a lot of the processing through the methods defined below.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @see Exam
 * @since 0.1
 */
public final class ExamController {

    // Hard-coded static values.
    private static final String QUESTION_CONFIG_NAME = "questions.json";
    public static final int EXAM_TIME = 15 /*minutes*/ * 60 /*seconds*/;

    // Important exam components.
    private final Exam exam;
    private final QuestionBank questionBank;

    // Global timer thread.
    private Thread globalTimer;
    private Thread testTimer;

    public ExamController() {
        exam = new Exam(EXAM_TIME);
        questionBank = new QuestionBank(QUESTION_CONFIG_NAME);
    }

    /**
     * @return Exam model on its own. Used by the views for data display.
     */
    public Exam getExamModel() {
        return exam;
    }

    /**
     * Marks the beginning of the overall exam.
     *
     * @param globalTimer Global timer task.
     */
    public void beginExam(Runnable globalTimer) {
        this.globalTimer = new Thread(globalTimer);
        this.globalTimer.start();
    }

    /**
     * This method is called every second by the global
     * timer thread. It allows the global timer to decrement
     * as well as force the exam to cease once time is up.
     */
    public int tickExam() {
        int result = exam.decrementTime();
        if (result == 0)
            endExam();
        return result;
    }

    /**
     * This method is called every second by the test
     * timer thread. It allows the test timer to decrement
     * as well as force the test to cease once time is up.
     */
    public int tickTest() {
        int result = getExamModel().getCurrentTest().decrementTime();
        if (result == 0)
            testTimer.interrupt();
        return result;
    }

    /**
     * Called upon when the student has completed all
     * tests, or when the global timer has depleted.
     */
    private void endExam() {
        // Stop the global timer.
        globalTimer.interrupt();
    }

    /**
     * Creates a new Student instance and retains it.
     *
     * @param studentID  The Student's ID.
     * @param schoolName The Student's school name.
     * @return Instance of the Student.
     * @see Exam#student
     * @see Student
     */
    public Student registerStudentInfo(String studentID, String schoolName) {
        Student result = new Student(studentID, schoolName);
        exam.registerStudent(result);
        return result;
    }

    /**
     * If the student clears the PIN generation form, even
     * after generating a PIN, they are no longer able to
     * log in with it. Stored student data should be cleared.
     */
    public void clearStudentInfo() {
        exam.registerStudent(null);
    }

    /**
     * @return Stored student information.
     */
    public Student getStudentInfo() {
        return exam.getStudent();
    }

    /**
     * @return Whether a new test can be started or not.
     */
    public boolean canBeginTest() {
        // Test can be begun if there are no previous tests or if the latest test is not active.
        return !exam.getTests().hasNext() || !exam.getCurrentTest().isActive();
    }

    /**
     * Begins a new test.
     *
     * @param category Category of the test.
     */
    public void beginTest(QuestionType category, Runnable testTimer) {
        exam.newTest(category);
        // Give it a question!
        nextQuestion();

        // Start timer
        this.testTimer = new Thread(testTimer);
        this.testTimer.start();
    }

    /**
     * This is called when a test needs to be repeated.
     * It is hard-coded to only repeat the Listening test when
     * called as that is the only requirement of the software.
     *
     * @param testTimer A new instance of a test timer runnable.
     */
    public void repeatTest(Runnable testTimer) {
        Iterator<Test> tests = exam.getTests();
        Test test = null;
        while (tests.hasNext()) {
            Test next = tests.next();
            if (next.getCategory() == QuestionType.LISTENING) {
                test = next;
                break;
            }
        }
        if (test == null)
            throw new IllegalStateException("Could not repeat Listening test: null");

        // Accrue negative marks to subtract 5 from the total.
        exam.accrueMarks(-5);

        // Repeat the test!~
        exam.bringOldTestForward(test);
        test.repeat();

        // Start another timer
        this.testTimer = new Thread(testTimer);
        this.testTimer.start();
    }

    /**
     * Stops the test timer and marks the test as incomplete, if applicable.
     *
     * @param incomplete Whether the test was not fully completed.
     */
    public void finishTest(boolean incomplete) {
        // Stop the test timer.
        testTimer.interrupt();
        getExamModel().getCurrentTest().setFinishedEarly(incomplete);
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
                if (answer.length != 1 || !(answer[0] instanceof Point))
                    throw new IllegalArgumentException("ImageQuestion expects Point");
                else image.answer((Point) answer[0]);
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
                WritingQuestion writing = (WritingQuestion) current;
                // Answer expects String and nothing more
                if (answer.length != 1 || !(answer[0] instanceof String))
                    throw new IllegalArgumentException("WritingQuestion expects String");
                else writing.answer((String) answer[0]);
                break;
            case SPELLING:
                SpellingQuestion spelling = (SpellingQuestion) current;
                // Answer expects String and nothing more
                if (answer.length != 1 || !(answer[0] instanceof String))
                    throw new IllegalArgumentException("SpellingQuestion expects String");
                else spelling.answer((String) answer[0]);
                break;
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

        // accrue marks if correct
        if (current.isCorrectlyAnswered())
            exam.accrueMarks(current.getMarksEarnt());

        // adjust difficulty
        Difficulty currentDiff = current.getDifficulty();
        test.adjustDifficulty(current.isCorrectlyAnswered() ? currentDiff.increase() : currentDiff.decrease());
    }

    /**
     * Attempts to hand another question to the currently active test.
     * Current test may become inactive and not accept a new question.
     */
    public void nextQuestion() {
        // Check active state first
        Test test = exam.getCurrentTest();
        if (!test.isActive()) return;

        Question nextQuestion = questionBank.retrieveQuestion(test.getCategory(), test.getCurrentDifficulty(), getExamModel().getCurrentTest());
        test.newQuestion(nextQuestion);
    }

    /**
     * Checks if the Listening Test is eligible to be repeated.
     * Specification only mentioned that listening test is needed to be repeatable.
     *
     * @return True if the listening test can be repeated.
     */
    public boolean canRepeatTest() {
        // Repeating the test costs five marks.
        if (exam.getTotalMarks() < 5) return false;

        Iterator<Test> tests = exam.getTests();
        Test test = null;
        while (tests.hasNext()) {
            Test next = tests.next();
            if (next.getCategory() == QuestionType.LISTENING) {
                test = next;
                break;
            }
        }
        // There is no test to repeat.
        if (test == null) return false;
        // Test has already been fully completed, or is still active.
        return test.isRepeatable() && !test.isComplete() && !test.isActive();
    }
}
