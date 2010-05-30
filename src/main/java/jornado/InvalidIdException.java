package jornado;

/**
 * The given Id was invalid and the login cookie should be cleared
 */
public class InvalidIdException extends RuntimeException {
    public InvalidIdException() {
    }

    public InvalidIdException(String message) {
        super(message);
    }

    public InvalidIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidIdException(Throwable cause) {
        super(cause);
    }
}
