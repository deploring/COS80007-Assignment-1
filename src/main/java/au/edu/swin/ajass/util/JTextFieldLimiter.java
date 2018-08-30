package au.edu.swin.ajass.util;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * Implements a simple Document to prevent certain
 * JComponents from having their inputs longer than
 * X amount of characters. This class is designated
 * as a utility because we may need to use it anywhere.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public class JTextFieldLimiter extends PlainDocument {

    private final int max;

    public JTextFieldLimiter(int max) {
        super();
        this.max = max;
    }

    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        // String may be null, so ignore if this is the case.
        if (str == null)
            return;

        // Check if adding this String to the Document exceeds the max length, and cancel it.
        if ((getLength() + str.length()) <= max) {
            super.insertString(offset, str, attr);
        }
    }
}
