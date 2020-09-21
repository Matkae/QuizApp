package quizApp.data;

/**
 * The class represents a question with four possible answers of which always exactly one answers the question correctly.
 * 
 * @author Matkae
 */
public class Question {

	private String question, cAnswer;
	private String[] wAnswers;
	
	/**
     * Creates a new Question with the specified question text, correct answer and wrong answers one to three
     * 
     * @param question the question text of this Question
     * @param cAnswer the correct answer of this Question
     * @param wAnswer1 the first wrong answer of this Question
     * @param wAnswer2 the second wrong answer of this Question
     * @param wAnswer3 the third wrong answer of this Question
     * 
     * @throws IllegalInputException if question or answers are not valid
     */
	public Question(String question, String cAnswer, String wAnswer1, String wAnswer2, String wAnswer3) throws IllegalInputException {
		wAnswers = new String[3];
		setQuestion(question);
		setCorrectAnswer(cAnswer);
		setWrongAnswers(wAnswer1, wAnswer2, wAnswer3);
	}
	
	/**
	 * Sets the question text to the provided String
	 * 
	 * @param question the question text
	 * @throws IllegalInputException if question is null or not between 4 and 130 characters
	 */
	public void setQuestion(String question) throws IllegalInputException {
		if (!checkQuestion(question)) {
			throw new IllegalInputException("Invalid input: " + question + ".\nQuestion has to be between 4 and 130 characters long.");
		}
		this.question = question;
	}
	
	/**
	 * Sets the correct answer text to the provided String
	 * 
	 * @param cAnswer the correct answer text
	 * @throws IllegalInputException if cAnswer is null or not between 1 and 30 characters
	 */
	public void setCorrectAnswer(String cAnswer) throws IllegalInputException {
		if (!checkAnswer(cAnswer)) {
			throw new IllegalInputException("Invalid input: " + cAnswer + ".\nAnswer has to be between 1 and 30 characters long.");
		}
		this.cAnswer = cAnswer;
	}
	
	/**
	 * Sets the wrong answer texts to the provided String objects
	 * 
	 * @param wAnswer1 the first wrong answer text
	 * @param wAnswer2 the second wrong answer text
	 * @param wAnswer3 the third wrong answer text
	 * @throws IllegalInputException if one or more of the input String objects is null or not between 1 and 30 characters
	 */
	public void setWrongAnswers(String wAnswer1, String wAnswer2, String wAnswer3) throws IllegalInputException {
		if (!checkAnswer(wAnswer1)) {
			throw new IllegalInputException("Invalid input: " + wAnswer1 + ".\nAnswer has to be between 1 and 30 characters long.");
		}
		if (!checkAnswer(wAnswer2)) {
			throw new IllegalInputException("Invalid input: " + wAnswer2 + ".\nAnswer has to be between 1 and 30 characters long.");
		}
		if (!checkAnswer(wAnswer3)) {
			throw new IllegalInputException("Invalid input: " + wAnswer3 + ".\nAnswer has to be between 1 and 30 characters long.");
		}
		wAnswers[0] = wAnswer1;
		wAnswers[1] = wAnswer2;
		wAnswers[2] = wAnswer3;
	}
	
    /**
     * Returns the correct answer of this Question
     * 
     * @return cAnswer the correct answer to this Question
     */
	public String getCorrectAnswer() {
		return cAnswer;
	}
	
    /**
     * Returns an Array with the wrong answers to this Question
     * 
     * @return a clone of the Array containing the wrong answers to this Question
     */
	public String[] getWrongAnswers() {
		return wAnswers.clone();
	}
	
    /**
     * Returns the question text of this Question
     * 
     * @return question the question text to this Question
     */
	public String getQuestion() {
		return question;
	}
	
	private static boolean checkAnswer(String answer) {
		return answer != null && answer.length() <= 30 && !answer.isBlank();
	}
	
	private static boolean checkQuestion(String question) {
		return question != null && question.length() > 3 && question.length() <= 130 && !question.isBlank();
	}
	
    /**
     * Returns a String object representing the question text, correct answer and wrong answers
     * 
     * @return String representation of question text, correct answer and wrong answers of this Question
     */
	@Override
	public String toString() {
		return "Question: " + question + " | Correct Answer: " + cAnswer + " | Wrong Answers: " + wAnswers[0] + ", " + wAnswers[1] + ", " + wAnswers[2];
	}
	
    /**
     * Checks if this Question object is the same as the provided Object o
     * 
     * @return true if this Question object and Object o have the same question text, correct answer and wrong answers
     */
	@Override
	public boolean equals(Object o) {
		if (o != null && o.getClass().equals(Question.class)) {
			return toString().equals(((Question) o).toString());
		}
		return false;
	}
}
