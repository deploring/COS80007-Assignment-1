package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Question;

import java.awt.*;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * ImageQuestion prompts the user to click on repeated
 * patterns within a picture. In order to be correct, the
 * user must click each required clickable point at least
 * once to have the question marked as correct.
 * <p>
 * The click does not need to be exact; there is a 25 pixel
 * margin for error around the point that will still be
 * considered as correct if clicked in.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public final class ImageQuestion extends Question {

    public static final int CORRECT_RANGE = 25; // Range, in pixels, around the required region that will be considered correct.
    private final Point[] answers; // Required clickable regions
    private final Set<Point> answer; // User clicked regions
    private final String imageFileLoc;

    private Point absoluteImagePoint;

    /**
     * Image-Question specific parameters.
     * Refer to Question superclass for other param descriptions.
     *
     * @param answers      Required clickable regions
     * @param imageFileLoc Path to image file (as JAR resource)
     * @see Question
     */
    public ImageQuestion(QuestionType type, Difficulty difficulty, String prompt, Point[] answers, String imageFileLoc) {
        super(type, difficulty, prompt);
        this.answers = answers;
        this.imageFileLoc = imageFileLoc;
        answer = new HashSet<>();
    }

    /**
     * This method sets the Question's state to correct if:
     * <ul>
     * <li>The amount of user answers equals the amount of required answers.</li>
     * <li>Each point in the user answers overlaps all the required points.</li>
     * </ul>
     * Selecting the same correct point twice will not be marked as correct.
     */
    @Override
    public void checkAnswer() {
        // check length of user answers vs required answers
        if (answer.size() != answers.length)
            return;

        // record which points in the answers array have been correctly selected
        boolean[] temp = new boolean[answers.length];
        // fill temp array
        for (int i = 0; i < temp.length; i++) temp[i] = false;

        // go through each user selected point individually
        // if the selected point is close to a required answer point, it will mark its position in the temp array as true
        for (Point answer : answer)
            for (int i = 0; i < answers.length; i++) {
                // This point has been correctly selected already, continue
                if (temp[i]) continue;
                Point correct = answers[i];
                double minX = correct.getX() - CORRECT_RANGE, maxX = correct.getX() + CORRECT_RANGE;
                double minY = correct.getY() - CORRECT_RANGE, maxY = correct.getY() + CORRECT_RANGE;
                if (answer.getX() >= minX && answer.getX() <= maxX && answer.getY() >= minY && answer.getY() <= maxY)
                    temp[i] = true;
    }

        // if any of the points were not selected, then the answer was not correct.
        for (boolean aTemp : temp) if (!aTemp) return;

        // if each correct required point was selected at least once, then the user's answer is correct
        correct = true;
    }

    /**
     * Stores the user's latest click in the Set for marking later.
     *
     * @param answer The point where the user clicked on screen.
     * @throws IllegalStateException Doesn't allow more answers than the answer requires.
     */
    public void answer(Point answer) throws IllegalStateException {
        // Ignore additional answers after set size has been capped.
        if (this.answer.size() >= answers.length) return;
        this.answer.add(answer);
    }

    /**
     * @return An iterator over the user's answers
     */
    public Iterator<Point> getAnswer() {
        return answer.iterator();
    }

    /**
     * @return Array of required answer points
     */
    public Point[] getAnswers() {
        return answers;
    }

    /**
     * Resets the user's given answers so they can try again.
     */
    public void resetAnswers() {
        answer.clear();
    }

    /**
     * @return Path to the image file (as a JAR resource).
     */
    public String getImageFileLoc() {
        return imageFileLoc;
    }

    /**
     * Once a question is presented, the absolute point of the image
     * on the screen should be recorded so clicks can be accurately
     * calculated.
     *
     * @param point Absolute image point.
     */
    public void setAbsoluteImagePoint(Point point){
        absoluteImagePoint = point;
    }

    /**
     * @return Absolute position of the image on screen.
     */
    public Point getAbsoluteImagePoint(){
        return absoluteImagePoint;
    }

}
