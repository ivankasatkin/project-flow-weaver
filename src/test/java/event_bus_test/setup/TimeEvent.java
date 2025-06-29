package event_bus_test.setup;

import io.github.shared.events.TimelineEvent;

import java.time.Duration;
import java.time.Instant;

public class TimeEvent extends TimelineEvent {

    private final String title;

    public TimeEvent(Object source, String title) {
        super(source);
        this.title = title;
    }

    public TimeEvent(Object source, Instant startTime, Duration duration, String title) {
        super(source, startTime, duration);
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
