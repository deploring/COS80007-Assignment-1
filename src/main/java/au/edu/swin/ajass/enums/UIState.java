package au.edu.swin.ajass.enums;

/**
 * Represents the state that the UI should be in at any given time.
 * Since the UI itself does not know what view it needs to display,
 * this enum is used to allow the MainView to choose which view to
 * display. Views displayed by MainView are the views that display
 * data from the Models.
 *
 * @author Joshua Skinner
 * @version 1
 * @since 0.1
 * @see au.edu.swin.ajass.views.MainView
 */
public enum UIState {
    TERMS, // User is looking at the T&C
    PINGEN, // User is generating a login PIN
    LOGIN, // User is logging in
    EXAM // User is logged in and performing exam.
}
