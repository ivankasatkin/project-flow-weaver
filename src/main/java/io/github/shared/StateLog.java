package io.github.shared;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.shared.annotations.ToStateLog;
import io.github.shared.contracts.InstanceNameAware;
import io.github.shared.exceptions.StateLogEntryIllegalArgumentException;
import io.github.shared.exceptions.StateLogIllegalArgumentException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Collectors;


/**
 * A thread-safe class that holds, populates and exports a sequence of arbitrary objects' states in a for of a log.
 * Uses internal class {@link Entry} to create a log entry that is appended to the log.
 * <p>
 * Exports log either as an immutable {@link Map} snapshot or as a JSON string.
 */
public class StateLog extends BlockingReadWriteLockWrapper {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final Gson GSON = new GsonBuilder().serializeNulls().create();
    private final List<Entry> log = new ArrayList<>();


    /**
     * Creates a {@link StateLog} instance containing no {@link Entry}s.
     *
     * @return an empty {@link StateLog} instance.
     */
    public static StateLog empty() {
        return new StateLog();
    }

    /**
     * Creates a {@link StateLog} instance containing a single {@link Entry}.
     *
     * @return a {@link StateLog} having a single {@link Entry}.
     * @throws StateLogIllegalArgumentException in case if the submitted {@link Entry} is null.
     */
    public static StateLog fromEntry(StateLog.Entry stateLogEntry) {
        if (stateLogEntry == null) {
            throw new StateLogIllegalArgumentException("State log entry must not be null!");
        }
        return new StateLog(Collections.singletonList(stateLogEntry));
    }

    /**
     * Creates a {@link StateLog} instance containing n-th amount of {@link Entry} defined within the submitted {@link List}.
     *
     * @return a {@link StateLog} containing n-th quantity of non-null {@link Entry} values specified within the submitted {@link List}.
     * @throws StateLogIllegalArgumentException in case if the submitted {@link List} parameter is null.
     */
    public static StateLog fromEntryList(List<StateLog.Entry> stateLogEntryList) {
        return new StateLog(stateLogEntryList);
    }

    /**
     * Creates a {@link StateLog} instance from another {@link StateLog}.
     *
     * @return a copy of other {@link StateLog}.
     * @throws StateLogIllegalArgumentException in case if the submitted {@link StateLog} parameter is null.
     */
    public static StateLog fromOther(StateLog other) {
        if (other == null) {
            throw new StateLogIllegalArgumentException("Cannot create state log from other as the submitted state log is null!");
        }
        return new StateLog(other.log);
    }

    /**
     * Appends a newly created {@link Entry} to a {@link StateLog}.
     * <p>
     * Could be used as a builder method for the {@link StateLog} instance.
     * <p>
     * Thread-safe.
     *
     * @param instanceNameAware an arbitrary {@link InstanceNameAware} implementor.
     * @return a {@link StateLog} instance.
     */
    public StateLog append(InstanceNameAware instanceNameAware) {
        invokeAppend(() -> new Entry(buildLogEntryTitle(instanceNameAware), instanceNameAware));
        return this;
    }

    /**
     * Appends a newly created {@link Entry} to {@link StateLog}.
     * <p>
     * Could be used as a builder method for the {@link StateLog} instance.
     * <p>
     * Thread-safe.
     *
     * @param title   a String representation of a {@link Entry} title.
     * @param content an arbitrary {@link Entry} content in a form of an Object.
     * @return a {@link StateLog} instance.
     */
    public StateLog append(String title, Object content) {
        invokeAppend(() -> new Entry(buildLogEntryTitle(title), content));
        return this;
    }

    /**
     * Returns a {@link StateLog} log size (a quantity of {@link Entry} within a log).
     *
     * @return {@link StateLog} log size (a quantity of {@link Entry} within a log).
     */
    public int size() {
        return withReadLock(log::size);
    }

    /**
     * Exports a {@link StateLog} as a JSON string.
     * <p>
     * Thread-safe.
     *
     * @return a JSON representation of a {@link StateLog}.
     */
    public String asJson() {
        return withReadLock(() -> toJson(log));
    }

