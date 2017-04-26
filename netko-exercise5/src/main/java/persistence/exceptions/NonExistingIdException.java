package persistence.exceptions;

public class NonExistingIdException extends Exception {

	private static final String DEFAULT_MESSAGE = "Cannot update object with ID, because ID does not exist.";

	public NonExistingIdException() {
		super(DEFAULT_MESSAGE);
	}

	private static final long serialVersionUID = 8211584403559123899L;

}
