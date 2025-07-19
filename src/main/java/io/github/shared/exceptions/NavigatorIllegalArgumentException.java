package io.github.shared.exceptions;

public class NavigatorIllegalArgumentException extends NavigatorRuntimeException {

    public NavigatorIllegalArgumentException(String message) {
        super(message);
    }

    public NavigatorIllegalArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public NavigatorIllegalArgumentException(Throwable cause) {
        super(cause);
    }
}
