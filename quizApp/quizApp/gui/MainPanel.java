package quizApp.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import quizApp.data.QuestionContainer;

@SuppressWarnings("serial")
public class MainPanel extends JPanel {
	
	private JTextPane txtPane;
	private JButton btnOne, btnTwo;
	
	public MainPanel(QuizGUI owner) {
		super(new BorderLayout());		
		
		txtPane = new JTextPane();
		txtPane.setContentType("text/html");
		txtPane.setEditable(false);
		add(txtPane, BorderLayout.CENTER);		
		
		JPanel pnlButtons = new JPanel(new GridLayout(1, 0, 5 ,5));
		pnlButtons.setPreferredSize(new Dimension(0, 70));
		add(pnlButtons, BorderLayout.SOUTH);
		btnOne = new JButton();
		btnOne.addActionListener(owner);
		pnlButtons.add(btnOne);
		btnTwo = new JButton();
		btnTwo.addActionListener(owner);
		pnlButtons.add(btnTwo);
		showWelcomeScreen();
		setVisible(true);
	}
	
	public void showWelcomeScreen() {
		txtPane.setText("<html><h1 style=font-size:30px><center><br>Welcome to QuizApp!</center></h1></html>");
		if (QuestionContainer.instance().getQuestions().size() > 0) {
			btnOne.setText("Start Quiz");
			btnTwo.setText("Show Questions");
		} else {
			btnOne.setText("Add New Questions");
			btnTwo.setText("Load New Quiz Data");	
		}
	}
	
	public void endQuiz(int correctAnswerCount, int answeredQuestionCount) {
		txtPane.setText("<html><h1 style=font-size:30px><center><br>" + ((correctAnswerCount / (double) answeredQuestionCount >= 0.5) ? "Congratulations!" : "Better Luck Next Time") + "</center></h1><body style=font-size:20px><center><br>You answered " + correctAnswerCount + " out of " + answeredQuestionCount + " questions correctly.</center></body></html>");
		btnOne.setText("Restart Quiz");
		btnTwo.setText("Load New Quiz Data");
	}
	
	public void showNoQuestionsDialog() {
		txtPane.setText("<html><body style=font-size:20px><br><br><p>The quiz could not be started because the data set contains no questions. Please add or load new questions before starting a new quiz.</p></body><html>");
		btnOne.setText("Add New Questions");
		btnTwo.setText("Load New Quiz Data");
	}
}
