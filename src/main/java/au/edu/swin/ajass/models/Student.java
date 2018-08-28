package au.edu.swin.ajass.models;

/**
 *  @author Bradley Chick, Joshua Skinner
 * @version 1
 * @since 0.1
 */

public class Student {

    public String studentID, schoolName;
    public String loginPIN;
    public Long timeCreated;

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
        String PIN = "";
        for (int i = 0; i < 4; i++) {
            PIN += (int)(Math.random() * 10) + "";
        }
        return PIN;
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