    /**
     * Exports a {@link StateLog} as a {@link Map} representation.
     * <p>
     * Thread-safe.
     *
     * @return a {@link Map} representation of a {@link StateLog}.
     */
    public Map<String, Map<String, Object>> asMap() {
        return withReadLock(() -> (log.isEmpty())
                ? Collections.emptyMap()
                : log.stream().collect(
                LinkedHashMap::new,
                (map, entry) -> map.put(entry.title, entry.content),
                LinkedHashMap::putAll
        ));
    }


    private static Object copyAsObject(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return GSON.fromJson(GSON.toJson(obj), obj.getClass());
        } catch (Exception e) {
            return "<cannot_copy_object>";
        }
    }

    private static String toJson(Object obj) {
        try {
            return GSON.toJson(obj);
        } catch (Exception e) {
            return "<unable_to_get_json>";
        }
    }

    private String buildLogEntryTitle(InstanceNameAware instanceNameAwareInstance) {
        return buildLogEntryTitle((instanceNameAwareInstance == null) ? null : instanceNameAwareInstance.getInstanceName());
    }

    private String buildLogEntryTitle(String name) {
        return (StringUtils.isBlank(name)) ? null : (log.size() + 1) + ". " + name;
    }

    private void invokeAppend(Supplier<Entry> stateLogSupplier) {
        withWriteLock(() -> log.add(stateLogSupplier.get()));
    }


    private StateLog() {
        super(true);
    }

    private StateLog(List<StateLog.Entry> stateLogEntries) {
        super(true);
        if (stateLogEntries == null) {
            throw new StateLogIllegalArgumentException("Log entries list must not be null!");
        }
        log.addAll(stateLogEntries.stream().filter(Objects::nonNull).collect(Collectors.toList()));
    }


    /**
     * A static nested class of a {@link StateLog} that holds log entry title, timestamp and content in a form of an Object.
     * <p>
     * Immutable.
     */
    public static class Entry {

        private final String title;
        private final String timestamp;
        private final Map<String, Object> content = new LinkedHashMap<>();

        public Entry(String title, Object obj) {
            if (StringUtils.isBlank(title)) {
                throw new StateLogEntryIllegalArgumentException("Log entry 'title' must not be null!");
            }
            this.title = title;
            this.timestamp = TIMESTAMP_FORMATTER.format(LocalDateTime.now());
            this.content.putAll(buildContent(obj));
        }

        public String getTitle() {
            return title;
        }

        public String getTimestampAsString() {
            return timestamp;
        }

        public LocalDateTime getTimestampAsLocalDateTime() {
            return LocalDateTime.parse(timestamp, TIMESTAMP_FORMATTER);
        }

        public Map<String, Object> getContent() {
            return content;
        }


        private static Map<String, Object> buildContent(Object obj) {
            if (obj == null) {
                throw new StateLogEntryIllegalArgumentException("Cannot log object's state as the submitted object is null!");
            }

            Map<String, Object> content = new LinkedHashMap<>();
            Class<?> currentClass = obj.getClass();

            while (currentClass != Object.class) {
                List<Field> fields = Arrays.asList(currentClass.getDeclaredFields());
                List<Field> loggableFields = fields.stream()
                        .filter(field -> field.isAnnotationPresent(ToStateLog.class))
                        .collect(Collectors.toList());

                if (loggableFields.isEmpty()) {
                    logDiscoveredFields(fields, obj, content);
                } else {
                    logDiscoveredFields(loggableFields, obj, content);
                }
                currentClass = currentClass.getSuperclass();
            }
            return content;
        }

        private static void logDiscoveredFields(List<Field> fields, Object obj, Map<String, Object> content) {
            fields.forEach(field -> {
                try {
                    boolean isFieldInitiallyAccessible = false;
                    if (isFieldInitiallyAccessible == field.isAccessible()) {
                        field.setAccessible(true);
                    }
                    Object value = field.get(obj);
                    content.put(field.getName(), copyAsObject(value));
                    if (!isFieldInitiallyAccessible && field.isAccessible()) {
                        field.setAccessible(false);
                    }
                } catch (Exception ignore) {
                }
            });
        }
    }
}
