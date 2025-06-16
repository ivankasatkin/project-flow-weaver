package io.github.shared.exceptions;

public class DisposableSwitchModeRegistryException  extends FlowWeaverRuntimeException {

    public DisposableSwitchModeRegistryException(String message) {
        super(message);
    }

    public DisposableSwitchModeRegistryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DisposableSwitchModeRegistryException(Throwable cause) {
        super(cause);
    }
}
