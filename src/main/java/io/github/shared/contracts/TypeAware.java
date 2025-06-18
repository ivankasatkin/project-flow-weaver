package io.github.shared.contracts;

public interface TypeAware<T> {

    Class<T> getType();
}
