package jornado;

/**
 * Creates application-level requests given a delegate
 * @author john
 */
public interface RequestFactory<U extends WebUser, R extends Request<U>> {
  R createRequest(Request<U> delegate);
}
