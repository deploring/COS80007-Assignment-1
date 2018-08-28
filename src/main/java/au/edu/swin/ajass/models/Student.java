package au.edu.swin.ajass.models;

/**
 * Represents the user, or a student, in the assignment specification.
 * Their information is contained within this Model, as is the login
 * information to access the software.
 *
 * @author Bradley Chick, Joshua Skinner
 * @version 1
 * @since 0.1
 */

public final class Student {

    private String studentID, schoolName;
    private String loginPIN;
    private Long timeCreated;

    public Student(String studentID, String schoolName) {
        this.studentID = studentID;
        this.schoolName = schoolName;
        loginPIN = generatePin();
        timeCreated = System.currentTimeMillis();
    }

    /**
     * Generate a random PIN for the user to access the test
     * @return random loginPIN generated
     */
    private String generatePin() {
        StringBuilder PIN = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            PIN.append((int) (Math.random() * 10));
        }
        return PIN.toString();
    }

    /**
     * @return schoolName entered by user
     */
    public String getSchoolName() {
        return schoolName;
    }

    /**
     * @ loginPIN generated for user
     */
    public String getLoginPin() {
        return loginPIN;
    }

    /**
     * @return timeCreated , system time  was generated
     */
    public Long getTimeCreated() {
        return timeCreated;
    }
}
