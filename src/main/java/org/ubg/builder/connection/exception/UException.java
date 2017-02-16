package org.ubg.builder.connection.exception;

/**
 * Created by dincaus on 2/3/17.
 */
public class UException extends Exception {

    public UException() {
        super();
    }

    public UException(String message) {
        super(message);
    }

    public UException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String toString() {
        return this.getMessage();
    }
}
