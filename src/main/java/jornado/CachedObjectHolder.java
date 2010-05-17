package jornado;

/**
 * A simple object holder. Useful for negative result caching.
 * @param <T>
 */
public class CachedObjectHolder<T> {
    private final T object;

    public CachedObjectHolder(T object) {
        this.object = object;
    }

    public T get() {
        return object;
    }

    public boolean exists() {
        return object != null;
    }
}
