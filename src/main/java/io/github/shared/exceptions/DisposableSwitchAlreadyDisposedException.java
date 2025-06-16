package io.github.shared.exceptions;

public class DisposableSwitchAlreadyDisposedException extends DisposableSwitchRuntimeException {

    public DisposableSwitchAlreadyDisposedException(String message) {
        super(message);
    }

    public DisposableSwitchAlreadyDisposedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisposableSwitchAlreadyDisposedException(Throwable cause) {
        super(cause);
    }
}
