package io.github.shared.events;

import io.github.shared.exceptions.EventBusIllegalArgumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.EventObject;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;

import java.util.function.Consumer;


/**
 * A class that acts either as a {@link EventObject} dispatcher or {@link EventObject} publisher providing a unified API
 * for both {@link EventListener} management and {@link EventObject} publishing.
 * <p>
 * Supports both {@code sync} and {@code async} publishing of {@link EventObject}s.
 * <p>
 * Thread-safe.
 * @since 0.1.0
 */
public class EventBus {

    private static final Logger log = LoggerFactory.getLogger(EventBus.class);
    private final Map<Class<?>, List<EventListener<? extends EventObject>>> listeners = new ConcurrentHashMap<>();
    private final Executor executor;

    /**
     * @throws IllegalArgumentException in case if the submitted {@code executor} parameter is null.
     */
    public EventBus(final Executor executor, final Collection<EventListener<? extends EventObject>> eventListeners) {
        this(executor);
        registerAll(eventListeners);
    }

    /**
     * @throws EventBusIllegalArgumentException in case if the submitted {@code executor} parameter is null.
     */
    public EventBus(final Executor executor) {
        Executor executorToAssign;

        if (executor == null) {
            throw new EventBusIllegalArgumentException("'executor' parameter must not be null!");
        } else {
            executorToAssign = executor;
        }
        this.executor = executorToAssign;
    }


    /**
     * Publishes an {@link EventObject}.
     * <p>
     * Supports both {@code sync} and {@code async} publication modes which are be specified by a {@code isAsync} method parameter.
     *
     * @param event an {@link EventObject} instance.
     * @param isAsync a boolean flag for specifying {@link EventObject} publishing mode: {@code true} is for ASYNC, {@code false} is for SYNC respectively.
     */
    public <E extends EventObject> void publish(final E event, final boolean isAsync) {
        if (isAsync) {
            publishAsync(event);
        } else {
            publishSync(event);
        }
    }

    /**
     * Publishes an {@link EventObject} synchronously.
     *
     * @param event an {@link EventObject} instance.
     */
    public <E extends EventObject> void publishSync(final E event) {
        dispatchEvent(event, listener -> {
            try {
                listener.onEvent(event);
            } catch (Exception e) {
                handleEventPublishingException(event, listener, e);
            }
        });
    }

    /**
     * Publishes an {@link EventObject} asynchronously via the {@link Executor} submitted to this {@link EventBus}
     * as a constructor parameter.
     *
     * @param event an {@link EventObject} instance.
     */
    public <E extends EventObject> void publishAsync(final E event) {
        dispatchEvent(event, listener -> {
            try {
                executor.execute(() -> listener.onEvent(event));
            } catch (Exception e) {
                handleEventPublishingException(event, listener, e);
            }
        });
    }

    /**
     * Registers a bunch of {@link EventListener} instances to the current {@link EventBus}.
     *
     * @param eventListeners a {@link Collection} of {@link EventListener}s to be registered.
     */
    public void registerAll(final Collection<EventListener<? extends EventObject>> eventListeners) {
        if (eventListeners != null) {
            eventListeners.forEach(this::register);
        }
    }

    /**
     * Registers a particular {@link EventListener} instance to the current {@link EventBus}.
     *
     * @param eventListener an {@link EventListener} instance to be registered.
     */
    public void register(final EventListener<? extends EventObject> eventListener) {
        if (eventListener != null) {
            listeners.computeIfAbsent(eventListener.getType(), k -> new CopyOnWriteArrayList<>()).add(eventListener);
        }
    }

    /**
     * Unregisters all the previously registered {@link EventListener}s from the current {@link EventBus}.
     */
    public void unregisterAll() {
        listeners.clear();
    }

    /**
     * Unregister all the {@link EventListener}s bound to a particular {@link EventObject} type.
     *
     * @param eventType type of particular {@link EventObject} to which {@link EventListener}s which are meant ot be unregistered are mapped.
     */
    public void unregisterAllByEventType(final Class<? extends EventObject> eventType) {
        if (eventType != null) {
            listeners.remove(eventType);
        }
    }

    /**
     * Unregister a particular {@link EventListener} instance from the current {@link EventBus}.
     *
     * @param eventListener an {@link EventListener} instance to be unregistered from the current {@link EventBus}.
     */
    public void unregister(final EventListener<? extends EventObject> eventListener) {
        if (eventListener == null) {
            return;
        }

        List<EventListener<?>> eventListeners = listeners.get(eventListener.getType());

        if (eventListeners != null && !eventListeners.isEmpty()) {
            eventListeners.removeIf(listener -> listener.equals(eventListener));
        }
    }

    /**
     * Checks whether any {@link EventListener} is mapped to the submitted {@code eventType}.
     *
     * @param eventType type of {@link EventObject} to be assessed whether any {@link EventListener} to it.
     */
    public boolean isListenersRegisteredForEventType(final Class<? extends EventObject> eventType) {
        return eventType != null && listeners.containsKey(eventType);
    }

    public Executor getExecutor() {
        return executor;
    }

    protected void handleEventPublishingException(final EventObject event, EventListener<?> listener, final Throwable t) {
        log.error("Fail to publish event '{}' to listener '{}' due to '{}' - {}",
                (event == null) ? "<event_name_not_available>" : event.getClass().getSimpleName(),
                (listener == null) ? "<listener_name_not_available>" : listener.getClass().getSimpleName(),
                t.getClass().getSimpleName(), t.getMessage()
        );
    }

    private <E extends EventObject> void dispatchEvent(final E event, final Consumer<EventListener<E>> eventDispatchConsumer) {
        if (event == null) {
            return;
        }

        Class<? extends EventObject> eventClass = event.getClass();
        if (!isListenersRegisteredForEventType(eventClass)) {
            return;
        }

        List<EventListener<?>> eventListeners = listeners.getOrDefault(eventClass, Collections.emptyList());
        invokeListeners(eventListeners, eventDispatchConsumer);
    }

    @SuppressWarnings("unchecked")
    private <E extends EventObject> void invokeListeners(
            List<EventListener<?>> eventListeners,
            Consumer<EventListener<E>> eventConsumer
    ) {
        if (eventConsumer == null) {
            return;
        }

        for (EventListener<?> listener : eventListeners) {
            eventConsumer.accept((EventListener<E>) listener);
        }
    }
}
