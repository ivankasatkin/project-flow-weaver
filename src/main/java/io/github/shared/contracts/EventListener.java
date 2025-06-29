package io.github.shared.contracts;

import java.util.EventObject;

public interface EventListener<E extends EventObject> extends TypeAware<E> {

    void onEvent(E event);
}
