package io.github.shared.exceptions;

public class FlowWeaverRuntimeException extends RuntimeException {

    public FlowWeaverRuntimeException(String message) {
        super(message);
    }

    public FlowWeaverRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FlowWeaverRuntimeException(Throwable cause) {
        super(cause);
    }
}
