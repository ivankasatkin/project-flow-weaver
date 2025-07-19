package io.github.shared.exceptions;

public class NavigatorRuntimeException extends RuntimeException {

    public NavigatorRuntimeException(String message) {
        super(message);
    }

    public NavigatorRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NavigatorRuntimeException(Throwable cause) {
        super(cause);
    }
}
