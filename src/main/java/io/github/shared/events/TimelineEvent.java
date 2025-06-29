package io.github.shared.events;

import java.time.Duration;
import java.time.Instant;

public abstract class TimelineEvent extends Event {

    protected final Instant startTime;
    protected final Duration duration;

    public TimelineEvent(Object source) {
        this(source, null, null);
    }

    public TimelineEvent(Object source, Instant startTime, Duration duration) {
        super(source);
        this.startTime = (startTime == null) ? Instant.now() : startTime;
        this.duration = (duration == null) ? Duration.ZERO : duration;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Duration getDuration() {
        return duration;
    }
}
