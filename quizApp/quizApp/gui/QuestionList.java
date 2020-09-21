package quizApp.gui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import quizApp.data.Question;
import quizApp.data.QuestionContainer;

@SuppressWarnings("serial")
public class QuestionList extends JDialog implements ActionListener, PropertyChangeListener {

	private static QuestionList unique;
	private QuizGUI owner;
	private JList<Question> list;
	private QuestionContainer container;
	
	private QuestionList(QuizGUI owner, QuestionContainer container) {
		super(owner, "Question List", false);
		this.owner = owner;
		this.container = container;
		container.addPropertyChangeListener(this);
		list = new JList<>(new Vector<Question>(container.getQuestions()));
		list.setFont(new Font("Arial", Font.PLAIN, 14));
		add(list, BorderLayout.CENTER);
		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);
		JButton btnAdd = new JButton("Add New Questions");
		btnAdd.addActionListener(this);
		buttonPanel.add(btnAdd);
		JButton btnModify = new JButton("Modify Selected Question");
		btnModify.addActionListener(this);
		buttonPanel.add(btnModify);
		JButton btnDelete = new JButton("Delete Selected Questions");
		btnDelete.addActionListener(this);
		buttonPanel.add(btnDelete);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(1000, 500);
		setVisible(true);
	}
	
	public static QuestionList instance(QuizGUI owner, QuestionContainer container) {
		if (unique == null) {
			unique = new QuestionList(owner, container);
		}
		return unique;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("Add New Questions")) {
			AddQuestionDialog.instance(owner, container).setVisible(true);
		}
		if (e.getActionCommand().equalsIgnoreCase("Modify Selected Question")) {
			AddQuestionDialog tempDialog = AddQuestionDialog.instance(owner, container);
			tempDialog.setVisible(true);
			tempDialog.modifyDialog(list.getSelectedValue());
		}
		if (e.getActionCommand().equalsIgnoreCase("Delete Selected Questions")) {
			if (list.isSelectionEmpty()) {
				JOptionPane.showMessageDialog(this, "No question selected.");
			} else {
				int i = 0;
				int j = list.getSelectedValuesList().size();
				for (Question q : list.getSelectedValuesList()) {
					if (container.unlinkQuestion(q)) {
						i++;
					}
				}
				JOptionPane.showMessageDialog(this, "" + i + " out of " + j + " selected questions were successfully deleted.");
			}
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if (e.getPropertyName().equals("link") || e.getPropertyName().equals("replace") || e.getPropertyName().equals("unlink")) {
			list.setListData(new Vector<Question>(container.getQuestions()));
		}
	}
}
