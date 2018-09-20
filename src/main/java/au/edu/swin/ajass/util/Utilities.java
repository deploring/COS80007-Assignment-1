package au.edu.swin.ajass.util;

import au.edu.swin.ajass.Main;
import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * This Utility class provides a collection of useful
 * methods that do not belong to any particular class,
 * but are used throughout all the program.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public class Utilities {

    // Keep an instance of the latest MediaPlayer so GC does not remove it mid-playback.
    private static MediaPlayer mediaPlayer;

    /**
     * @return Seconds formatted into MM:SS.
     */
    public static String digitalTime(int totalSecs) {
        if (totalSecs == 0) return "0:00";
        int minutes = (totalSecs % 3600) / 60;
        int seconds = totalSecs % 60;
        return minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }

    /**
     * Plays an internal sound resource file located in the JAR.
     *
     * @param loc Path to sound resource file.
     */
    public static void playSound(String loc) {
        try {
            // Stop previous playback if playing.
            if (mediaPlayer != null)
                mediaPlayer.stop();

            // Translate the loc path into a URI for the Media to load.
            Media toPlay = new Media(Main.class.getResource(String.format("/%s", loc)).toURI().toString());
            mediaPlayer = new MediaPlayer(toPlay);
            mediaPlayer.play();
        } catch (URISyntaxException e) {
            // This shouldn't happen, so let's print the stack trace for debugging.
            e.printStackTrace();
        }
    }

    /**
     * Returns an instance of an Image that JavaFX or AWT can make use of.
     *
     * @param loc Path to image resource file.
     * @return Image resource!
     */
    public static Image image(String loc) {
        try {
            return ImageIO.read(Main.class.getResourceAsStream(String.format("/%s", loc)));
        } catch (IOException e) {
            // This shouldn't happen, so expect more bad things to happen!
            e.printStackTrace();
            throw new IllegalStateException("Unable to load image resource");
        }
    }

}
