package io.github.shared.exceptions;

public class EventBusIllegalArgumentException extends EventBusRuntimeException {

    public EventBusIllegalArgumentException(String message) {
        super(message);
    }

    public EventBusIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventBusIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
