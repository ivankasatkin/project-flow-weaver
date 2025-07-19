package io.github.shared;

import io.github.shared.exceptions.NavigatorIllegalArgumentException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Navigator<E> extends BlockingReadWriteLockWrapper {

    private final List<E> list;
    int cursor = -1;


    public Navigator(final List<E> list) {
        super(true);
        if (list == null) {
            throw new NavigatorIllegalArgumentException("List cannot be null");
        }
        this.list = new ArrayList<>(list);
    }

    public Optional<E> getByIndex(final int index) {
        return withReadLock(() -> {
            if (isCursorWithinListRange(index)) {
                return Optional.ofNullable(list.get(index));
            }
            return Optional.empty();
        });
    }

    public Optional<E> current() {
        return withReadLock(() -> {
            if (isCursorWithinListRange(cursor)) {
                return Optional.ofNullable(list.get(cursor));
            }
            return Optional.empty();
        });
    }

    public Optional<E> nextNonNull() {
        return withReadLock(cursor, c -> {
            while (hasNext()) {
                ++cursor;
                if (current().isPresent()) {
                    return current();
                }
            }
            return Optional.empty();
        });
    }

    public Optional<E> previousNonNull() {
        return withReadLock(cursor, c -> {
            while (hasPrevious()) {
                --cursor;
                if (current().isPresent()) {
                    return current();
                }
            }
            return Optional.empty();
        });
    }

    public Optional<E> firstNonNull() {
        return withReadLock(() -> {
            if (list.isEmpty()) {
                return Optional.empty();
            }

            cursor = -1;
            return nextNonNull();
        });
    }

    public Optional<E> lastNonNull() {
        return withReadLock(() -> {
            if (list.isEmpty()) {
                return Optional.empty();
            }

            cursor = list.size() - 1;
            return previousNonNull();
        });
    }

    public Optional<E> next() {
        return withReadLock(() -> {
            if (!hasNext()) {
                return Optional.empty();
            }
            return Optional.ofNullable(list.get(++cursor));
        });
    }

    public Optional<E> previous() {
        return withReadLock(() -> {
            if (!hasPrevious()) {
                return Optional.empty();
            }
            return Optional.ofNullable(list.get(--cursor));
        });
    }

    public Optional<E> first() {
        return withReadLock(() -> getByIndex(0));
    }

    public Optional<E> last() {
        return withReadLock(() -> getByIndex(list.size() - 1));
    }

    public boolean hasNext() {
        return withReadLock(() -> cursor + 1 < list.size());
    }

    public boolean hasPrevious() {
        return withReadLock(() -> cursor - 1 >= 0);
    }

    public boolean setNext(final E element) {
        return withWriteLock(() -> {
            if (!hasNext()) {
                return false;
            }
            list.set(cursor + 1, element);
            return true;
        });
    }

    public boolean setPrevious(final E element) {
        return withWriteLock(() -> {
            if (!hasPrevious()) {
                return false;
            }
            list.set(cursor - 1, element);
            return true;
        });
    }

    public boolean setCurrent(final E element) {
        return withWriteLock(() -> {
            if (isCursorWithinListRange(cursor)) {
                list.set(cursor, element);
                return true;
            }
            return false;
        });
    }

    public boolean setFirst(final E element) {
        return withWriteLock(() -> {
            if (list.isEmpty()) {
                return false;
            }
            list.set(0, element);
            return true;
        });
    }

    public boolean setLast(final E element) {
        return withWriteLock(() -> {
            if (list.isEmpty()) {
                return false;
            }
            list.set(list.size() - 1, element);
            return true;
        });
    }

    public boolean removeCurrent() {
        return withWriteLock(() -> {
            if (isCursorWithinListRange(cursor)) {
                list.remove(cursor);
                return true;
            }
            return false;
        });
    }

    public boolean removeNext() {
        return withWriteLock(() -> {
            if (!hasNext()) {
                return false;
            }
            list.remove(cursor + 1);
            return true;
        });
    }

    public boolean removePrevious() {
        return withWriteLock(() -> {
            if (!hasPrevious()) {
                return false;
            }
            list.remove(cursor - 1);
            return true;
        });
    }

    public boolean removeFirst() {
        return withWriteLock(() -> {
            if (list.isEmpty()) {
                return false;
            }
            list.remove(0);
            return true;
        });
    }

    public boolean removeLast() {
        return withWriteLock(() -> {
            if (list.isEmpty()) {
                return false;
            }
            list.remove(list.size() - 1);
            return true;
        });
    }

    public boolean moveToIndex(final int index) {
        return withReadLock(() -> {
            if (isCursorWithinListRange(index)) {
                cursor = index;
                return true;
            }
            return false;
        });
    }

    public void resetCursor() {
        withWriteLock(() -> cursor = -1);
    }

    public int getCurrentCursorPosition() {
        return cursor;
    }

    public int size() {
        return list.size();
    }

    public List<E> getListSnapshot() {
        return withReadLock(() -> new ArrayList<>(list));
    }

    public boolean isLast() {
        return withReadLock(() -> cursor == list.size() - 1);
    }

    private boolean isFirst() {
        return withReadLock(() -> cursor == 0);
    }

    private boolean isCursorWithinListRange(final int cursor) {
        return withReadLock(() -> !list.isEmpty() && cursor >= 0 && cursor < list.size());
    }
}
