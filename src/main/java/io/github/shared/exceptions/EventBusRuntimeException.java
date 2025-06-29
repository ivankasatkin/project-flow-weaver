package io.github.shared.exceptions;

public class EventBusRuntimeException extends FlowWeaverRuntimeException {

    public EventBusRuntimeException(String message) {
        super(message);
    }

    public EventBusRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public EventBusRuntimeException(Throwable cause) {
        super(cause);
    }
}
