package au.edu.swin.ajass.enums;

/**
 * In the adaptive test, there are different
 * difficulty levels for each question. This
 * enumerated type allows Questions to discern
 * what difficulty they are.
 *
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 */
public enum Difficulty {
    EASY(2),
    MEDIUM(5),
    HARD(10);

    private final double marks;

    Difficulty(double marks) {
        this.marks = marks;
    }

    /**
     * Returns how many marks this level of
     * difficulty is worth for a question.
     *
     * @return Marks available.
     */
    public double getMarks() {
        return marks;
    }

    /**
     * @return Difficulty level above this difficulty.
     */
    public Difficulty increase() {
        switch(this){
            case EASY:
                return MEDIUM;
            default:
                return HARD;
        }
    }

    /**
     * @return Difficulty level below this difficulty.
     */
    public Difficulty decrease() {
        switch(this){
            case HARD:
                return MEDIUM;
            default:
                return EASY;
        }
    }
}
