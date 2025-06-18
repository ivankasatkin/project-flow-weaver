package io.github.shared.exceptions;

public class StateLogIllegalArgumentException extends StateLogRuntimeException {

    public StateLogIllegalArgumentException(String message) {
        super(message);
    }

    public StateLogIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateLogIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
