package au.edu.swin.ajass.models.questions;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;

/**
 * ListeningQuestion is a special extension of ChoiceQuestion,
 * as Listening Questions require the playing of a sound clip.
 * This class serves the express purpose of not only identifying
 * a Listening Question type-wise, but also provides access to
 * the sound clip JAR resource.
 *
 * @author Joshua Skinner
 * @version 1.0
 * @see ChoiceQuestion
 * @since 0.1
 */
public final class ListeningQuestion extends ChoiceQuestion {

    private final String soundFileLoc;

    /**
     * Choice-Question specific parameters.
     * Refer to superclass for other param descriptions.
     *
     * @param soundFileLoc Path to the sound clip file (as a JAR resource).
     * @see ChoiceQuestion
     * @see ListeningQuestion
     */
    public ListeningQuestion(QuestionType type, Difficulty difficulty, String[] answers, String[] choices, String soundFileLoc) {
        super(type, difficulty, "Click the button to listen to the sentence(s). Note the accent. Select the correct key words that are spoken.", answers, choices);
        this.soundFileLoc = soundFileLoc;
    }

    /**
     * @return Path to the sound clip file (as a JAR resource).
     */
    public String getSoundFileLoc() {
        return soundFileLoc;
    }
}
