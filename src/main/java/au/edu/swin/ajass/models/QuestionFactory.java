package au.edu.swin.ajass.models;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.questions.SpellingQuestion;
import au.edu.swin.ajass.models.questions.WritingQuestion;

/**
 * QuestionFactory returns questions that theoretically have infinitely,
 * or nearly infinitely many answers. Answers to these questions must be
 * checked individually and thus must be created and returned individually.
 *
 * @author Bradley Chick, Joshua Skinner
 * @version 1
 * @since 0.1
 */
public final class QuestionFactory {

    public QuestionFactory() {
    }

    public Question retrieveQuestion(QuestionType category, Difficulty diff, Test currentTest) {
        String prompt;
        switch (category) {
            case SPELLING:
                switch (diff) {
                    case EASY:
                        prompt = "Please spell a positive word starting with 'G'.";
                        break;
                    case MEDIUM:
                        prompt = "Please spell a positive word starting with 'J'.";
                        break;
                    case HARD:
                        prompt = "Please spell a positive word starting with 'Z'.";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid or unsupported question difficulty");
                }
                return new SpellingQuestion(category, diff, prompt, currentTest);
            case WRITING:
                switch (diff) {
                    case EASY:
                        prompt = "Please write out a simple sentence.";
                        break;
                    case MEDIUM:
                        prompt = "Please write out a compound sentence.";
                        break;
                    case HARD:
                        prompt = "Please write out a complex sentence.";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid or unsupported question difficulty");
                }
                prompt += " Do NOT use numeric characters.";
                return new WritingQuestion(category, diff, prompt, currentTest);
            default:
                throw new IllegalArgumentException("Invalid or unsupported question category");
        }
    }
}
