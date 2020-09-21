package quizApp.store;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import quizApp.data.IllegalInputException;
import quizApp.data.Question;
import quizApp.data.QuestionContainer;

/**
 * The class to save/load Question objects to/from a persistent data store in the form of a text file.
 * 
 * @author Matkae
 *
 */
public class PersistenceText implements DataManagement {

	private String filename;
	
	/**
	 * Creates a new PersistenceText with a specified file path. 
	 * 
	 * @param filename the absolute file path
	 */
	public PersistenceText(String filename) {
		this.filename = filename;
	}
	
	/**
	 * Empty implementation
	 */
	@Override
	public void add(Question question) throws LoadSaveException {
		// empty	
	}

	/**
	 * Empty implementation
	 */
	@Override
	public void modify(Question oldQuestion, Question newQuestion) throws LoadSaveException {
		// empty	
	}

	/**
	 * Empty implementation
	 */
	@Override
	public void remove(Question question) throws LoadSaveException {
		// empty	
	}

	/**
	 * Saves the Question objects linked to the provided QuestionContainer in a file. The path of the file is defined in the constructor of this PersistenceText object.<br>
	 * The Question objects will be saved in the following format:<br>
	 * <br>
	 * new question<br>
	 * question text of the first Question<br>
	 * correct answer of the first Question<br>
	 * first wrong answer of the first Question<br>
	 * second wrong answer of the first Question<br>
	 * third wrong answer of the first Question<br>
	 * new question<br>
	 * .<br>
	 * .<br>
	 * .<br>
	 * third wrong answer of the last Question<br>
	 * end<br>
	 * 
	 * @param container the QuestionContainer object who's linked Questions are to be saved
	 * @throws LoadSaveException if a FileNotFoundException is thrown by the PrintWriter
	 */
	@Override
	public void save(QuestionContainer container) throws LoadSaveException {
		try (PrintWriter writer = new PrintWriter(filename)) {
			for (Question q : container.getQuestions()) {
				writer.println("new question");
				writer.println(q.getQuestion());
				writer.println(q.getCorrectAnswer());
				for (String s : q.getWrongAnswers()) {
					writer.println(s);
				}
			}
			writer.println("end");
		} catch (FileNotFoundException ex) {
			throw new LoadSaveException(ex.getMessage(), ex);
		}	
	}

	/**
	 * Loads the Question objects saved in the file and links them to the provided QuestionContainer. The path of the file is defined in the constructor of this PersistenceText object.
	 * 
	 * @param container the QuestionContainer object that the loaded Questions are linked to
	 * @throws LoadSaveException if the file has an invalid format
	 * @throws LoadSaveException if an IllegalInputException is thrown by a loaded Question
	 * @throws LoadSaveException if an IOException is thrown by the BufferedReader
	 */
	@Override
	public void load(QuestionContainer container) throws LoadSaveException {
		try (BufferedReader reader = Files.newBufferedReader(Paths.get(filename))) {
			String line = reader.readLine();
			if (line == null) {
				throw new LoadSaveException("the selected file has an invalid format.", null);
			}
			while (!line.equals("end")) {
				if (line.equals("new question")) {
					line = reader.readLine();
					if (line == null) {
						throw new LoadSaveException("the selected file has an invalid format.", null);
					}
					String question = line;
					line = reader.readLine();
					if (line == null) {
						throw new LoadSaveException("the selected file has an invalid format.", null);
					}
					String cAnswer = line;
					line = reader.readLine();
					if (line == null) {
						throw new LoadSaveException("the selected file has an invalid format.", null);
					}
					String wAnswer1 = line;
					line = reader.readLine();
					if (line == null) {
						throw new LoadSaveException("the selected file has an invalid format.", null);
					}
					String wAnswer2 = line;
					line = reader.readLine();
					if (line == null) {
						throw new LoadSaveException("the selected file has an invalid format.", null);
					}
					String wAnswer3 = line;
					try {
						container.linkQuestion(new Question(question, cAnswer, wAnswer1, wAnswer2, wAnswer3));
					} catch (IllegalInputException ex) {
						throw new LoadSaveException(ex.getMessage(), ex);
					}
					line = reader.readLine();
					if (line == null) {
						throw new LoadSaveException("the selected file has an invalid format.", null);
					}
				} else {
					throw new LoadSaveException("the selected file has an invalid format.", null);
				}
			}
		} catch (IOException ex) {
			throw new LoadSaveException("Loading failed.", ex);
		}		
	}
	
}
