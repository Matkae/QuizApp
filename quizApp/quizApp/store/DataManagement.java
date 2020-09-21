package quizApp.store;

import quizApp.data.Question;
import quizApp.data.QuestionContainer;

public interface DataManagement {

	void add(Question question) throws LoadSaveException;
	
	void modify(Question oldQuestion, Question newQuestion) throws LoadSaveException;
	
	void remove(Question question) throws LoadSaveException;
	
	void save(QuestionContainer container) throws LoadSaveException;
	
	void load(QuestionContainer container) throws LoadSaveException;
	
}
