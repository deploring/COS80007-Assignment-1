package au.edu.swin.ajass.views;

import au.edu.swin.ajass.models.Question;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import au.edu.swin.ajass.models.questions.ListeningQuestion;
import au.edu.swin.ajass.util.Utilities;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by sky on 21/8/18.
 */
public class TestView extends JPanel implements IView {

    // Reference back to JFrame, upper View.
    private final MainView main;
    private final ExamView exam;

    // Makes finishing screen fade after 10 seconds. Interruptible.
    private Thread fadeThread;

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

        // Submit button. Each question gives it a different action listener.
        JButton submit = new JButton("Answer");
        submit.setEnabled(false);

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
                // Stuff to add later.
                optionsPanel = new JPanel();

                // JTextField response.
                selectPrompt = new JLabel("Please enter your answer.");

                JTextArea writing = new JTextArea(10, 10);
                // Enable submit after text has been inputted
                writing.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyPressed(KeyEvent e) {
                        SwingUtilities.invokeLater(() -> submit.setEnabled(writing.getText().length() > 0));
                    }
                });
                submit.addActionListener(e -> {
                    // TODO: hint/feedback
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
            default:
                throw new NotImplementedException();
        }

        c.gridy++;
        c.anchor = GridBagConstraints.PAGE_END;
        c.weighty = 0.25;
        result.add(submit, c);
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
    public void displayFillerPanel() {
        JPanel toDisplay = new JPanel();

        // Figure out what to display.
        switch (main.exam().getExamModel().getCurrentTest().getCategory()) {
            case LISTENING:
            case SPELLING:
            case WRITING:
                JLabel label = new JLabel("Nice work!");
                label.setFont(new Font("Arial", Font.BOLD, 30));
                JLabel label2 = new JLabel("You've completed this test.");
                JLabel label3 = new JLabel("Hover over the button to see how long you took!");
                toDisplay.add(label, BorderLayout.NORTH);
                toDisplay.add(label2, BorderLayout.CENTER);
                toDisplay.add(label3, BorderLayout.SOUTH);
                break;
            case IMAGE:
            case MATHS:
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
