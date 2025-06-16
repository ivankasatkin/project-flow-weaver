package io.github.shared.exceptions;

public class DisposableSwitchIllegalArgumentException extends FlowWeaverRuntimeException {

    public DisposableSwitchIllegalArgumentException(String message) {
        super(message);
    }

    public DisposableSwitchIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisposableSwitchIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
