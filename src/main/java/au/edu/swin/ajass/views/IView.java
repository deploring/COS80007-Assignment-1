package au.edu.swin.ajass.views;

import javax.swing.*;

/**
 * IView is a structure class that all Views in
 * this software need to implement. It allows the
 * MainView to "update" the view if necessary when
 * it is displayed, or re-displayed.
 *
 * @author Joshua Skinner
 * @see MainView
 * @see au.edu.swin.ajass.enums.UIState
 */
public interface IView {

    /**
     * This is called when the view has been displayed inside the JFrame.
     * Routines relevant to the view should be executed here, as not all
     * views are displayed immediately. (i.e. the exam view)
     */
    default void onDisplay() {
        // Views that need to change their layout once they're displayed can override this method.
    }

    /**
     * Each view extends JPanel, so it should return an instance of itself.
     *
     * @return Instance of the view (JPanel).
     */
    JPanel getPanel();
}
