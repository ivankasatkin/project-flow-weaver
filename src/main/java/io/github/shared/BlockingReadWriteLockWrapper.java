package io.github.shared;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A fairly simple abstraction providing {@code blocking} read/write functionality around a single common reentrant {@link ReadWriteLock}.
 * Initializes with a {@code boolean} flag resembling {@link ReentrantReadWriteLock} fairness configuration.
 * <p>
 * {@code NB!} As the current implementation relies on {@link ReentrantReadWriteLock} functionality - this class supports
 * reentrancy {@code per thread}.
 *
 * @author Ivan Kasatkin
 * @since 0.1.0
 */
public abstract class BlockingReadWriteLockWrapper {

    private final ReadWriteLock lock;

    public BlockingReadWriteLockWrapper(boolean isFair) {
        this.lock = new ReentrantReadWriteLock(isFair);
    }

    /**
     * Wraps the execution of a {@link Function} with a blocking read lock/unlock structure.
     *
     * @param input              an input type for a read lock/unlock - wrapped {@link Function}.
     * @param readLockedFunction a {@link Function} to be wrapped within a read lock/unlock structure.
     * @return the result of a submitted {@link Function}.
     */
    public <T, R> R withReadLock(T input, Function<T, R> readLockedFunction) {
        try {
            lock.readLock().lock();
            return readLockedFunction.apply(input);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Wraps the execution of a {@link Function} with a blocking write lock/unlock structure.
     *
     * @param input               an input type for a write lock/unlock - wrapped {@link Function}.
     * @param writeLockedFunction a {@link Function} to be wrapped within a write lock/unlock structure.
     * @return the result of a submitted {@link Function}.
     */
    public <T, R> R withWriteLock(T input, Function<T, R> writeLockedFunction) {
        try {
            lock.writeLock().lock();
            return writeLockedFunction.apply(input);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Wraps the execution of a {@link Consumer} with a blocking read lock/unlock structure.
     *
     * @param input              an input type for a read lock/unlock - wrapped {@link Consumer}.
     * @param readLockedConsumer a {@link Consumer} to be wrapped within a read lock/unlock structure.
     */
    public <T> void withReadLock(T input, Consumer<T> readLockedConsumer) {
        try {
            lock.readLock().lock();
            readLockedConsumer.accept(input);
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Wraps the execution of a {@link Consumer} with a blocking write lock/unlock structure.
     *
     * @param input               an input type for a write lock/unlock - wrapped {@link Consumer}.
     * @param writeLockedConsumer a {@link Consumer} to be wrapped within a write lock/unlock structure.
     */
    public <T> void withWriteLock(T input, Consumer<T> writeLockedConsumer) {
        try {
            lock.writeLock().lock();
            writeLockedConsumer.accept(input);
        } finally {
            lock.writeLock().unlock();
        }
    }

    /**
     * Wraps the execution of a {@link Supplier} with a blocking read lock/unlock structure.
     *
     * @param readLockedSupplier a {@link Supplier} to be wrapped within a read lock/unlock structure.
     * @return the result of a submitted {@link Supplier}.
     */
    public <T> T withReadLock(Supplier<T> readLockedSupplier) {
        try {
            lock.readLock().lock();
            return readLockedSupplier.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    /**
     * Wraps the execution of a {@link Supplier} with a blocking write lock/unlock structure.
     *
     * @param writeLockedSupplier a {@link Supplier} to be wrapped within a write lock/unlock structure.
     * @return the result of a submitted {@link Supplier}.
     */
    public <T> T withWriteLock(Supplier<T> writeLockedSupplier) {
        try {
            lock.writeLock().lock();
            return writeLockedSupplier.get();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
