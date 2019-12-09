package dawn.dlock.rlock.exception;

/**
 * 异常
 *
 * @author HEBO
 */
public class RlockException extends Exception {

	public RlockException() {
	}

	public RlockException(String message) {
		super(message);
	}

	public RlockException(Throwable cause) {
		super(cause);
	}

	public RlockException(String message, Throwable cause) {
		super(message, cause);
	}
}
