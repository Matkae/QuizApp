package quizApp.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;

import quizApp.store.DataManagement;
import quizApp.store.LoadSaveException;
import quizApp.store.PersistenceText;

/**
 * The Container class for Question objects 
 * 
 * @author Matkae
 * @see Question
 */
public class QuestionContainer {

	private static QuestionContainer unique;
	private PropertyChangeSupport changes;
	private Collection<Question> questions;
	private DataManagement store;
	
	private QuestionContainer() {
		changes = new PropertyChangeSupport(this);
		questions = new ArrayList<>();
	}
	
	/**
	 * Returns the only existing object of the QuestionContainer class.
	 * 
	 * @return the only existing object of the QuestionContainer class
	 */
	public static QuestionContainer instance() {
		if (unique == null) {
			unique = new QuestionContainer();
		}
		return unique;
	}
	
	/**
	 * links the question to this QuestionContainer if there is not already a Question object with the same question text linked to this container.
	 * Fires a PropertyChangeEvent from the PropertyChangeSupport if the question is successfully linked.
	 * 
	 * @param question the Question object to be linked to this QuestionContainer
	 * @return true if the question was successfully linked to this QuestionContainer
	 * @see Question
	 * @see PropertyChangeEvent
	 * @see PropertyChangeSupport
	 */
	public boolean linkQuestion(Question question) {
		if (question == null) {
			return false;
		}
		for (Question q : questions) {
			if (question.getQuestion().equals(q.getQuestion())) {
				return false;
			}
		}
		if (questions.add(question)) {
			changes.firePropertyChange("link", null, question);
			return true;
		}
		return false;
	}
	
	/**
	 * Unlinks the question from this QuestionContainer if it was previously linked to the container.
	 * Fires a PropertyChangeEvent from the PropertyChangeSupport if the question is successfully unlinked.
	 * 
	 * @param question the Question object to be unlinked from this QuestionContainer
	 * @return true if the question was successfully unlinked from this QuestionContainer
	 * @see Question
	 * @see PropertyChangeEvent
	 * @see PropertyChangeSupport
	 */
	public boolean unlinkQuestion(Question question) {
		if (questions.remove(question)) {
			changes.firePropertyChange("unlink", question, null);
			return true;
		}
		return false;
	}
	
	/**
	 * Replaces the oldQuestion with the newQuestion in this QuestionContainer.
	 * Fires a PropertyChangeEvent from the PropertyChangeSupport if the question was successfully replaced and the newQuestion is not equal to the oldQuestion.
	 * 
	 * @param oldQuestion the Question object that is replaced by newQuestion in this QuestionContainer
	 * @param newQuestion the Question object that replaces oldQuestion in this QuestionContainer
	 * @return true if the Question object was successfully replaced
	 * @throws IllegalInputException if oldQuestion or newQuestion is null or if oldQuestion is not an element of this QuestionContainer
	 * @see Question
	 * @see PropertyChangeEvent
	 * @see PropertyChangeSupport
	 */
	public boolean replaceQuestion(Question oldQuestion, Question newQuestion) throws IllegalInputException {
		if (oldQuestion == null || !questions.contains(oldQuestion)) {
			throw new IllegalInputException("oldQuestion must not be null and be an element of the question container. value: " + oldQuestion);
		}
		if (newQuestion == null) {
			throw new IllegalInputException("newQuestion must not be null. value: " + newQuestion);
		}
		
		if (questions.remove(oldQuestion)) {
			if (questions.add(newQuestion)) {
				changes.firePropertyChange("replace", oldQuestion, newQuestion);
				return true;
			} else {
				questions.add(oldQuestion);
			}
		}
		return false;
	}
	
    /**
     * Returns a copy of all the Question objects linked to this QuestionContainer.
     * 
     * @return a Collection containing a copy of all the Question objects linked to this QuestionContainer
     */
	public Collection<Question> getQuestions() {
		Collection<Question> copy = new ArrayList<>(questions);
		return copy;
	}
	
	/**
	 * Adds the provided PropertyChangeListener to the PropertyChangeSupport of this QuestionContainer.
	 * 
	 * @param listener the PropertyChangeListener to be added to the PropertyChangeSupport
	 * @see PropertyChangeListener
	 * @see PropertyChangeSupport
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changes.addPropertyChangeListener(listener);
	}
	
	/**
	 * Removes the provided PropertyChangeListener from the PropertyChangeSupport of this QuestionContainer.
	 * 
	 * @param listener the PropertyChangeListener to be removed from the PropertyChangeSupport
	 * @see PropertyChangeListener
	 * @see PropertyChangeSupport
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changes.removePropertyChangeListener(listener);
	}
	
	/**
	 * Saves the content of this QuestionContainer in a file on the hard drive.
	 * 
	 * @param filename the absolute path of the file on the hard drive
	 * @throws LoadSaveException if an error occurs during the saving process
	 */
	public void save(String filename) throws LoadSaveException {
		store = new PersistenceText(filename);
		store.save(this);
	}
	
	/**
	 * Loads the content of this QuestionContainer from a file on the hard drive.
	 * 
	 * @param filename the absolute path of the file on the hard drive
	 * @throws LoadSaveException if an error occurs during the loading process
	 */
	public void load(String filename) throws LoadSaveException {
		Collection<Question> tempQuestions = new ArrayList<>(questions);
		store = new PersistenceText(filename);
		try {
			questions.clear();
			store.load(this);
		} catch (LoadSaveException ex) {
			questions = tempQuestions;
			throw new LoadSaveException(ex.getMessage(), ex.getCause());
		}
	}
	
	/**
	 * Removes all Question objects from this QuestionContainer and fires a PropertyChangeEvent from the PropertyChangeSupport.
	 * 
	 * @see PropertyChangeEvent
	 * @see PropertyChangeSupport
	 */
	public void clear() {
		questions.clear();
		changes.firePropertyChange("clear", questions, null);
	}
}
