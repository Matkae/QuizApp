package quizApp.data;
/**
 * Indicates that a user input for one ore more attributes is invalid.
 * 
 * @author Matkae
 */
@SuppressWarnings("serial")
public class IllegalInputException extends Exception {

	/**
    * Constructs an IllegalInputException with the specified message
    * 
    * @param message the message specifying why the Exception was thrown
    * 
	*/
	public IllegalInputException(String message) {
		super(message);
	}
	
}
