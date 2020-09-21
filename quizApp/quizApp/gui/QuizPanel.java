package quizApp.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import quizApp.data.Question;
import quizApp.data.QuestionContainer;

@SuppressWarnings("serial")
public class QuizPanel extends JPanel implements ActionListener {

	private QuizGUI owner;
	private JTextPane txtPane;
	private JButton[] btnsAnswer;
	private QuestionContainer container;
	private ArrayList<Question> questions;
	private Question question;
	private JButton btnCAnswer;
	private JButton btnEndQ;
	private JButton btnNextQ;
	private JTextField txtStatus;
	private int correctAnswersCount = 0;
	private int answeredQuestionsCount = 0;
	
	public QuizPanel(QuizGUI owner, QuestionContainer container) {
		super(new BorderLayout(15, 15));
		this.owner = owner;
		if (container == null) {
			container = QuestionContainer.instance();
		}
		this.container = container;
		txtPane = new JTextPane();
		txtPane.setContentType("text/html");
		txtPane.setPreferredSize(new Dimension(0, 85));
		txtPane.setEditable(false);
		add(txtPane, BorderLayout.NORTH);
		
		JPanel pnlButtons = new JPanel(new GridLayout(2, 2, 15, 15));
		add(pnlButtons, BorderLayout.CENTER);
		btnsAnswer = new JButton[4];
		for (int i = 0; i <btnsAnswer.length; i++) {
			btnsAnswer[i] = new JButton();
			btnsAnswer[i].setFont(new Font(btnsAnswer[i].getFont().getFontName(), Font.PLAIN, 18));
			btnsAnswer[i].addActionListener(this);
			pnlButtons.add(btnsAnswer[i]);
		}
		
		JPanel pnlBottom = new JPanel(new BorderLayout(5, 5));
		add(pnlBottom, BorderLayout.SOUTH);
		JPanel pnlEndNextQ = new JPanel(new GridLayout(1, 0, 5, 0));
		pnlEndNextQ.setPreferredSize(new Dimension(300, 30));
		pnlBottom.add(pnlEndNextQ, BorderLayout.CENTER);
		btnEndQ = new JButton("End Quiz");
		btnEndQ.setFont(new Font(btnEndQ.getFont().getFontName(), Font.PLAIN, 14));
		btnEndQ.addActionListener(this);
		pnlEndNextQ.add(btnEndQ);
		btnNextQ = new JButton("Next Question");
		btnNextQ.setFont(new Font(btnNextQ.getFont().getFontName(), Font.PLAIN, 14));
		btnNextQ.addActionListener(this);
		btnNextQ.setEnabled(false);
		pnlEndNextQ.add(btnNextQ);
		txtStatus = new JTextField(30);
		txtStatus.setPreferredSize(new Dimension(0, 27));
		txtStatus.setFont(new Font("Arial", Font.PLAIN, 12));
		txtStatus.setEditable(false);
		
		pnlBottom.add(txtStatus, BorderLayout.SOUTH);
		setPreferredSize(new Dimension(500, 450));
		startQuiz();
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnNextQ) {
			showNextQuestion();
		} else if (e.getActionCommand().equalsIgnoreCase("End Quiz")) {
			owner.actionPerformed(e);
		} else if (e.getActionCommand().equals(question.getCorrectAnswer())) {
			onCorrectAnswer();
		} else {
			onWrongAnswer((JButton) e.getSource());
		}
	}
	
	public int getCorrectAnswersCount() {
		return correctAnswersCount;
	}
	
	public int getAnsweredQuestionsCount() {
		return answeredQuestionsCount;
	}
	
	public void startQuiz() {
		questions = (ArrayList<Question>) container.getQuestions();
		correctAnswersCount = 0;
		answeredQuestionsCount = 0;
		for (JButton btn : btnsAnswer) {
			btn.setEnabled(true);
			btn.setBackground(null);
		}
		txtStatus.setText("");
		showNextQuestion();
	}
	
	private void showNextQuestion() {
		if (questions.size() > 0) {
			Random rng = new Random();
			int index = rng.nextInt(questions.size());
			question = questions.get(index);
			txtPane.setText("<html><h3 style=font-size:18px>" + question.getQuestion() + "</h3></html>");
			ArrayList<String> answers = new ArrayList<>();
			answers.add(question.getCorrectAnswer());
			for (String wAnswer : (String[]) question.getWrongAnswers()) {
				answers.add(wAnswer);
			}
			for (JButton btn : btnsAnswer) {
				int i = rng.nextInt(answers.size());
				String answer = answers.get(i);
				if (answer.equals(question.getCorrectAnswer())) {
					btnCAnswer = btn;
				}
				btn.setText(answer);
				btn.setEnabled(true);
				btn.setBackground(null);
				answers.remove(i);
			}
			questions.remove(index);
			btnNextQ.setEnabled(false);
		} else {
			btnEndQ.doClick();
		}
	}

	private void onCorrectAnswer() {
		answeredQuestionsCount++;
		correctAnswersCount++;
		txtStatus.setText("Correctly answered questions: " + correctAnswersCount + "/" + answeredQuestionsCount);
		btnCAnswer.setBackground(Color.GREEN);
		for (JButton btn : btnsAnswer) {
			btn.setEnabled(false);
		}
		btnNextQ.setEnabled(true);
	}
	
	private void onWrongAnswer(JButton clickedButton) {
		answeredQuestionsCount++;
		txtStatus.setText("Correctly answered questions: " + correctAnswersCount + "/" + answeredQuestionsCount);
		clickedButton.setBackground(Color.RED);
		btnCAnswer.setBackground(Color.GREEN);
		for (JButton btn : btnsAnswer) {
			btn.setEnabled(false);
		}
		btnNextQ.setEnabled(true);
	}
}
