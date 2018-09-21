- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Index
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

This file contains:
	- Team Member's Information
	- Instructions to run the assignment
	- List of known bugs
	- Help received from outside sources
	- Itemization of assessment items
	- Estimated Marks
	- Additional Discussion


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Team Member's Information
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

Name:		Bradley Chick
Student ID:	101 626 151 
Subject ID:	COS80007
		Undergradute

Name:		Joshua Skinner
Student ID:	101 601 828
Subject ID:	COS80007
		Undergraduate

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Instructions to run the assignment
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

1. Download and Install "Maven". Follow their instructions for installation.
    - http://maven.apache.org/install.html
2. Navigate to the project's root directory in a command prompt/terminal, where "src" is located.
3. Type "mvn clean install".
4. Wait a while, especially if it is your first time compiling something with Maven.
5. Compiled JAR file is located in target/.
6. Click to run the JAR file, or type "java -jar AJASS1.jar" after navigating into "target/".

7x. If you are unable to compile the program, a pre-compiled AJASS1.jar will be included in the project root directory.

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		List of known bugs
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

- Exam Timer does not stop sometimes, even when interrupted. (doesn't matter however, as you are already in the results view by then)
- Calculating Writing Results sometimes does not work. It produces a NullPointerException and we do not know why.
- Finishing tests in an oddly specific way causes the UI to glitch out (not major)

- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Helped Received from outside sources
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

We have used no additional code from outside sources.

However, we have used this sound effect:
	"Metronome" Sound Effect
	http://soundbible.com/914-Metronome.html

And, we have used this website for and Text to Speech components
of the application:
	Text to Speech Voice Generation
	http://www.fromtexttospeech.com/

And, we have used this website for a EULA template:
    http://simpleeulas.weebly.com/fair-eulas.html


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Itemization of assessment items
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
/----------/
/  Design: /
/----------/
- A UML class diagram depicting the structure and relationships of objects
  within the application
  See: UML Class Diagram.png

- Flowcharts showing the logic behind:
	- Temporary PIN Generation
	- Activating The Test
	- Adaptively Prompting Questions
	- Recording Data
	- Producing Test Results
  See: Flowcharts.pdf


- Use of MVC (Model/View/Controller)

	Models, Views and Controllers are used to separate the internal
	representations of data from how the data is presented and
	manipulated by the user.
	
	The benefit of using MVC is that there is high cohesion (logical
	grouping of related actions) between controllers, and the same
	with the models.
	
	MVC also reduces the amount of coupling between methods.
	
	Modifications can be made much more easily with MVC, because
	responsibilities of the application are separated, so changes
	in one view, model or controller, will not adversely affect
	other components. This also allows multiple people to work on
	the same project, at the same time.


/-----------/
/  Content: /
/-----------/
++++++++++++++++++++++++++++++++++++++++
Login:
- User must conform to Terms and Conditions before gaining access to the 
  application

- A student must enter his/her ID, name, school details to receive a PIN

PIN:

- A PIN is generated for the user, before they can enter the exam
- PIN is unique and randomly generated
- PIN has a time limit of 20 minutes

++++++++++++++++++++++++++++++++++++++

Exam:
- The exam has 5 categories 
	- Math
	- Recognising critical information from a given image
	- Spelling
	- Listening
	- Writing

- Exam has 15 minutes to be completed

- Each Test Category are considered completed after:
	- Math: 3 minutes
	- Recognising critical information from a given image: 1.5 minutes
	- Spelling: 3 minutes
	- Listening: 3 minutes
	- Writing: 3 minutes

- When a test category has been selected, the remaining categories can
  be seen but cannot be clicked.
- A fast-track student can finish a test ahead of time by clicking 'Finish' button
- The 'Finish' button becomes visible after at least one question has been completed

- Questions have three difficulties: Hard, Harder, Hardest
- If the user gets a question correct, they will receive a question of harder
  difficulty
- If the user gets a question incorrect, they will receive a question of
  easier difficulty
- If the user is already at the Hard difficulty and get a question incorrect,
  they will receive another Hard question.
- If the user is already at the Hardest difficulty and get a question correct,
  they will receive another Hardest question.

- Questions are randomly selected from a question bank, based on the test category
  and question difficulty
- User will not receive the same question more than once

- Hard questions are worth 2 marks
- Harder questions are worth 5 marks
- Hardest questions are worth 10 marks

The time remaining for the exam can be seen at all times.

All test categories have a timer to see time remaining. If the
time is soon to run out, an alert sound will play.

After a test has been completed, the user can click the 'finish'
button to end the test, or wait for the time to expire.

+++++++++++++++++++++++++++++++

Recognising critical information from a given image:

In this category, student must recognise critical information in an image
from a prompt

The images will be:
Hazy:	2 Marks - 15% blur
Hazier:	5 Marks - 20% blur
Haziest:10 Marks - 25% blur

After the completion of the test "Good Luck!" image will display
on screen for 10 seconds

+++++++++++++++++++++++++++++++

Writing:

There are three types of writing questions:

Simple: 2 Marks
Simple questions must be: 
     * The first letter of the first word is capitalised.
     * No words contain out-of-place capitalisation (except for first character in each word).
     * There is a full stop at the end of the sentence, and only one full stop.
     * There are no numeric characters used.
     * At least five words are used.

Compound: 5 Marks
Compound questions must be:
     * The existing sentence has been classed as "simple".
     * A conjunction is used.
     * A comma is used.
     * At least ten words are used.

Complex: 10 Marks
Complex questions must be:
     * The existing sentence has been classed as "compound".
     * A relative pronoun is used.
     * A word is capitalised that isn't at the beginning of the sentence.
     * At least fifteen words are used.

If there is any mistake detected by the software, the software
will give the student clues by prompting them with the errors
the program has detected; if the correction is done in less than
10 sec, half of the allotted marks will be awarded.

+++++++++++++++++++++++++++++++
Listening:
Student must select keyword(s) from one-sentence, two-sentence, and
three-sentence voices of diverse accents.
One sentence:	2 marks
Two sentence:	5 marks
Three sentence:	10 marks

If the student has had accumulated overall 10 marks, an Opt for Extra
Time button will be visible, allowing the student to repeat the 
listening test for 120 seconds at the cost of 5 marks deducted 
from the accumulated mark.

++++++++++++++++++++++++++++++

Spelling:
In spelling students are prompted to spell positive words staring with
"G" = 2 marks
"J" = 5 marks
"Z" = 10 marks
If the user does not respond within 10 seconds there will be a popup
window that prompts the first 3 characters of a desired word for 5 seconds.
If the user answers correct they will be awarded with half the allotted
marks.
++++++++++++++++++++++++++++++


At the end of the test, the panel containing all the buttons will be
replaced with a text message, showing the end of all the tests.

Results:

At the end of the test, all questions with the users answers are displayed.
Users can see if they have answered correct and what the correct answer were.
Other statistics include, number of questions answers, percentage completion,
marks achieved in each test, overall percentage mark.

There are also bar charts and line graphs to depict how the user achieved on
the test.

/-----------------------------------------------/
/  Swing/JavaFX + other advanced Java features: /
/-----------------------------------------------/

List of all Swing/JavaFX stuff used:
    JPanels:
        - Used in all views. Used to space and structure stuff.
    JFrame:
        - Used in MainView to display stuff on screen.
    JMenu, JMenuItem, etc.:
        - Used to display help dialogs, time remaining, etc.
    Listeners:
        - ActionListener
        - KeyListener
        - MouseListener
        - ItemListener
        - Used generally throughout the GUI of the program for event handling.
    JComponents:
        - JButton
        - JLabel
        - JTextField
        - JTextArea
        - JCheckBox
        - JRadioButton
    Layout Managers:
        - GridBagLayout
        - GridBagConstraints
        - BorderLayout
        - GridLayout
        - FlowLayout
        - Used generally throughout program to structure stuff.
    JOptionPane:
        - Used to display dialog messages.

List of Advanced Java features used:
    Threads:
        - Used for the test and exam timers.
        - Used to delay things from happening, like the spelling message hint.
    Data Structures:
        - Maps (nested HashMap to store lists of questions by category and difficulty)
        - LinkedList (to store tests and questions in order)
        - Set (to store question answers)
        - Iterators (over tests and questions)
        - Collections methods (shuffle)
        - Arrays methods (asList)
    File Handling:
        - questions.json (QuestionCache question loading)
        - Loading Image Files
        - Loading Sound Files
    Lambdas:
        - Used to shorten anonymous class declarations.
    Inner Classes
    UML Diagram



/-------------------------/
/  Use of Graphics2D API: /
/-------------------------/

Used in:
    - ImageQuestion click rendering.
    - ImageQuestion results rendering.
    - Bar chart results rendering.
    - Line chart results rendering.

/-----------------------------------------/
/  Formally written word-version report:  /
/-----------------------------------------/

A formally written word report has been included within the file system.
This document outlines and discusses scenarios relating to the completion
of an exam, while providing screenshots and code snippets with explanations
on how the programming achieves desired features.

See: Report.pdf

/-----------------/
/  New features:  /
/-----------------/


/----------/
/  Video:  /
/----------/

The video gives a run through on how an exam can be completed, while giving
explanations to how the code achieves certain criteria stated within the
assignment.

LINK: https://www.youtube.com/watch?v=xWB3EjmfMz4


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Estimated marks
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

-------------------------------------------------------------------------

1. Provide UML diagrams, showing and justifying association, composition,
   and aggregation wherever appropriate. 

Estimated marks:  10 / 10

Reasoning:
A clear, fully completed UML diagram has been submitted. Proper UML
formatting standards have been followed.
See: UML.pdf

-------------------------------------------------------------------------

2. Provide flowcharts, showing the logic for temporary PIN generation, 
   activating the test, adaptively prompting the questions, recording the
   data, and producing the test result. 

Estimated marks:  10 / 10

Reasoning:
A clear, fully completed flowchart has been submitted for each prompted
component. Correct standards have been followed when designing the 
flowcharts.
See: Flowcharts.pdf

-------------------------------------------------------------------------

3. Relate your work with Model Controller View (MVC). 

Estimated marks:  10 / 10

Reasoning:
The concept of using MVC has been followed religiously in this application.
Justification, with advantages, for using MVC has been stated in both 
this readme file and the formally written report. Naming conventions of 
files also include whether it is a model, view or controller.

See: In this document 'Itemization of assessment items > Design'
	and
     Report.pdf
	and
     UML.pdf

-------------------------------------------------------------------------

4. Use Swing/JavaFx and advanced java features as far as possible to 
   implement each of the 5 categories. 

Estimated marks:  35 / 50

Reasoning: Since we are both undergrads fresh out of OOP, we believe we have
used Swing/JavaFX to a fair extent, but not enough to warrant maximum or even
high marks. Based on our current knowledge and utilisation of the features
presented over the last few weeks, we have agreed that 35 is a good estimate.

-------------------------------------------------------------------------

5. Use event handling. 

Estimated marks:  10 / 10

Reasoning: We have used event handling where it is necessary throughout
the entire UI to ensure that all the functions required to fulfill the
purpose of the software do what we have intended them to do.

-------------------------------------------------------------------------

6. Make sure usability, meaning there must be clear and logically      
   organized user interface and functionality. 

Estimated marks:  8 / 10

Reasoning:
We have focused predominantly on getting the functionality of the applciation
working, over having proper usability, in regards to having an organized
user interface. We believe it flows correctly, but is not good enough for
full marks.

-------------------------------------------------------------------------

7. Use Graphics2D API as far as possible. 

Estimated marks:  8 / 10

Reasoning: Graphics2D is an outdated, primitive API which we believe should be
reserved for Applets and other low-level learning. We believe we have made as
much practical use of it as possible, through the means of ImageQuestions,
as well as drawing the graphs in the overall results.

-------------------------------------------------------------------------

8. Include reasonable documentation according to the Javadoc standards. 

Estimated marks:  9 / 10

Reasoning: Every single class and method are meaningfully documented. We
have attempted to follow the structure of Javadoc as much as possible but
still feel it is lacking in some minor areas, such as HTML formatting.

-------------------------------------------------------------------------

9. Implement Terms and Condition entry screen before receiving the 
   temporary PIN, and  an �About� dialog activated from the menu. 

Estimated marks:  10 / 10

Reasoning:
A fully functioning Terms and Conditions page has been implemented.
An 'About' page can be accessed which discusses the program's purpose,
instructions on how begin the exam, how to answer each test category
and how to interpret the results.

-------------------------------------------------------------------------

10. A formally written word-version report with the clearly numbered 
    screenshots related to the scenarios must be within your compressed 
    folder as mentioned at page 1.   

Estimated marks:  25 / 30

Reasoning: The report covers the entire functionality of the program, but
at the same time hides a bit of the software's internals. Overall, it is a
well-written, informative,

-------------------------------------------------------------------------

11. A readme.txt file explaining features completed, expected mark and
    locating presence of software from other sources.  

Estimated marks:  10 / 10

Reasoning:
The file contains all required aspects. Including features completed
(with their nuances), estimated marks, discussing usage of external
software, known bugs and more.
See: This file, 'readme.txt'

-------------------------------------------------------------------------

12. At least two new features (5 marks) in regards to voice assistance 
    or whatever.

Estimated marks:  0 / 5

Reasoning: We did not have time to consider additional features.

-------------------------------------------------------------------------

13. Demonstration and correct responses (5 marks) and absence in the
    demonstration will incur the negative marks calculated as 5% of 
    the marks obtained in each marking criteria from 1 to 12.  

Estimated marks:  4 / 5

Reasoning: Neither of us are good at holding full conversations about
software we've made, but we will try to explain everything to the best
of our abilities.

-------------------------------------------------------------------------

14. Screen-capture (mp4 video) presentation recorded with clear voice 
    and display of code that is testable to compare with what is recorded
    and presented.

Estimated marks: 5 / 10

Due to time constraints, the video is not as comprehensive or as short as we
would like, but we have covered most of the relevant information regarding
the assignment.

-------------------------------------------------------------------------

Late Penalty: No marks lost
% External Code acknowledged: No marks lost
% External code no acknowledged: No marks lost

Reasoning:
No external code was used in the making of this application. Therefore, we
cannot lose marks. All sources that have been used that do not involve 
code have also been referenced.

-------------------------------------------------------------------------

Total: 154 / 200

Modified Total: 154 / 200

Total(divide modified total by 10 and round up): 16 / 20

-------------------------------------------------------------------------


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Additional Discussion
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

VIDEO LINK: https://www.youtube.com/watch?v=xWB3EjmfMz4