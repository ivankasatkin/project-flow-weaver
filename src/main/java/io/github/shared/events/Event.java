package io.github.shared.events;

import java.time.Instant;
import java.util.EventObject;

/**
 * A simple {@link EventObject} inheritor designated to carry the state of an arbitrary event.
 * @since 0.1.0
 */
public abstract class Event extends EventObject {

    protected final Instant eventTimestamp;

    public Event(final Object source) {
        super(source);
        this.eventTimestamp = Instant.now();
    }

    public Instant getEventTimestamp() {
        return eventTimestamp;
    }
}
