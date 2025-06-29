package event_bus_test;

import event_bus_test.setup.SimpleEvent;
import event_bus_test.setup.TimeEvent;
import io.github.shared.EventBus;
import io.github.shared.contracts.EventListener;
import io.github.shared.exceptions.EventBusIllegalArgumentException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Executor;

import static org.junit.jupiter.api.Assertions.*;

public class EventBusTest {

    static Map<String, Object> eventListeningResult;
    EventBus eventBus;
    Executor executor;
    EventListener<SimpleEvent> simpleEventListener;
    EventListener<SimpleEvent> anotherSimpleEventListener;
    EventListener<TimeEvent> timeEventListener;


    @BeforeEach
    void init() {
        eventListeningResult = new HashMap<>();
        executor = command -> {
            Thread t = new Thread(command);
            t.start();
        };
        eventBus = new EventBus(executor);
        simpleEventListener = new SimpleEventListener();
        anotherSimpleEventListener = new AnotherSimpleEventListener();
        timeEventListener = new TimeEventListener();
    }

    @AfterEach
    void tearDown() {
        executor = null;
        eventBus = null;
        eventListeningResult = null;
        simpleEventListener = null;
        anotherSimpleEventListener = null;
        timeEventListener = null;
    }


    @Test
    @DisplayName("Try constructing EventBus with null Executor")
    void constructor_PassNull_Executor_ThrowEventBusIllegalArgumentException() {
        assertThrows(EventBusIllegalArgumentException.class, () -> new EventBus(null));
    }

    @Test
    @DisplayName("Try constructing EventBus with non-null executor and null EventListeners collection")
    void constructor_PassNonNullExecutorAndNullEventListeners_DoesNotThrowException() {
        assertDoesNotThrow(() -> new EventBus(executor, null));
    }

    @Test
    @DisplayName("Try constructing EventBus with non-null executor and empty EventListeners collection")
    void constructor_PassNonNullExecutorAndEmptyEventListeners_DoesNotThrowException() {
        assertDoesNotThrow(() -> new EventBus(executor, Collections.emptySet()));
    }

