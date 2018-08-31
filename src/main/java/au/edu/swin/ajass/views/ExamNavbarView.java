package au.edu.swin.ajass.views;

import javax.swing.*;
import java.awt.*;

/**
 * Created by sky on 21/8/18.
 */
public class ExamNavbarView extends JPanel implements IView {

    // Reference back to JFrame, ExamController
    private final MainView main;

    public ExamNavbarView(MainView main) {
        this.main = main;
        setLayout(new FlowLayout());
    }

    @Override
    public void onDisplay() {
        main.exam().beginExam(new ExamTimer());
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    /**
     * This timer decrements the amount of
     * time remaining for the whole exam.
     *
     * @author Joshua Skinner
     * @version 1
     * @since 0.1
     */
    private class ExamTimer implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                main.exam().decrementTimer();
                // Continue running this thread until instructed to stop.
                run();
            } catch (InterruptedException e) {
                // The thread should stop once it is interrupted.
            }
        }
    }
}
