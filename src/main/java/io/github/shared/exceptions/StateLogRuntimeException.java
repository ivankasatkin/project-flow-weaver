package io.github.shared.exceptions;

public class StateLogRuntimeException extends RuntimeException {

    public StateLogRuntimeException(String message) {
        super(message);
    }

    public StateLogRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateLogRuntimeException(Throwable cause) {
        super(cause);
    }
}