    @Test
    @DisplayName("Register null EventListener")
    void register_PassNullEventListener_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.register(null));
    }

    @Test
    @DisplayName("Register all with EventListener collection")
    void registerAll_PassNullEventListenerCollection_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.registerAll(null));
    }

    @Test
    @DisplayName("Register all with empty EventListener collection")
    void registerAll_PassEmptyEventListenerCollection_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.registerAll(Collections.emptyList()));
    }

    @Test
    @DisplayName("Register all with non-empty EventListener collection containing only nulls")
    void registerAll_PassAllNullEventListenerCollection_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.registerAll(Arrays.asList(null, null, null, null, null, null)));
    }

    @Test
    @DisplayName("Register all with non-empty EventListener collection containing both null and non-null event listeners")
    void registerAll_PassEventListenerCollectionWithNullAndNonNullEventListeners_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.registerAll(Arrays.asList(simpleEventListener, null)));
    }

    @Test
    @DisplayName("Unregister event listener with null from empty Eventbus")
    void unregister_PassNullToEmptyEventBus_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.unregister(null));
    }

    @Test
    @DisplayName("Unregister event listener with null from non-empty EventBus")
    void unregister_PassNullToNonEmptyEventBuss_DoesNotThrowException() {
        registerAllListenersAndNull();
        assertDoesNotThrow(() -> eventBus.unregister(null));
    }

    @Test
    @DisplayName("Unregister all event listeners by event type fro empty EventBus using null event type")
    void unregisterAllByEventType_PassNullToEmptyEventBus_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.unregisterAllByEventType(null));
    }

    @Test
    @DisplayName("Unregister all event listeners by event type fro non-empty EventBus using null event type")
    void unregisterAllByEventType_PassNullToNonEmptyEventBus_DoesNotThrowException() {
        registerAllListenersAndNull();
        assertDoesNotThrow(() -> eventBus.unregisterAllByEventType(null));
    }

    @Test
    @DisplayName("Unregister all event listeners by event type fro non-empty EventBus by non-existent event type")
    void unregisterAllByEventType_PassNonExistentToNonEmptyEventBus_DoesNotThrowException() {
        eventBus.registerAll(Arrays.asList(simpleEventListener, anotherSimpleEventListener));
        assertDoesNotThrow(() -> eventBus.unregisterAllByEventType(TimeEvent.class));
    }

    @Test
    @DisplayName("Unregister all event listeners from empty EventBus")
    void unregisterAll_CallOnEmptyEventBus_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.unregisterAll());
    }

    @Test
    @DisplayName("Unregister all event listeners from non-empty EventBus with all registered event listeners being nulls")
    void unregisterAll_CallOnNonEmptyEventBusWithNullEventListeners_DoesNotThrowException() {
        eventBus.registerAll(Arrays.asList(null, null, null, null, null));
        assertDoesNotThrow(() -> eventBus.unregisterAll());
    }

    @Test
    @DisplayName("Pass null as event type to empty EventBus while trying to find out whether any event listeners are registered for the submitted type")
    void isListenersRegisteredForEventType_PassNullToEmptyEventBus_ReturnFalse() {
        assertFalse(eventBus.isListenersRegisteredForEventType(null));
    }

    @Test
    @DisplayName("Pass null as event type to non-empty EventBus while trying to find out whether any event listeners are registered for the submitted type")
    void isListenersRegisteredForEventType_PassNullToNonEmptyEventBus_ReturnFalse() {
        registerAllListenersAndNull();
        assertFalse(eventBus.isListenersRegisteredForEventType(null));
    }

    @Test
    @DisplayName("Pass existent event type to non-empty EventBus while trying to find out whether any event listeners are registered for the submitted type")
    void isListenersRegisteredForEventType_PassExistentEventType_ReturnTrue() {
        registerAllListenersAndNull();
        assertTrue(eventBus.isListenersRegisteredForEventType(TimeEvent.class));
    }

    @Test
    @DisplayName("Publish null event with empty EventBus")
    void publish_PassNullEventToEmptyEventBus_DoesNotThrowException() {
        assertDoesNotThrow(() -> eventBus.publishSync(null));
    }

    @Test
    @DisplayName("Publish null event with non-empty EventBus")
    void publish_PassNullEventToNonEmptyEventBus_DoesNotThrowException() {
        registerAllListenersAndNull();
        assertDoesNotThrow(() -> eventBus.publishSync(null));
    }

    @Test
    @DisplayName("Publish null event with non-empty EventBus to address any of event listeners")
    void publish_PassNullEventToNonEmptyEventBus_NoEventListenersAreAddressed() {
        eventBus.publishSync(null);
        assertTrue(eventListeningResult.isEmpty());
    }


    @Test
    @DisplayName("Publish non-null event to non-empty EventBus to address event listeners of a particular type")
    void publish_PassNonNullEventToNonEmptyEventBus_ReturnExpectedEventListenersTriggeringResult() {
        registerAllListenersAndNull();
        eventBus.publishSync(new SimpleEvent(this, "John", "+123456789"));
        assertEquals(2, eventListeningResult.size());
        assertFalse(eventListeningResult.containsKey("timeEvent"));
    }


    static class SimpleEventListener implements EventListener<SimpleEvent> {

        @Override
        public void onEvent(SimpleEvent event) {
            eventListeningResult.put("simpleEvent", event);
        }

        @Override
        public Class<SimpleEvent> getType() {
            return SimpleEvent.class;
        }
    }

    static class AnotherSimpleEventListener implements EventListener<SimpleEvent> {

        @Override
        public void onEvent(SimpleEvent event) {
            eventListeningResult.put("anotherSimpleEvent", event);
        }

        @Override
        public Class<SimpleEvent> getType() {
            return SimpleEvent.class;
        }
    }

    static class TimeEventListener implements EventListener<TimeEvent> {

        @Override
        public void onEvent(TimeEvent event) {
            eventListeningResult.put("timeEvent", event);
        }

        @Override
        public Class<TimeEvent> getType() {
            return TimeEvent.class;
        }
    }


    private void registerAllListenersAndNull() {
        eventBus.registerAll(Arrays.asList(simpleEventListener, anotherSimpleEventListener, timeEventListener, null));
    }
}
