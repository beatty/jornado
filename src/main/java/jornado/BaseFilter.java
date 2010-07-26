package jornado;

/**
 * A base filter that does nothing.
 *
 * @author john
 */
public class BaseFilter<R extends Request> implements Filter<R> {
  @Override
  public Response filter(R request, Class<? extends Handler<R>> handlerClass, FilterChain<R> filterChain) {
    return filterChain.doFilter(request);
  }
}
