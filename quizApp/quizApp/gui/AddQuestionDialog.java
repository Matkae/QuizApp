package quizApp.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import quizApp.data.IllegalInputException;
import quizApp.data.Question;
import quizApp.data.QuestionContainer;

@SuppressWarnings("serial")
public class AddQuestionDialog extends JDialog implements ActionListener {

	private static AddQuestionDialog unique;
	
	private QuizGUI owner;
	private QuestionContainer container;
	private JButton btnAdd, btnClear, btnCancel;
	private JTextField[] txtFields;
	private Question oldQuestion;
	
	
	private AddQuestionDialog(QuizGUI owner, QuestionContainer container) {
		super(owner, "Add New Questions", false);
		this.owner = owner;
		this.container = container;
		
		JPanel gridPanel = new JPanel(new GridLayout(5, 1, 5, 5));
		add(gridPanel, BorderLayout.CENTER);
		
		JLabel[] labels = {new JLabel("   Question:"), new JLabel("   Correct Answer:"), new JLabel("   Wrong Answers:"), new JLabel(""), new JLabel("")};
		txtFields = new JTextField[labels.length];
		JPanel[] inputPanel = new JPanel[labels.length];
		for (int i = 0; i < txtFields.length; i++) {
			txtFields[i] = new JTextField(30);
			inputPanel[i] = new JPanel(new BorderLayout(15, 5));
			inputPanel[i].add(labels[i], BorderLayout.WEST);
			inputPanel[i].add(txtFields[i], BorderLayout.EAST);
			gridPanel.add(inputPanel[i]);
		}
		
		JPanel btnPanel = new JPanel();
		add(btnPanel, BorderLayout.SOUTH);
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(this);
		btnPanel.add(btnAdd);
		btnClear = new JButton("Clear");
		btnClear.addActionListener(this);
		btnPanel.add(btnClear);
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(this);
		btnPanel.add(btnCancel);
		JButton btnShowQ = new JButton("Show Questions");
		btnShowQ.addActionListener(this);
		btnPanel.add(btnShowQ);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setMinimumSize(this.getSize());
		setMaximumSize(this.getSize());
		setVisible(true);
	}
	
	public static AddQuestionDialog instance(QuizGUI owner, QuestionContainer container) {
		if (unique == null) {
			unique = new AddQuestionDialog(owner, container);
		}
		return unique;
	}
	
	public void modifyDialog(Question oldQuestion) {
		this.oldQuestion = oldQuestion;
		txtFields[0].setText(oldQuestion.getQuestion());
		txtFields[1].setText(oldQuestion.getCorrectAnswer());
		String[] temp = oldQuestion.getWrongAnswers();
		txtFields[2].setText(temp[0]);
		txtFields[3].setText(temp[1]);
		txtFields[4].setText(temp[2]);
		btnAdd.setText("Modify");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Add")) {
			try {
				container.linkQuestion(new Question(txtFields[0].getText(), txtFields[1].getText(), txtFields[2].getText(), txtFields[3].getText(), txtFields[4].getText()));
				clearTxtFields();
			} catch (IllegalInputException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		}
		if (e.getActionCommand().equals("Modify")) {
			try {
				container.replaceQuestion(oldQuestion, new Question(txtFields[0].getText(), txtFields[1].getText(), txtFields[2].getText(), txtFields[3].getText(), txtFields[4].getText()));
				clearTxtFields();
				oldQuestion = null;
				btnAdd.setText("Add");
				dispose();
			} catch (IllegalInputException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		}
		if (e.getSource() == btnClear) {
			clearTxtFields();
		}
		if (e.getSource() == btnCancel) {
			dispose();
		}
		if (e.getActionCommand().equalsIgnoreCase("Show Questions")) {
			QuestionList.instance(owner, container).setVisible(true);
		}
	}
	
	private void clearTxtFields() {
		for (JTextField t : txtFields) {
			t.setText("");
		}
	}
}
