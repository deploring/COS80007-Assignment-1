package au.edu.swin.ajass.controllers;

/**
 * Created by sky on 21/8/18.
 */
public class ExamState {

    private int timeRemaining;

    public ExamState(int totalTime) {
        this.timeRemaining = timeRemaining;

        // TODO: Call this when the first test is commenced.
        //new Thread(new GlobalTimer()).run();
    }

    /**
     * This timer decrements the amount of
     * time remaining for the whole exam.
     *
     * @author Joshua Skinner
     * @since 0.1
     * @version 1
     */
    private class GlobalTimer implements Runnable {

        @Override
        public void run()  {
            try {
                Thread.sleep(1000);
                timeRemaining--;
            } catch (InterruptedException e) {
                // This will never happen in normal operation.
            } finally {
                // Continue running this thread until instructed to stop.
                //TODO: Will infinitely call itself. It should terminate the exam when time reaches zero.
                run();
            }
        }
    }
}
