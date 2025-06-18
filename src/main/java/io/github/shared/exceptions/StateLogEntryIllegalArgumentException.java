package io.github.shared.exceptions;

public class StateLogEntryIllegalArgumentException extends StateLogRuntimeException {

    public StateLogEntryIllegalArgumentException(String message) {
        super(message);
    }

    public StateLogEntryIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public StateLogEntryIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
