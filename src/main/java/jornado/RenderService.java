package jornado;

/**
 * Renders an object to a byte[]. Optionally supports incremental renderers.
 * TODO: consider splitting interfaces rather than this ugly design
 */
public interface RenderService {
    boolean isIncremental();
    byte[] renderFully(Body obj);
    Iterable<byte[]> render(Body obj);
}