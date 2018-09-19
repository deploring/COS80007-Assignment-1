package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Question;
import au.edu.swin.ajass.models.Test;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import au.edu.swin.ajass.util.Utilities;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by sky on 21/8/18.
 */
public class ResultsView extends JPanel implements IView {

    private final MainView main;
    private final HashMap<QuestionType, JComponent> results;
    private final HashMap<QuestionType, JButton> buttons;
    private final JButton overallButton;

    public ResultsView(MainView main) {
        this.main = main;
        results = new HashMap<>();
        buttons = new HashMap<>();
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttons.put(QuestionType.MATHS, new JButton("Maths Results"));
        buttons.put(QuestionType.LISTENING, new JButton("Listening Results"));
        buttons.put(QuestionType.IMAGE, new JButton("Image Results"));
        buttons.put(QuestionType.SPELLING, new JButton("Spelling Results"));
        buttons.put(QuestionType.WRITING, new JButton("Writing Results"));

        for (JButton toAdd : buttons.values())
            buttonPanel.add(toAdd);

        overallButton = new JButton("Overall Results");
        buttonPanel.add(overallButton);

        add(buttonPanel, BorderLayout.PAGE_START);
    }

    private void generateResultsPanels() {
        Iterator<Test> tests = main.exam().getExamModel().getTests();
        while (tests.hasNext()) {
            Test test = tests.next();
            if (results.containsKey(test.getCategory()))
                throw new IllegalStateException("The same test was taken twice?");
            // Display general results for the test. Same for every test.
            JTextArea informationText = new JTextArea();
            int totalQuestions = test.getMaxQuestions();
            int questionsIssued = test.getQuestionsIssued();
            int questionsAnswered = 0;
            int questionsCorrect = 0;
            Iterator<Question> questions = test.getQuestions();
            while (questions.hasNext()) {
                Question question = questions.next();
                if (question.isAnswered()) questionsAnswered++;
                if (question.isCorrectlyAnswered()) questionsCorrect++;
            }
            String timeTaken = Utilities.digitalTime(test.getTimeElapsed());
            double marksAccrued = test.getMarksEarnt();
            int maximumMarks = test.MAXIMUM_POSSIBLE_MARKS;

            // Percentages
            double overallMark = (marksAccrued / (double) maximumMarks) * 100;
            double questionsAnsweredP = ((double) questionsAnswered / (double) totalQuestions) * 100;
            double correctP = ((double) questionsCorrect / (double) questionsIssued) * 100;
            double totalContribution = (marksAccrued / main.exam().getExamModel().getTotalMarks()) * 100;

            String information = String.format("RESULTS FOR %s TEST:\n\nNo. of questions: %d\nNo. of questions issued: %d (%d were answered (%d%%))\nQuestions correctly answered: %d (%d%%)\nMarks accrued from this test: %.2f/%d (%d%% of possible marks)\nContribution to overall mark score: %d%%\nTime taken: %s",
                    test.getCategory().toString(), totalQuestions, questionsIssued, questionsAnswered, (int) questionsAnsweredP, questionsCorrect, (int) correctP, marksAccrued, maximumMarks, (int) overallMark, (int) totalContribution, timeTaken);
            informationText.setText(information);
            informationText.setEditable(false);
            informationText.setOpaque(false);

            JPanel result = new JPanel();
            result.setLayout(new BorderLayout());
            result.add(informationText, BorderLayout.NORTH);

            JPanel individuals = new JPanel();
            individuals.setLayout(new GridLayout(test.getQuestionsIssued(), 2));

            // Show explanations for marks.
            questions = test.getQuestions();
            int i = 0;
            while (questions.hasNext()) {
                Question question = questions.next();
                i++;

                switch (question.getType()) {
                    case LISTENING:
                    case MATHS:
                        ChoiceQuestion choiceQ = (ChoiceQuestion) question;

                        JPanel first = new JPanel();
                        first.setLayout(new FlowLayout(FlowLayout.LEFT));

                        JLabel qNum = new JLabel(String.format("Q%d", i));
                        qNum.setFont(new Font("Arial", Font.BOLD, 15));

                        // User's answer.
                        JLabel given = new JLabel("Answer given: ");
                        JLabel userAnswer;
                        if (choiceQ.isAnswered())
                            userAnswer = new JLabel(prettyStringArray(choiceQ.getAnswer()));
                        else userAnswer = new JLabel("Unanswered");

                        // Set appropriate answer color.
                        if (choiceQ.isCorrectlyAnswered())
                            userAnswer.setForeground(Color.GREEN);
                        else
                            userAnswer.setForeground(Color.RED);

                        // Marks earnt for the question.
                        JLabel marks = new JLabel(String.format(" (%.2f marks)", choiceQ.getMarksEarnt()));

                        first.add(qNum);
                        first.add(given);
                        first.add(userAnswer);
                        first.add(marks);

                        // Add correct answer in right hand column.
                        JPanel second = new JPanel();
                        second.setLayout(new FlowLayout(FlowLayout.LEFT));

                        JLabel correct = new JLabel("Correct answer: ");
                        JLabel correctAnswer = new JLabel(prettyStringArray(choiceQ.getAnswers()));

                        second.add(correct);
                        second.add(correctAnswer);

                        // Add both JPanels.
                        individuals.add(first);
                        individuals.add(second);
                        break;
                    default:
                        throw new NotImplementedException();
                }
            }

            result.add(individuals, BorderLayout.CENTER);

            JScrollPane scrollPanel = new JScrollPane(result, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            results.put(test.getCategory(), scrollPanel);
        }

        // Make Panels for tests that weren't taken.
        for (QuestionType type : QuestionType.values()) {
            if (results.containsKey(type)) continue;
            JPanel result = new JPanel();
            result.setLayout(new BorderLayout());
            result.add(new JLabel("There is no results information available for this test, as it was not taken. :("));
            results.put(type, result);
        }
    }

    private JPanel generateBarChartStatistics() {
        JPanel result = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                // Paint background gray.
                g.fillRect(0, 0, 300, 300);
                // Draw a black border around this panel.
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, 300, 300);
                Iterator<Test> iter = main.exam().getExamModel().getTests();
                int x = 40;
                int y = 50;
                while (iter.hasNext()) {
                    Test test = iter.next();

                    // Draw bar.
                    double barLength = (test.getMarksEarnt() / test.MAXIMUM_POSSIBLE_MARKS) * 200;
                    g.setColor(testColor(test.getCategory()));
                    g.fillRect(x, y + (int) (200 - barLength), 40, (int) barLength);

                    // Draw percentage.
                    g.setColor(Color.BLACK);
                    g.drawString(String.format("%d%%", (int) (barLength / 2)), x + 5, y + 100);
                    x += 80;
                }
            }
        };
        result.setPreferredSize(new Dimension(300, 300));
        result.setSize(new Dimension(300, 300));
        return result;
    }

    /**
     * Gives each test a unique color for each result chart generated.
     *
     * @param category Test category.
     * @return Color for that category.
     */
    private Color testColor(QuestionType category) {
        switch (category) {
            case MATHS:
                return new Color(255, 102, 102);
            case LISTENING:
                return new Color(255, 255, 102);
            case IMAGE:
                return new Color(153, 255, 51);
            case SPELLING:
                return new Color(51, 204, 255);
            case WRITING:
                return new Color(255, 153, 51);
            default:
                throw new IllegalArgumentException("Unsupported question category");
        }
    }

    /**
     * @return String[] converted into a pretty format.
     */
    private String prettyStringArray(String[] array) {
        if (array.length == 0) return "None";
        else if (array.length == 1) return array[0];
        else {
            StringBuilder string = new StringBuilder("[");
            for (String in : array)
                string.append(in).append(array[array.length - 1].equals(in) ? "" : ", ");
            return string.append("]").toString();
        }
    }

    @Override
    public void onDisplay() {
        generateResultsPanels();
        //JPanel bar = generateBarChartStatistics();
        add(results.get(QuestionType.MATHS), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
