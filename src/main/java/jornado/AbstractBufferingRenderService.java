package jornado;

/**
 * A base class for fully buffering render services.
 *
 * @author john
 */
public abstract class AbstractBufferingRenderService implements RenderService {
    public abstract byte[] renderFully(Body obj);

    public final boolean isIncremental() {
        return false;
    }

    public final Iterable<byte[]> render(Body obj) {
        throw new UnsupportedOperationException();
    }
}
