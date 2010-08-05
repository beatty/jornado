package jornado;

/**
 * A request filter.
 *
 * @author john
 */
public interface Filter<R extends Request> {
  public interface FilterChain<R extends Request> {
    Response doFilter(R request);
  }
  public Response filter(R request, Class<? extends Handler<R>> handlerClass, FilterChain<R> filterChain);
}
