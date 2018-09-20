package au.edu.swin.ajass.views;

import au.edu.swin.ajass.enums.Difficulty;
import au.edu.swin.ajass.enums.QuestionType;
import au.edu.swin.ajass.models.Exam;
import au.edu.swin.ajass.models.Question;
import au.edu.swin.ajass.models.Student;
import au.edu.swin.ajass.models.Test;
import au.edu.swin.ajass.models.questions.ChoiceQuestion;
import au.edu.swin.ajass.models.questions.ImageQuestion;
import au.edu.swin.ajass.models.questions.ImmutableQuestion;
import au.edu.swin.ajass.models.questions.WritingQuestion;
import au.edu.swin.ajass.util.Utilities;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This view sits around until the exam is complete,
 * which it then takes the state of the Exam model
 * and calculates results reflecting the user's aptitude.
 * The user can view results for individual tests, as
 * well as how well they did overall.
 */
public class ResultsView extends JPanel implements IView {

    private final MainView main;
    private final HashMap<QuestionType, JComponent> results;
    private final HashMap<QuestionType, JButton> buttons;

    private final JButton overallButton;
    private JComponent overall;

    public ResultsView(MainView main) {
        this.main = main;
        results = new HashMap<>();
        buttons = new HashMap<>();
        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        buttons.put(QuestionType.MATHS, new JButton("Maths"));
        buttons.put(QuestionType.LISTENING, new JButton("Listening"));
        buttons.put(QuestionType.IMAGE, new JButton("Image"));
        buttons.put(QuestionType.SPELLING, new JButton("Spelling"));
        buttons.put(QuestionType.WRITING, new JButton("Writing"));

        // Enable nav buttons.
        for (Map.Entry<QuestionType, JButton> entry : buttons.entrySet()) {
            buttonPanel.add(entry.getValue());
            entry.getValue().addActionListener((e) -> swapResultsView(entry.getKey()));
        }

        // Enable overall results nav button.
        overallButton = new JButton("Overall Results");
        overallButton.addActionListener((e) -> swapResultsView(null));
        buttonPanel.add(overallButton);

        add(buttonPanel, BorderLayout.PAGE_START);
    }

    /**
     * Generates an individual results panel for each test that was taken.
     */
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

            JPanel result = new JPanel();
            result.setLayout(new BorderLayout());
            result.add(informationText, BorderLayout.NORTH);

            JPanel individuals = new JPanel();
            individuals.setLayout(new GridLayout(test.getQuestionsIssued() + 1, 2));

            JLabel given = new JLabel("Answer(s) Given");
            given.setFont(new Font("Arial", Font.BOLD, 16));
            JLabel correct = new JLabel("Correct Answer(s)");
            correct.setFont(new Font("Arial", Font.BOLD, 16));
            individuals.add(given);
            individuals.add(correct);

