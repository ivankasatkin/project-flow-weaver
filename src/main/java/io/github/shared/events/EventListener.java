package io.github.shared.events;

import io.github.shared.contracts.TypeAware;

import java.util.EventObject;

public interface EventListener<E extends EventObject> extends TypeAware<E> {

    void onEvent(E event);
}
