package io.github.shared.events;

import java.util.EventObject;

/**
 * A fairly simple helper class for leveraging {@link EventBus} capabilities in a more safe and uniform way for the inheritor class.
 */
public abstract class EventPublisher {

    protected transient EventBus eventBus;

    public EventPublisher(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public EventPublisher() {
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public void setEventBus(final EventBus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * Publishes {@link EventObject} with {@link EventBus#publish(EventObject, boolean)}.
     *
     * @param event an {@link EventObject} instance.
     * @param isAsync a boolean flag for specifying {@link EventObject} publishing mode: {@code true} is for ASYNC, {@code false} is for SYNC respectively.
     * @see EventBus#publish(EventObject, boolean)
     */
    protected final <E extends EventObject> void publish(final E event, final boolean isAsync) {
        if (this.eventBus != null) {
            eventBus.publish(event, isAsync);
        }
    }

    /**
     * Publishes {@link EventObject} asynchronously with {@link EventBus#publishAsync(EventObject)}.
     *
     * @param event an {@link EventObject} instance.
     * @see EventBus#publishAsync(EventObject)
     */
    protected final <E extends EventObject> void publishAsync(final E event) {
        if (this.eventBus != null) {
            eventBus.publishAsync(event);
        }
    }

    /**
     * Publishes {@link EventObject} synchronously with {@link EventBus#publishSync(EventObject)}.
     *
     * @param event an {@link EventObject} instance.
     * @see EventBus#publishSync(EventObject)
     */
    protected final <E extends EventObject> void publishSync(final E event) {
        if (this.eventBus != null) {
            eventBus.publishSync(event);
        }
    }
}