            // Show explanations for marks.
            questions = test.getQuestions();
            int i = 0;
            while (questions.hasNext()) {
                Question question = questions.next();
                i++;

                switch (question.getType()) {
                    case IMAGE:
                        // Hide second row label for Image questions.
                        individuals.getComponent(1).setVisible(false);

                        ImageQuestion imageQ = (ImageQuestion) question;
                        Image image = Utilities.image(imageQ.getImageFileLoc());

                        JPanel left = new JPanel();
                        left.setLayout(new BorderLayout());
                        JLabel userAnswerLabel = new JLabel() {
                            @Override
                            public void paintComponent(Graphics g) {
                                super.paintComponent(g);
                                Iterator<Point> answers = imageQ.getAnswer();
                                // Draw user's answers.
                                // Draw as green if correct, red if incorrect.
                                while (answers.hasNext()) {
                                    Point answer = answers.next();
                                    if (imageQ.isCorrectlyAnswered())
                                        g.setColor(Color.GREEN);
                                    else g.setColor(Color.RED);
                                    g.drawOval(answer.x - (ImageQuestion.CORRECT_RANGE / 2) + 5, answer.y - (ImageQuestion.CORRECT_RANGE / 2) + 5, ImageQuestion.CORRECT_RANGE, ImageQuestion.CORRECT_RANGE);
                                }
                                g.setColor(Color.YELLOW);
                                for (Point actual : imageQ.getAnswers())
                                    g.drawOval(actual.x - (ImageQuestion.CORRECT_RANGE / 2), actual.y - (ImageQuestion.CORRECT_RANGE / 2), ImageQuestion.CORRECT_RANGE, ImageQuestion.CORRECT_RANGE);
                            }
                        };
                        userAnswerLabel.setIcon(new ImageIcon(image));

                        left.add(userAnswerLabel, BorderLayout.CENTER);
                        individuals.add(left);
                        // Fill right size. We don't need it.
                        individuals.add(new JPanel());
                        break;
                    case WRITING:
                    case SPELLING:
                        ImmutableQuestion immuteQ = (ImmutableQuestion) question;

                        // Left-side panel should have a size restriction because JTextArea doesn't know when to stop.
                        JPanel first = new JPanel();
                        first.setLayout(new BorderLayout());

                        // Display the user's given answer. Boom.
                        JTextArea userFullAnswer = new JTextArea();
                        if (immuteQ.isAnswered())
                            userFullAnswer.setText(immuteQ.getAnswer().toString());
                        else userFullAnswer.setText("((unanswered))");
                        userFullAnswer.setEditable(false);
                        userFullAnswer.setLineWrap(true);
                        userFullAnswer.setWrapStyleWord(true);
                        first.add(userFullAnswer, BorderLayout.CENTER);

                        // Right-side panel should share the same restriction.
                        JPanel second = new JPanel();
                        second.setLayout(new BorderLayout());

                        JTextArea answerFeedback = new JTextArea();

                        if (immuteQ.isCorrectlyAnswered())
                            answerFeedback.setText(String.format("You answered this question correctly. Good job! (%.2f marks earnt)", immuteQ.getMarksEarnt()));
                        else if (immuteQ.isAnswered()) {
                            if (immuteQ instanceof WritingQuestion) {
                                StringBuilder feedback = new StringBuilder("You answered this question incorrectly. Mistakes: ");
                                for (Map.Entry<String, Integer> entry : ((WritingQuestion) immuteQ).getErrors().entrySet())
                                    feedback.append(entry.getKey()).append(" (").append(entry.getValue()).append(" mistakes), ");
                                String toPut = feedback.toString();
                                toPut = toPut.substring(0, toPut.length() - 2);
                                answerFeedback.setText(toPut);
                            } else
                                answerFeedback.setText("You answered this question incorrectly.");
                        } else
                            answerFeedback.setText("No attempt was made to answer this question.");

                        second.add(answerFeedback, BorderLayout.CENTER);
                        answerFeedback.setEditable(false);
                        answerFeedback.setLineWrap(true);
                        answerFeedback.setWrapStyleWord(true);

                        // Add both JPanels.
                        individuals.add(first);
                        individuals.add(second);
                        break;
                    case LISTENING:
                    case MATHS:
                        ChoiceQuestion choiceQ = (ChoiceQuestion) question;

                        // Left-hand column.
                        first = new JPanel();
                        first.setLayout(new FlowLayout(FlowLayout.LEFT));

                        JLabel qNum = new JLabel(String.format("Q%d", i));
                        qNum.setFont(new Font("Arial", Font.BOLD, 15));

                        // User's answer.
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
                        first.add(userAnswer);
                        first.add(marks);

                        // Add correct answer in right hand column.
                        second = new JPanel();
                        second.setLayout(new FlowLayout(FlowLayout.LEFT));
                        JLabel correctAnswer = new JLabel(prettyStringArray(choiceQ.getAnswers()));

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

            overall = generateOverallStatistics();
        }

        // Make Panels for tests that weren't taken.
        for (QuestionType type : QuestionType.values()) {
            if (results.containsKey(type)) continue;
            JPanel result = new JPanel();
            result.setLayout(new BorderLayout());

            // Give information on why there is no results for this test.
            JTextArea information = new JTextArea();
            information.setText(String.format("No information is provided for the %s test because it was not attempted.", type.toString().toLowerCase()));
            result.add(information, BorderLayout.CENTER);

            // Put it in a scroll panel to make it look sexy.
            JScrollPane scrollPanel = new JScrollPane(result, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            results.put(type, scrollPanel);
        }
    }

    /**
     * @return A special panel that displays overall statistics, and a few graphs.
     */
    private JComponent generateOverallStatistics() {
        JTextArea informationText = new JTextArea();
        Student studentInfo = main.exam().getStudentInfo();
        double totalMarks = main.exam().getExamModel().getTotalMarks();
        double marksP = (totalMarks / Exam.MAXIMUM_POSSIBLE_MARKS) * 100;
        int testsTaken = main.exam().getExamModel().getNumberOfTestsTaken();

        String information = String.format("OVERALL RESULT FOR STUDENT %s:\n\nSchool Name: %s\nMarks Obtained: %.2f/%d (%.2f%%)\nTests Taken: %d",
                studentInfo.getStudentID(), studentInfo.getSchoolName(), totalMarks, Exam.MAXIMUM_POSSIBLE_MARKS, marksP, testsTaken);
        informationText.setText(information);
        informationText.setEditable(false);

        JPanel result = new JPanel();
        result.setLayout(new BorderLayout());
        result.add(informationText, BorderLayout.NORTH);

        JPanel graphs = new JPanel();
        graphs.setLayout(new GridLayout(2, 1));

        // Generate graphs.
        graphs.add(generateBarChartStatistics());
        graphs.add(generateLineChartStatistics());

        result.add(graphs, BorderLayout.CENTER);

        return new JScrollPane(result, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    }

    /**
     * @return A bar chart reflecting performance on each individual test.
     */
    private JPanel generateBarChartStatistics() {
        JPanel result = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                // Paint background gray.
                g.fillRect(0, 0, 480, 300);
                // Draw a black border around this panel.
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, 480, 300);

                // Draw graph cross-sections.
                g.drawLine(40, 50, 440, 50);
                g.drawString("100%", 5, 50);
                g.drawLine(40, 150, 440, 150);
                g.drawString("50%", 10, 150);

                // Outlines
                g.drawLine(40, 150, 440, 150);
                g.drawLine(39, 50, 39, 250);
                g.drawLine(441, 50, 441, 250);
                g.drawLine(40, 250, 440, 250);

                Iterator<Test> iter = main.exam().getExamModel().getTests();
                int x = 40;
                int y = 50;
                while (iter.hasNext()) {
                    Test test = iter.next();

                    // Draw bar.
                    double barLength = (test.getMarksEarnt() / test.MAXIMUM_POSSIBLE_MARKS) * 200;

                    g.setColor(testColor(test.getCategory()));
                    g.fillRect(x, y + (int) (200 - barLength), 40, barLength < 5 ? 5 : (int) barLength);

                    // Draw percentage.
                    g.setColor(Color.WHITE);
                    g.drawString(String.format("%d%%", (int) (barLength / 2)), x + 5, y + 95);
                    x += 80;

                    // Draw label.
                    g.drawString(test.getCategory().toString(), x - 85, 280);
                }

                // Draw title.
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Performance In Each Test", 100, 25);
            }
        };
        result.setPreferredSize(new Dimension(450, 300));
        result.setSize(new Dimension(450, 300));
        return result;
    }

    /**
     * @return A line chart reflecting performance question-to-question over time.
     */
    private JPanel generateLineChartStatistics() {
        JPanel result = new JPanel() {
            @Override
            public void paint(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.GRAY);
                // Paint background gray.
                g.fillRect(0, 0, 480, 300);
                // Draw a black border around this panel.
                g.setColor(Color.BLACK);
                g.drawRect(0, 0, 480, 300);

                // Outlines
                g.drawLine(39, 50, 39, 250);
                g.drawLine(441, 50, 441, 250);
                g.drawLine(40, 250, 440, 250);

                Iterator<Test> iter = main.exam().getExamModel().getTests();
                int x;
                int labelx = 45;
                int y = 245;
                while (iter.hasNext()) {
                    Test test = iter.next();
                    y -= 20;
                    x = 40;

                    g.setColor(testColor(test.getCategory()));

                    Point old = null;
                    Iterator<Question> questions = test.getQuestions();
                    while (questions.hasNext()) {
                        Question question = questions.next();
                        Difficulty diff = question.getDifficulty();
                        int dy = diff == Difficulty.HARD ? -100 : diff == Difficulty.MEDIUM ? -50 : 0;

                        // Draw point.
                        g.fillOval(x - 3, y + dy - 3, 6, 6);

                        // Draw line to previous entry.
                        if(old != null)
                            g.drawLine(old.x, old.y, x, y + dy);

                        old = new Point(x, y + dy);

                        x += 34;
                    }

                    // Draw legend label.
                    g.drawString(test.getCategory().toString(), labelx, 280);
                    labelx += 75;
                }

                // Draw title.
                g.setColor(Color.WHITE);
                g.drawString("Key:", 10, 280);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Difficulty Over Time", 150, 25);
            }
        };
        result.setPreferredSize(new Dimension(450, 300));
        result.setSize(new Dimension(450, 300));
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
        // Generate the panels needed and then display the overall one.
        generateResultsPanels();
        swapResultsView(null);
    }

    /**
     * Changes which view for what results should be shown.
     * i.e. clicking the math results button should display that results panel.
     */
    private void swapResultsView(QuestionType category) {
        JComponent toSwapTo = results.getOrDefault(category, overall);
        // Remove old results view. Keep navbar.
        if (getComponents().length >= 2)
            remove(1);
        add(toSwapTo, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }
}
