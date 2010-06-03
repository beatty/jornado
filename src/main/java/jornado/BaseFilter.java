package jornado;

/**
 * A base filter that does nothing.
 *
 * @author john
 */
public class BaseFilter<R extends Request> implements Filter<R> {
  @Override
  public Response before(R request, Class<? extends Handler<R>> handlerClass) {
    return null;
  }

  @Override
  public void after(R request, Response response, Class<? extends Handler<R>> handlerClass) {
  }
}
