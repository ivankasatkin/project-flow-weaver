package io.github.shared.exceptions;

public class DisposableSwitchRuntimeException extends FlowWeaverRuntimeException {

    public DisposableSwitchRuntimeException(String message) {
        super(message);
    }

    public DisposableSwitchRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisposableSwitchRuntimeException(Throwable cause) {
        super(cause);
    }
}
