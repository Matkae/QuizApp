package quizApp.store;
/**
 * Indicates that an error occurred during the loading or saving process
 * 
 * @author Matkae
 */
@SuppressWarnings("serial")
public class LoadSaveException extends Exception{

	/**
	 * Constructs an LoadSaveException with the specified message
	 * 
	 * @param message the message specifying why the Exception was thrown
	 * @param ex the Throwable that caused this LoadSaveException
	 */
	public LoadSaveException(String message, Throwable ex) {
		super(message, ex);
	}
	
}
