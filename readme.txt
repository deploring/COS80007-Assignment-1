readme.txt

REMEMBER:
	- Mention Undergradute or Postgradute
	- All must be submitted in a single ZIP file
	- Video and Report must be in ZIP file
	- Your-ID-FirstNameUG/PG-18S2-AJ-Assign1.Zip naming convention


TO-DO:
	- In sources, add the source for terms and conditions code yeah thanks
	- Under assessment items, edit 'Content' - i just added all the requirements
	  even if we havent done them, so remove the ones we havent done
	- In Assessment Items > Content > Exam, specify amount of time spent on each test

Requirements:

	- Add Team Members information
	- Instructions on how to run the assignment
	- A list of known bugs
	- Identification of help we have recieved for the assignment
		Including - code from other sources
	-Itemisation of the assessment items completed
	- Esimated marks
	- Any discussion that is need to test the software

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


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		List of known bugs
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


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


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Itemization of assessment items
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
/----------/
/  Design: /
/----------/
- A UML class diagram depicting the structure and relationships of objects
  within the application
  See: file

- Flowcharts showing the logic behind:
	- Temporary PIN Generation
	- Activating The Test
	- Adaptively Prompting Questions
	- Recording Data
	- Producing Test Results
  See: files


- Use of MVC (Model/View/Controller)
	Explained within report
	See: Report

/-----------/
/  Content: /
/-----------/
- User must conform to Terms and Conditions before gaining access to the 
  application

- A student must enter his/her ID, name, school details to receive a PIN

PIN:

- A PIN is generated for the user, before they can enter the exam
- PIN is unique and randomly generated
- PIN has a time limit of 20 minutes

Exam:
- The exam has 5 categories 
	- Math
	- Recognising critical information from a given image
	- Spelling
	- Listening
	- Writing

- Exam has 15 minutes to be completed

- Each Test Category are considered completed after:
	- Math
	- Recognising critical information from a given image
	- Spelling
	- Listening
	- Writing

- When a test category has been selected, the remaining categories can
  be seen but cannot be clicked.
- A fast-track student can finish a test ahead of time by clicking 'Finish' button
- The 'Finish' button becomes visible after at least one question has been completed

- Questions have three difficulties: Hard, Harder, Hardest
- If the user gets a question correct, they will receive a question of harder
  diffculty
- If the user gets a question incorrect, they will receive a question of
  easier difficulty
- If the user is already at the Hard difficulty and get a question incorrect,
  they will receive another Hard question.
- If the user is already at the Hardest difficulty and get a question incorrect,
  they will receive another Hardest question.

- Questions are randomly selected from a question bank, based on the test category
  and question difficulty
- User will not receive the same question more than once

- Hard questions are worth 2 marks
- Harder questions are worth 5 marks
- Hardest questions are worth 10 marks





Math:
Recognising critical information from a given image:
Spelling:
Listening:
Writing:








/-----------------------------------------------/
/  Swing/JavaFX + other advanced Java features: /
/-----------------------------------------------/


/-------------------------/
/  Use of Graphics2D API: /
/-------------------------/

/-----------------------------------------/
/  Formally written word-version report:  /
/-----------------------------------------/


/-----------------/
/  New features:  /
/-----------------/

/----------/
/  Video:  /
/----------/
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Estimated marks
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

-------------------------------------------------------------------------

1. Provide UML diagrams, showing and justifying association, composition,
   and aggregation wherever appropriate. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

2. Provide flowcharts, showing the logic for temporary PIN generation, 
   activating the test, adaptively prompting the questions, recording the
   data, and producing the test result. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

3. Relate your work with Model Controller View (MVC). 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

4. Use Swing/JavaFx and advanced java features as far as possible to 
   implement each of the 5 categories. 

Estimated marks:   / 50

Reasoning:

-------------------------------------------------------------------------

5. Use event handling. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

6. Make sure usability, meaning there must be clear and logically      
   organized user interface and functionality. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

7. Use Graphics2D API as far as possible. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

8. Include reasonable documentation according to the Javadoc standards. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

9. Implement Terms and Condition entry screen before receiving the 
   temporary PIN, and  an “About” dialog activated from the menu. 

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

10. A formally written word-version report with the clearly numbered 
    screenshots related to the scenarios must be within your compressed 
    folder as mentioned at page 1.   

Estimated marks:   / 30

Reasoning:

-------------------------------------------------------------------------

11. A readme.txt file explaining features completed, expected mark and
    locating presence of software from other sources.  

Estimated marks:   / 10

Reasoning:

-------------------------------------------------------------------------

12. At least two new features (5 marks) in regards to voice assistance 
    or whatever.

Estimated marks:   / 5

Reasoning:

-------------------------------------------------------------------------

13. Demonstration and correct responses (5 marks) and absence in the
    demonstration will incur the negative marks calculated as 5% of 
    the marks obtained in each marking criteria from 1 to 12.  

Estimated marks:   / 5

Reasoning:

-------------------------------------------------------------------------

14. Screen-capture (mp4 video) presentation recorded with clear voice 
    and display of code that is testable to compare with what is recorded
    and presented.

Estimated marks: / 10

Reasoning:

-------------------------------------------------------------------------

Late Penalty: No marks lost
% External Code acknowledged: No marks lost
% External code no acknowldged: No marks lost

Reasoning:
No external code was used in the making of this application. Therefore, we
cannot lose marks. All sources that have been used that do not involve 
code have also been referenced.

-------------------------------------------------------------------------

Total: / 200

Modified Total: / 200

Total(divide modified total by 10 and round up): / 20

-------------------------------------------------------------------------


- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
		Additional Discussion
- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -





