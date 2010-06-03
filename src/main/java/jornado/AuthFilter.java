package jornado;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * A filter that redirects to the loginUrl if the handler class specifies it requires authentication
 * and the user isn't currently logged in.
 *
 * @author john
 */
@Singleton
public class AuthFilter<R extends Request> extends BaseFilter<R> {
  private final String loginUrl;

  @Inject
  public AuthFilter(@Named("loginUrl") String loginUrl) {
    this.loginUrl = loginUrl;
  }

  @Override
  public Response before(R request, Class<? extends Handler<R>> handlerClass) {
    if (handlerClass.getAnnotation(RequiresLogin.class) != null && !request.isLoggedIn()) {
      return new RedirectResponse(loginUrl + "?target=" + request.getReconstructedUrl());
    } else {
      return null;
    }
  }
}
