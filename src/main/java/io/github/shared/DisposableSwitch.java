package io.github.shared;

import io.github.shared.exceptions.DisposableSwitchAlreadyDisposedException;
import io.github.shared.exceptions.DisposableSwitchIllegalArgumentException;
import io.github.shared.exceptions.DisposableSwitchModeRegistryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

/**
 * A blocking thread-safe {@link Function} (operation) execution dispatcher that acts mostly like a {@code switch}
 * excepting the fact that cases are categorized into two groups: {@code permitted} and {@code restricted} modes.
 * <p>
 * When using {@code permitted} modes, the current class acts entirely as an ordinary Java {@code switch} and
 * the corresponding operation could be executed unlimited amount of times.
 * <p>
 * On the contrary, once any {@code restricted} mode is reached - it executes the designated operation
 * {@code only once} and then {@link DisposableSwitch#dispose()} the {@link DisposableSwitch} instance making it no longer operable.
 * <p>
 * Moreover, reaching a non-specified mode (e.g. null or unexpected value) would also {@link DisposableSwitch#dispose()}
 * the current {@link DisposableSwitch} instance.
 * <p>
 * Allows using {@code null} qua "switch" value for both {@code permitted} and {@code restricted} modes.
 *
 * @author Ivan Kasatkin
 * @since 0.1.0
 */
public class DisposableSwitch<S, T, R> extends BlockingReadWriteLockWrapper {

    private final Map<S, Function<T, R>> permittedModesRegistry = new HashMap<>();
    private final Map<S, Function<T, R>> restrictedModesRegistry = new HashMap<>();
    private volatile boolean isDisposed = false;


    public DisposableSwitch() {
        this(false);
    }

    public DisposableSwitch(boolean isFair) {
        super(isFair);
    }

    /**
     * @throws DisposableSwitchIllegalArgumentException in case if null is passed qua {@code permittedOperation}.
     */
    public DisposableSwitch(final S permittedMode, final Function<T, R> permittedOperation) {
        this(permittedMode, permittedOperation, false);
    }

    /**
     * @throws DisposableSwitchIllegalArgumentException in case if null is passed qua {@code permittedOperation}.
     */
    public DisposableSwitch(final S permittedMode, final Function<T, R> permittedOperation, final boolean isReadWriteLockFair) {
        super(isReadWriteLockFair);
        validateOperation(permittedOperation);
        permittedModesRegistry.put(permittedMode, permittedOperation);
    }

