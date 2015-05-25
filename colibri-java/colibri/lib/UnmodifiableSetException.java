package colibri.lib;

/**
 * Thrown to indicate that the set can not be modified.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class UnmodifiableSetException extends UnsupportedOperationException {
	
	private static final long serialVersionUID = -7977653209139916994L;
	
	
	/**
	 * Constructs an <code>UnmodifiableSetException</code> without detail message.
	 *
	 */
	public UnmodifiableSetException() {
		super();
	}
	
	
	/**
	 * Constructs an <code>UnmodifiableSetException</code> with the specified
	 * detail message.
	 * @param message the detail message.
	 */
	public UnmodifiableSetException(String message) {
		super(message);
	}
}
