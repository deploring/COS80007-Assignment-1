package au.edu.swin.ajass.views;

import au.edu.swin.ajass.models.Question;
import au.edu.swin.ajass.models.Test;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import au.edu.swin.ajass.models.questions.ImageQuestion;
import au.edu.swin.ajass.models.questions.ListeningQuestion;
import au.edu.swin.ajass.models.questions.WritingQuestion;
import au.edu.swin.ajass.util.Utilities;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This is the lower half of the exam view. While a test is active,
 * it will receive questions of a supported type and create the
 * appropriate JPanel prompt for it. This allows the user to answer
 * the question, for which the answer is passed to ExamController.
 *
 * @author Joshua Skinner
 * @author Bradley Chick
 * @version 1
 * @since 0.1
 */
public class TestView extends JPanel implements IView {

    // Reference back to JFrame, upper View.
    private final MainView main;
    private final ExamView exam;

    // Makes finishing screen fade after 10 seconds. Interruptible.
    private Thread fadeThread;

    // Hold an instance of the active question so paint() can use it.
    private Question activeQuestion;

    public TestView(MainView main, ExamView exam) {
        this.main = main;
        this.exam = exam;
        setLayout(new BorderLayout());
    }

    /**
     * Creates a JPanel for a Question.
     * All of the event handling has been coded in, so it can simply
     * be displayed and then left to answer by the user.
     *
     * @param question Question to craft a JPanel out of.
     * @return Resulting JPanel.
     */
    private JPanel createQuestionPanel(Question question, int questNo) {
        // Keep an instance of active question for paint().
        activeQuestion = question;

        // Return instance of current test for utility purposes.
        Test currentTest = main.exam().getExamModel().getCurrentTest();

        JPanel result = new JPanel();
        result.setPreferredSize(getPreferredSize());
        result.setLayout(new GridBagLayout());
        result.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));

        // Basic gridbag constraints to fix components top to bottom.
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = 0;
        c.weighty = 0.05;
        result.add(new JPanel(), c);

        // Question Number
        JPanel questionNoPanel = new JPanel();
        JLabel questionNo = new JLabel(String.format("Question Number %d/%d", questNo, question.getType().getMaxQuestions()));
        questionNo.setFont(new Font("Arial", Font.BOLD, 22));
        questionNoPanel.add(questionNo);
        c.gridy++;
        result.add(questionNoPanel, c);

        // Prompt
        JPanel promptPanel = new JPanel();
        JTextArea prompt = new JTextArea(question.getPrompt(), 2, 20);
        prompt.setEditable(false);
        prompt.setLineWrap(true);
        prompt.setWrapStyleWord(true);
        prompt.setOpaque(false);
        promptPanel.add(prompt);
        c.gridy++;
        c.weighty = 0;
        c.fill = GridBagConstraints.BOTH;
        result.add(promptPanel, c);

        // Panel for submit buttons.
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        // Submit button. Each question gives it a different action listener.
        JButton submit = new JButton("Answer");
        submit.setEnabled(false);

        // Done button. Enables after answering at least one question. Allows user to finish test early.
        JButton done = new JButton("Finish");
        done.setEnabled(main.exam().getExamModel().getCurrentTest().getQuestionsIssued() > 1);

        done.addActionListener((e) -> {
            int confirm = JOptionPane.showConfirmDialog(null, "You are about to finish this test early. Is this what you want?", "Finish Test Early", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION)
                if (currentTest.isActive())
                    exam.testCompleted(true);
        });

        c.fill = GridBagConstraints.NONE;
        switch (question.getType()) {
            case MATHS:
            case LISTENING:
                // These are both instances of ChoiceQuestion.
                ChoiceQuestion choiceQ = (ChoiceQuestion) question;
                int noOfAnswers = choiceQ.getAnswers().length;
                int noOfChoices = choiceQ.getChoices().length;

                // Keep track of ChoiceQuestion answers!
                Set<String> answers = new HashSet<>();

                // Stuff to add later.
                JLabel selectPrompt;
                JComponent[] toAdd;
                JPanel optionsPanel = new JPanel();
                optionsPanel.setLayout(new FlowLayout());
                if (noOfChoices == 0) {
                    // JTextField response.
                    selectPrompt = new JLabel("Please enter your answer.");

                    JTextField response = new JTextField(5);
                    // Enable submit after text has been inputted
                    response.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyPressed(KeyEvent e) {
                            SwingUtilities.invokeLater(() -> submit.setEnabled(response.getText().length() > 0));
                        }
                    });
                    submit.addActionListener(e -> {
                        answers.add(response.getText());
                        main.exam().attemptAnswer(answers);
                        exam.finaliseQuestionResponse();
                    });
                    toAdd = new JComponent[]{response};
                } else {
                    if (noOfAnswers == 1) {
                        // Radio selection response.
                        selectPrompt = new JLabel("Please select the correct answer.");

                        // Create button group.
                        toAdd = new JRadioButton[choiceQ.getChoices().length];
                        ButtonGroup choices = new ButtonGroup();

                        int i = 0;
                        for (String choice : choiceQ.getChoices()) {
                            JRadioButton button = new JRadioButton(choice);
                            choices.add(button);
                            toAdd[i] = button;
                            // Enable submit once a selection has been made
                            ActionListener listener = e -> SwingUtilities.invokeLater(() -> submit.setEnabled(true));
                            button.addActionListener(listener);
                            i++;
                        }
                        submit.addActionListener(e -> {
                            JRadioButton selected = null;
                            for (JRadioButton button : (JRadioButton[]) toAdd)
                                if (button.isSelected()) {
                                    selected = button;
                                    break;
                                }
                            answers.add(selected == null ? "" : selected.getText());
                            main.exam().attemptAnswer(answers);
                            exam.finaliseQuestionResponse();
                        });
                    } else {
                        // Checkbox selection response.
                        selectPrompt = new JLabel("Please select the correct answers.");

                        toAdd = new JCheckBox[choiceQ.getChoices().length];
                        ItemListener listener = new ItemListener() {
                            int totalSelected = 0;

                            public void itemStateChanged(ItemEvent e) {
                                if (e.getStateChange() == ItemEvent.SELECTED)
                                    totalSelected++;
                                else if (e.getStateChange() == ItemEvent.DESELECTED)
                                    totalSelected--;
                                submit.setEnabled(totalSelected > 0);
                            }
                        };

                        int i = 0;
                        for (String choice : choiceQ.getChoices()) {
                            // Count how many checkboxes are selected and only enable submit if one has been selected.
                            JCheckBox check = new JCheckBox(choice);
                            check.addItemListener(listener);
                            toAdd[i] = check;
                            i++;
                        }

                        submit.addActionListener(e -> {
                            // Add all selected checkboxes to answer.
                            for (JCheckBox box : (JCheckBox[]) toAdd)
                                if (box.isSelected()) answers.add(box.getText());
                            main.exam().attemptAnswer(answers);
                            exam.finaliseQuestionResponse();
                        });
                    }
                }

                // Show button to play sound.
                if (question instanceof ListeningQuestion) {
                    JButton listen = new JButton();
                    listen.setIcon(new ImageIcon(Utilities.image("speaker.png")));
                    listen.addActionListener(e -> Utilities.playSound(((ListeningQuestion) question).getSoundFileLoc()));

                    c.gridy++;
                    result.add(listen, c);
                }

                c.gridy++;
                result.add(selectPrompt, c);
                for (JComponent add : toAdd)
                    optionsPanel.add(add);
                c.gridy++;
                c.weighty = 0.05;
                c.weightx = 0.05;
                // Stop text box from collapsing in on itself
                result.add(optionsPanel, c);
                c.fill = GridBagConstraints.NONE;
                c.weightx = 0;
                c.weighty = 0;
                break;
            case SPELLING:
                // Stuff to add later.
                optionsPanel = new JPanel();

                // JTextField response.
                selectPrompt = new JLabel("Please enter your answer.");

                JTextField response = new JTextField(5);
                // Enable submit after text has been inputted
                response.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        SwingUtilities.invokeLater(() -> submit.setEnabled(response.getText().length() > 0));
                    }
                });
                submit.addActionListener(e -> {
                    main.exam().attemptAnswer(response.getText());
                    exam.finaliseQuestionResponse();
                });
                toAdd = new JComponent[]{response};

                c.gridy++;
                result.add(selectPrompt, c);
                for (JComponent add : toAdd)
                    optionsPanel.add(add);
                c.gridy++;
                c.weighty = 0.05;
                c.weightx = 0.05;
                // Stop text box from collapsing in on itself
                result.add(optionsPanel, c);
                c.fill = GridBagConstraints.NONE;
                c.weightx = 0;
                c.weighty = 0;
                break;
            case WRITING:
                WritingQuestion writingQ = (WritingQuestion) question;
                // Stuff to add later.
                optionsPanel = new JPanel();

                // JTextArea response.
                selectPrompt = new JLabel("Please enter your answer.");
                JTextArea writing = new JTextArea();
                writing.setWrapStyleWord(true);
                writing.setLineWrap(true);
                writing.setPreferredSize(new Dimension(400, 200));

                // Enable submit after text has been inputted
                writing.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        SwingUtilities.invokeLater(() -> submit.setEnabled(writing.getText().length() > 0));
                    }
                });
                submit.addActionListener(e -> {
                    if (!writingQ.isHinted()) {
                        HashMap<String, Integer> incorrect = writingQ.hint(writing.getText());
                        if (incorrect.size() > 0) {
                            // Their answer is incorrect, provide a hint at the cost of half marks.
                            writingQ.deductHalfMarks();
                            StringBuilder warningMessage = new StringBuilder("Your answer was incorrect. \nYou will now be given an opportunity to\nreview and correct the errors below.\nHalf-marks will be awarded if you can fix\nthe issue within ten seconds:\n");
                            for (Map.Entry<String, Integer> entry : incorrect.entrySet())
                                warningMessage.append(entry.getKey()).append(" (").append(entry.getValue()).append(" errors)\n");
                            JOptionPane.showMessageDialog(null, warningMessage.toString(), "Hint", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    main.exam().attemptAnswer(writing.getText());
                    exam.finaliseQuestionResponse();
                });
                toAdd = new JComponent[]{writing};

                c.gridy++;
                result.add(selectPrompt, c);
                for (JComponent add : toAdd)
                    optionsPanel.add(add);
                c.gridy++;
                c.weighty = 0.05;
                c.weightx = 0.05;
                // Stop text box from collapsing in on itself
                result.add(optionsPanel, c);
                c.fill = GridBagConstraints.NONE;
                c.weightx = 0;
                c.weighty = 0;
                break;
            case IMAGE:
                // Retrieve image to show.
                ImageQuestion imageQ = (ImageQuestion) question;
                Image image = Utilities.image(imageQ.getImageFileLoc());
                JLabel imageLabel = new JLabel();
                imageLabel.setIcon(new ImageIcon(image));

                // Implement clear button.
                JButton reset = new JButton("Reset");

                reset.addActionListener((e) -> {
                    imageQ.resetAnswers();
                    repaint();
                });

                imageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        Point clicked = e.getPoint();
                        Point point = imageLabel.getLocation();

                        double absoluteX = clicked.getX() - point.getX();
                        double absoluteY = clicked.getY() - point.getY();
                        // Calculate absolute point clicked on the image.
                        Point absolutePoint = new Point((int) absoluteX + 5, (int) absoluteY + 5);

                        imageQ.answer(absolutePoint);
                        repaint();
                    }
                });

                // Add image to frame.
                optionsPanel = new JPanel();
                optionsPanel.add(imageLabel);
                c.gridy++;
                c.weighty = 0.05;
                c.weightx = 0.05;
                result.add(optionsPanel, c);
                c.weightx = 0;
                c.weighty = 0;
                break;
            default:
                throw new NotImplementedException();
        }

        buttonPanel.add(submit);
        buttonPanel.add(done);
        c.gridy++;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weighty = 0.25;
        result.add(buttonPanel, c);
        c.gridy++;
        c.weighty = 0.5;
        result.add(new JPanel(), c);
        return result;
    }

    /**
     * Displays a filler panel for 10 seconds after completing a test.
     * The user, however, may start another test before the 10 seconds
     * has elapsed.
     */
    void displayFillerPanel() {
        JPanel toDisplay = new JPanel();

        // Figure out what to display.
        switch (main.exam().getExamModel().getCurrentTest().getCategory()) {
            case LISTENING:
            case SPELLING:
            case WRITING:
            case MATHS:
                JLabel label = new JLabel("Nice work!");
                label.setFont(new Font("Arial", Font.BOLD, 30));
                JLabel label2 = new JLabel("You've completed this test.");
                JLabel label3 = new JLabel("Hover over the button to see how long you took!");
                toDisplay.add(label, BorderLayout.NORTH);
                toDisplay.add(label2, BorderLayout.CENTER);
                toDisplay.add(label3, BorderLayout.SOUTH);
                break;
            case IMAGE:
                toDisplay.setLayout(new FlowLayout());
                ImageIcon toShow = new ImageIcon(Utilities.image("goodluck.png"));
                JLabel imageLabel = new JLabel();
                imageLabel.setIcon(toShow);
                toDisplay.add(imageLabel, BorderLayout.CENTER);
                break;
        }

        removeAll();
        add(toDisplay);
        repaint();
        revalidate();

        fadeThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
                for (Component comp : getComponents()) {
                    if (comp.equals(toDisplay)) {
                        removeAll();
                        repaint();
                        revalidate();
                    }
                }
            } catch (InterruptedException e) {
                // Doesn't matter if interrupted
            }
        });
        fadeThread.start();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (activeQuestion != null) {
            switch (activeQuestion.getType()) {
                case IMAGE:
                    //TODO: Display clicks
                    break;
            }
        }
    }

    @Override
    public void onDisplay() {
        // Interrupt fadeThread if needed.
        if (fadeThread != null && !fadeThread.isInterrupted())
            fadeThread.interrupt();

        // Calling onDisplay in TestView prompts it to display a generated JPanel with a question prompt.
        Question current = main.exam().getExamModel().getCurrentTest().getCurrentQuestion();
        int questionNumber = main.exam().getExamModel().getCurrentTest().getQuestionsIssued();
        // Remove all current stuff.
        removeAll();
        add(createQuestionPanel(current, questionNumber), BorderLayout.CENTER);
        // Re-paint and re-layout the container.
        repaint();
        validate();
        // Make test timer visible.
        main.testTimer.setVisible(true);
        // Question has been prompted. Make it do stuff if needed.
        current.onPrompted();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
