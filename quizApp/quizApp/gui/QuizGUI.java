package quizApp.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import quizApp.data.QuestionContainer;
import quizApp.store.LoadSaveException;

@SuppressWarnings("serial")
public class QuizGUI extends JFrame implements ActionListener {

	private QuestionContainer container;
	private String folderLocation;
	private String filename;
	
	private QuizPanel pnlQuiz;
	private MainPanel pnlMain;
	
	private boolean welcome = true;
	private boolean quizActive;
	
	public QuizGUI() {
		super("QuizApp");
		container = QuestionContainer.instance();
		
		setLayout(new BorderLayout(0, 5));
		pnlQuiz = new QuizPanel(this, container);
		pnlMain = new MainPanel(this);
		add(pnlMain, BorderLayout.CENTER);
		
		
		JMenuBar mBar = new JMenuBar();
		add(mBar, BorderLayout.NORTH);
		JMenu menuQuiz = new JMenu("Quiz");
		mBar.add(menuQuiz);
		JMenuItem mItemStartQuiz = new JMenuItem("Start Quiz");
		mItemStartQuiz.addActionListener(this);
		menuQuiz.add(mItemStartQuiz);
		JMenuItem mItemEndQuiz = new JMenuItem("End Quiz");
		mItemEndQuiz.addActionListener(this);
		menuQuiz.add(mItemEndQuiz);
		JMenuItem mItemExit = new JMenuItem("Exit");
		mItemExit.addActionListener(this);
		menuQuiz.add(mItemExit);
		
		JMenu menuQuestions = new JMenu("Questions");
		mBar.add(menuQuestions);
		JMenuItem mItemAddQ = new JMenuItem("Add New Questions");
		mItemAddQ.addActionListener(this);
		menuQuestions.add(mItemAddQ);
		JMenuItem mItemShowQ = new JMenuItem("Show Questions");
		mItemShowQ.addActionListener(this);
		menuQuestions.add(mItemShowQ);
		JMenuItem mItemSave = new JMenuItem("Save");
		mItemSave.addActionListener(this);
		menuQuestions.add(mItemSave);
		JMenuItem mItemSaveAs = new JMenuItem("Save As");
		mItemSaveAs.addActionListener(this);
		menuQuestions.add(mItemSaveAs);
		JMenuItem mItemLoad = new JMenuItem("Load");
		mItemLoad.addActionListener(this);
		menuQuestions.add(mItemLoad);		
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(650, 500);
		
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		setLocation(gd.getDisplayMode().getWidth() / 2 - getWidth() / 2, gd.getDisplayMode().getHeight() / 2 - getHeight() / 2);
		setVisible(true);
		
		try {
			folderLocation = new File("").getAbsolutePath().concat("\\Questions\\");
			container.load(folderLocation.concat("Questions.txt"));
		} catch (LoadSaveException e) {
			JOptionPane.showMessageDialog(this, "Could not load Questions.txt.");
		}
		pnlMain.showWelcomeScreen();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equalsIgnoreCase("Start Quiz") || e.getActionCommand().equalsIgnoreCase("Restart Quiz")) {
			if (!quizActive) {
				if (container != null && container.getQuestions().size() > 0) {
					welcome = false;
					startQuiz();
				} else {
					showNoQuestionsDialog();
				}
			}
		}
		if (e.getActionCommand().equalsIgnoreCase("End Quiz")) {
			if (quizActive) {
				endQuiz();
			}
		}
		if (e.getActionCommand().equalsIgnoreCase("Exit")) {
			dispose();
		}
		if (e.getActionCommand().equalsIgnoreCase("Add New Questions")) {
			AddQuestionDialog.instance(this, container).setVisible(true);
		}
		if (e.getActionCommand().equalsIgnoreCase("Show Questions")) {
			QuestionList.instance(this, container).setVisible(true);
		}
		if (e.getActionCommand().equalsIgnoreCase("Save")) {
			onSave();
		}
		if (e.getActionCommand().equalsIgnoreCase("Save As")) {
			onSaveAs();
		}
		if (e.getActionCommand().equalsIgnoreCase("Load") || e.getActionCommand().equalsIgnoreCase("Load New Quiz Data")) {
			onLoad();
		}
	}
	
	private void startQuiz() {
		remove(pnlMain);
		add(pnlQuiz, BorderLayout.CENTER);
		pnlQuiz.startQuiz();		
		repaint();
		quizActive = true;
	}
	
	private void showNoQuestionsDialog() {
		add(pnlMain, BorderLayout.CENTER);
		pnlMain.showNoQuestionsDialog();
		repaint();
		quizActive = false;
	}
	
	private void endQuiz() {
		remove(pnlQuiz);			
		add(pnlMain, BorderLayout.CENTER);
		pnlMain.endQuiz(pnlQuiz.getCorrectAnswersCount(), pnlQuiz.getAnsweredQuestionsCount());
		repaint();
		quizActive = false;
	}
	
	private void onSave() {
		if (container == null) {
			container = QuestionContainer.instance();			
		}
		if (container.getQuestions().size() == 0) {
			JOptionPane.showMessageDialog(this, "No questions to save.");
		}
		if (filename == null) {
			JFileChooser chooser = new JFileChooser(folderLocation);
			if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				filename = chooser.getSelectedFile().getPath();
			} else {
				JOptionPane.showMessageDialog(this, "No file selected.");
			}
		}
		if (filename != null) {
			try {
				container.save(filename);
			} catch (LoadSaveException ex) {
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		}
	}
	
	private void onSaveAs() {
		filename = null;
		onSave();
	}
	
	private void onLoad() {
		if (container == null) {
			container = QuestionContainer.instance();			
		}
		JFileChooser chooser = new JFileChooser(folderLocation);
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				container.load(chooser.getSelectedFile().getPath());
				if (welcome) { 
					pnlMain.showWelcomeScreen();
				}
			} catch (LoadSaveException ex) {	
				JOptionPane.showMessageDialog(this, ex.getMessage());
			}
		} else {
			JOptionPane.showMessageDialog(this, "No file selected.");
		}
	}
	
}
