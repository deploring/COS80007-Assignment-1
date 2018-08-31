package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.UIState;

import javax.swing.*;
import java.awt.*;

/**
 * This view is what the user will first see when they
 * open the program. It is a list of terms and conditions
 * that the user must agree to before using the software.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public class TCView extends JPanel implements IView {

    // Reference back to JFrame, other Views
    private final MainView main;

    // Hard-coded T&C text.
    private final String termsText;

    // View Elements.
    private final JTextArea terms;
    private final JScrollPane termsScroll;
    private final JCheckBox agreed;
    private final JLabel display;
    private final JButton submit;

    TCView(MainView main) {
        // Reference back to MainView (JFrame)
        this.main = main;

        /*  -- SOURCED CODE AND/OR TEXT --
            Source: http://simpleeulas.weebly.com/fair-eulas.html
            We take absolutely no responsibility for the writing of this EULA.
            It has been modified and inserted into our program to fill space on the T&C screen. */
        termsText = "TERMS & CONDITIONS\n\n1. We grant you usage of this software on a single computer. You may create backups to the software but do not circulate/distribute them in public. If you do not agree to the following terms of this license, please uninstall and remove all copies of this software.\n\n2. You may install and use the software on another computer, but the software may not be in use on more than one computer at a time unless authorised. You may make back-up copies of the software for archival purposes. You may not transfer the right to use the software to another party. Exceptions may be made if authorised by the software developers.\n\n3. The software is protected by the copyright laws of Australia and other countries, and we retain all intellectual property rights in the software. You may not separately publish, sell, market, distribute, lend, lease, rent, or sublicense the software code. However, this license is not to be construed as prohibiting or limiting any fair use sanctioned by copyright law, such as permitted library and classroom usage or reverse engineering.\n\n4. We warrant that the software will provide the features and functions generally described in the product specification and in the product documentation.\n\n5. We have taken reasonable steps to keep the software free of viruses, spyware, \"back door\" entrances, or any other harmful code. We will not track or collect any information about you, your data, or your use of the software except as you specifically authorize. The software will not download or install patches, upgrades, or any third party software without getting your permission. We will not intentionally deprive you of your ability to use any features of the software or access to your data.\n\n6. We do not warrant that the software or your ability to use it will be uninterrupted or error-free. To the extent permitted by applicable law, we disclaim any implied warranty of merchantability or fitness for a particular purpose.\n\n7. If any part of this agreement is found to be invalid or unenforceable, the remaining terms will stay in effect. This agreement does not prejudice the statutory rights of any party dealing as a consumer.\n\n8. This agreement does not supersede any express warranties we made to you. Any modification to this agreement must be agreed to in writing by both parties.\n\n9. This agreement will apply from the date of the installation of the software.\n\n10. Wow you actually read this far. Here, have a cookie!";
        /*  -- END OF SOURCED CODE AND/OR TEXT --  */

        setLayout(new GridLayout(2, 3));

        // Elements
        terms = new JTextArea(termsText);
        terms.setLineWrap(true);
        terms.setWrapStyleWord(true);
        terms.setAlignmentX(JTextArea.CENTER_ALIGNMENT);
        terms.setEditable(false);
        terms.setOpaque(true);
        termsScroll = new JScrollPane(terms, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        display = new JLabel("To continue, agree to the T&C.");
        display.setForeground(new Color(234, 98, 98));
        agreed = new JCheckBox("I agree to the T&C.");
        submit = new JButton("Submit");

        // Placement
        JPanel bottomMiddle = new JPanel();
        bottomMiddle.setLayout(new GridLayout(3, 1));
        bottomMiddle.add(new JPanel());
        JPanel agreePanel = new JPanel();
        bottomMiddle.add(agreePanel);
        agreePanel.setLayout(new FlowLayout());
        agreePanel.add(display);
        agreePanel.add(agreed);

        JPanel bottomRight = new JPanel();
        JPanel fill = new JPanel();
        fill.setLayout(new FlowLayout());
        bottomRight.setLayout(new BorderLayout());
        bottomRight.add(fill, BorderLayout.CENTER);
        fill.add(submit);

        // Add stuff to the grid in the required order.
        JComponent[] topGrid = new JComponent[]{new JPanel(), termsScroll, new JPanel(), new JPanel(), bottomMiddle, bottomRight};
        for (JComponent toAdd : topGrid)
            add(toAdd);

        // Listeners
        submit.addActionListener(e -> {
            if (agreed.isSelected())
                main.update(UIState.PINGEN);
            else
                JOptionPane.showMessageDialog(null, "You must agree to the Terms and Conditions before proceeding!");
        });
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
