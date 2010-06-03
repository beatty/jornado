package jornado;

/**
 * A request filter.
 *
 * @author john
 */
public interface Filter<R extends Request> {
  public Response before(R request, Class<? extends Handler<R>> handlerClass);
  public void after(R request, Response response, Class<? extends Handler<R>> handlerClass);
}