    /**
     * Calls the operation ({@link Function}) corresponding to the submitted mode and wraps the result into an {@link Optional}.
     * <p>
     * Returns {@link Optional#empty()} and disposes ({@link DisposableSwitch#dispose()}) the current {@link DisposableSwitch}
     * instance if the non-specified mode (e.g. null or unexpected value) is used qua method parameter.
     * <p>
     * Thread-safe.
     *
     * @param mode  a reached mode case (allows {@code null}).
     * @param input an input for the operation ({@link Function}) corresponding to the submitted mode parameter.
     * @return an {@link Optional} of an execution result of a {@link Function} corresponding to the submitted mode parameter.
     * Return {@link Optional#empty()} in case if an exception occurred during the {@link Function} execution or in case if the
     * method was called on the already disposed {@link DisposableSwitch#isDisposed()} instance.
     */
    public Optional<R> tryApply(final S mode, final T input) {
        try {
            return Optional.ofNullable(apply(mode, input));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Calls the operation ({@link Function}) corresponding to the submitted mode.
     * <p>
     * Returns {@code null} and disposes ({@link DisposableSwitch#dispose()}) the current {@link DisposableSwitch}
     * instance if the non-specified mode (e.g. null or unexpected value) is used qua method parameter.
     * <p>
     * Thread-safe.
     *
     * @param mode  a reached mode case (allows {@code null}).
     * @param input an input for the operation ({@link Function}) corresponding to the submitted mode parameter.
     * @return an execution result of an operation ({@link Function}) corresponding to the submitted mode parameter.
     * @throws DisposableSwitchAlreadyDisposedException in case if the current {@link DisposableSwitch#dispose()} has been already called.
     */
    public R apply(final S mode, final T input) {
        if (isDisposed()) {
            throw new DisposableSwitchAlreadyDisposedException("This disposable mode has already been used for a restricted function and no longer accessible!");
        }
        return withReadLock(() -> {
            if (isModeNotRegistered(mode)) {
                dispose();
                return null;
            } else if (restrictedModesRegistry.containsKey(mode)) {
                dispose();
                return restrictedModesRegistry.get(mode).apply(input);
            } else {
                return permittedModesRegistry.get(mode).apply(input);
            }
        });
    }

    /**
     * Adds a permitted mode into the current {@link DisposableSwitch} instance.
     * <p>
     * Could be used as a builder method for a {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     *
     * @param permittedMode      a restricted mode to be added to the current {@link DisposableSwitch} instance (allows {@code null}).
     * @param permittedOperation an operation ({@link Function}) to be executed once the designated permittedMode is reached.
     * @return the current instance of {@link DisposableSwitch}.
     * @throws DisposableSwitchIllegalArgumentException in case if null is passed qua {@code permittedOperation}.
     * @throws DisposableSwitchModeRegistryException    in case if the submitted mode has already been registered.
     */
    public DisposableSwitch<S, T, R> addPermittedMode(final S permittedMode, final Function<T, R> permittedOperation) {
        registerMode(permittedMode, permittedOperation, permittedModesRegistry);
        return this;
    }

    /**
     * Adds a restricted mode into the current {@link DisposableSwitch} instance.
     * <p>
     * Could be used as a builder method for a {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     *
     * @param restrictedMode      a restricted mode to be added to the current
     *                            {@link DisposableSwitch} instance (allows {@code null}).
     * @param restrictedOperation an operation ({@link Function}) to be executed once the designated restrictedMode is reached.
     * @return the current instance of {@link DisposableSwitch}.
     * @throws DisposableSwitchIllegalArgumentException in case if null is passed qua {@code permittedOperation}.
     * @throws DisposableSwitchModeRegistryException    in case if the submitted mode has already been registered.
     */
    public DisposableSwitch<S, T, R> addRestrictedMode(final S restrictedMode, final Function<T, R> restrictedOperation) {
        registerMode(restrictedMode, restrictedOperation, restrictedModesRegistry);
        return this;
    }

    /**
     * Removes a particular permitted mode from the current {@link DisposableSwitch} instance.
     * <p>
     * Could be used as a builder method for a {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     *
     * @param permittedMode a permitted mode to be removed from the current
     *                      {@link DisposableSwitch} instance (allows {@code null}).
     * @return the current instance of {@link DisposableSwitch}.
     */
    public DisposableSwitch<S, T, R> removePermittedMode(final S permittedMode) {
        withWriteLock(permittedMode, ps -> {
            permittedModesRegistry.remove(ps);
        });
        return this;
    }

    /**
     * Removes a particular restricted mode from the current {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     *
     * @param restrictedMode a restricted mode to be removed from the current {@link DisposableSwitch} instance.
     */
    public DisposableSwitch<S, T, R> removeRestrictedMode(final S restrictedMode) {
        withWriteLock(restrictedMode, rs -> {
            restrictedModesRegistry.remove(rs);
        });
        return this;
    }

    /**
     * Removes both all permitted and restricted modes from the current {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     */
    public void clearAllModes() {
        withWriteLock(new Object(), consumer -> {
            permittedModesRegistry.clear();
            restrictedModesRegistry.clear();
        });
    }

    /**
     * Removes all the permitted modes from the current {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     */
    public void clearPermittedModes() {
        withWriteLock(new Object(), consumer -> {
            permittedModesRegistry.clear();
        });
    }

    /**
     * Removes all the restricted modes from the current {@link DisposableSwitch} instance.
     * <p>
     * Thread-safe.
     */
    public void clearRestrictedModes() {
        withWriteLock(new Object(), consumer -> {
            restrictedModesRegistry.clear();
        });
    }

    /**
     * Explicitly disposes the current {@link DisposableSwitch} making it no longer operable.
     */
    public void dispose() {
        isDisposed = true;
    }

    public boolean isDisposed() {
        return isDisposed;
    }

    /**
     * Returns the current size of permitted modes' registry.
     * <p>
     * Thread-Safe.
     *
     * @return an int representation of permitted modes registry's size.
     */
    public int getPermittedModesRegistrySize() {
        return new HashMap<>(permittedModesRegistry).size();
    }

    /**
     * Returns the current size of restricted modes' registry.
     * <p>
     * Thread-Safe.
     *
     * @return an int representation of restricted modes registry's size.
     */
    public int getRestrictedModesRegistrySize() {
        return new HashMap<>(restrictedModesRegistry).size();
    }


    private boolean isModeNotRegistered(final S mode) {
        return !permittedModesRegistry.containsKey(mode) && !restrictedModesRegistry.containsKey(mode);
    }

    private void validateOperation(final Function<T, R> operation) {
        if (operation == null) {
            throw new DisposableSwitchIllegalArgumentException("Operation parameter must not be null!");
        }
    }

    private void registerMode(final S mode, final Function<T, R> operation, Map<S, Function<T, R>> registry) {
        validateOperation(operation);
        withWriteLock(mode, m -> {
            if (isModeNotRegistered(m)) {
                registry.put(m, operation);
            } else {
                clarifyModeRegistrationException(m);
            }
        });
    }

    private void clarifyModeRegistrationException(final S mode) {
        String passedMode = (mode == null) ? null : mode.toString();
        String errorMessageTemplate = "Mode '%s' is already registered as %s operation!";
        String filledErrorMessage = permittedModesRegistry.containsKey(mode)
                ? String.format(errorMessageTemplate, passedMode, "permitted")
                : String.format(errorMessageTemplate, passedMode, "restricted");

        throw new DisposableSwitchModeRegistryException(filledErrorMessage);
    }
}
