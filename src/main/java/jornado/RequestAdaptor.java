package jornado;

/**
 * Lets applications support their own request type
 * @param <T>
 */
public interface RequestAdaptor<T extends Request> {
    T adapt(Request request);
}
