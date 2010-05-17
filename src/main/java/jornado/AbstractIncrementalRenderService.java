package jornado;

/**
 * A base class for incremental rendering services.
 * @author john
 */
public abstract class AbstractIncrementalRenderService implements RenderService {
    public abstract Iterable<byte[]> render(Body body);

    public final boolean isIncremental() {
        return true;
    }

    public final byte[] renderFully(Body body) {
        throw new UnsupportedOperationException();
    }
}
