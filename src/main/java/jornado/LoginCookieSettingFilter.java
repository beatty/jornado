package jornado;

import com.google.inject.Inject;

public class LoginCookieSettingFilter<R extends Request> extends BaseFilter<R>  {
  private final SecureCookieService secureCookieService;

  @Inject
  public LoginCookieSettingFilter(SecureCookieService secureCookieService) {
    this.secureCookieService = secureCookieService;
  }

  @Override
  public Response filter(R request, Class<? extends Handler<R>> handlerClass, FilterChain<R> chain) {
    final Response response = chain.doFilter(request);
    if (request.getLoginCookieNewValue() != null) {
      response.addHeaderOp(new SetUserCookieHeaderOp(secureCookieService.create(request.getLoginCookieNewValue())));
    }
    return response;
  }
}
