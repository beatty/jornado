package demo;

import com.google.common.collect.Lists;
import com.google.inject.*;
import jornado.*;

import javax.servlet.http.Cookie;
import java.util.List;
import java.util.Random;

/**
 * Sample app
 */
public class DemoApp {
  private static final String B_COOKIE_NAME = "b";
  private static final int B_COOKIE_MAX_AGE = 3600 * 24 * 31 * 6;

  //
  // DEMO: declare app-specific types we're going to use -- normally we'd add extra stuff here
  //
  static class DemoUser implements WebUser {
    private final String id;

    DemoUser(String id) {
      this.id = id;
    }

    @Override
    public String getWebId() {
      return id;
    }
  }

  static interface DemoHandler extends jornado.Handler<DemoRequest> {
  }

  //
  // DEMO: applications can define their own request classes like so
  //

  static class DemoRequest extends RequestWrapper<DemoUser> {
    private String newBCookieValue; // set only in case where the 'b' cookie doesn't pre-exist on the request

    public DemoRequest(Request<DemoUser> delegate) {
      super(delegate);
    }

    public void setNewBCookieValue(String newBCookieValue) {
      this.newBCookieValue = newBCookieValue;
    }

    /**
     * @return the browser ID, which is the b-cookie value the browser sent with the request,
     * or if this request did not contain a b-cookie, the new b-cookie value that will be included
     * in the response to this request.
     */
    public String getBrowserId() {
      return newBCookieValue != null ? newBCookieValue : getBCookie();
    }

    public String getBCookie() {
      return getCookieValue(B_COOKIE_NAME);
    }

    public String getNewBCookieValue() {
      return newBCookieValue;
    }
  }

  static class DemoRequestFactory implements RequestFactory<DemoUser, DemoRequest> {
    @Override
    public DemoRequest createRequest(Request<DemoUser> delegate) {
      return new DemoRequest(delegate);
    }
  }

  /**
   * Our guice module
   */
  static class DemoModule extends JornadoModule<DemoRequest> {
    private DemoModule(Config config, Iterable<RouteHandler<DemoRequest>> routes) {
      super(config, routes);
    }

    @Override
    protected void configure() {
      super.configure();
      bind(UserService.class).to(DemoUserService.class);
      bind(RequestFactory.class).to(DemoRequestFactory.class);
    }

    @Override
    protected List<? extends Class> filters() {
      return Lists.newArrayList(BCookieFilter.class, AuthFilter.class);
    }
  }

  /**
   * Fixes up the request and response to support a "browser ID", a random string.
   * Adds the cookie if a new cookie is needed.
   * TODO: use signed cookies.
   */
  static class BCookieFilter implements Filter<DemoRequest> {
    public Response before(DemoRequest request, Class<? extends Handler<DemoRequest>> handlerClass) {
      if (request.getBCookie() == null) {
        request.setNewBCookieValue(Long.toString(new Random().nextLong()));
      }
      return null;
    }

    public void after(DemoRequest request, Response response, Class<? extends Handler<DemoRequest>> handlerClass) {
    }

    @Override
    public Response filter(DemoRequest request, Class<? extends Handler<DemoRequest>> handlerClass, FilterChain<DemoRequest> demoRequestFilterChain) {
      final String newBCookieValue = request.getNewBCookieValue();
      Response response = demoRequestFilterChain.doFilter(request);
      if (newBCookieValue != null) {
        response.addHeaderOp(new SetCookieHeaderOp(B_COOKIE_NAME, newBCookieValue, B_COOKIE_MAX_AGE));
      }
      return response;
    }
  }

  public static void main(String[] args) throws Exception {
    // parse command line
    final Config config = Config.create(args);

    // specify routes
    final Iterable<RouteHandler<DemoRequest>> routes = RouteHandler.iterable(
            new FixedRoute(Method.GET, "/"), HomeHandler.class,
            new FixedRoute(Method.GET, "/login"), LoginFormHandler.class,
            new FixedRoute(Method.POST, "/login"), LoginPostHandler.class,
            new FixedRoute(Method.GET, "/logout"), LogoutHandler.class,
            new FixedRoute(Method.GET, "/secret"), SecretHandler.class,
            new RegexRoute(Method.GET, "/person/([A-Za-z0-9]+)", "name"), PersonHandler.class);

    // create guice module
    final Module module = new DemoModule(config, routes);

    // create guice injector
    final Injector injector = Guice.createInjector(module);

    // get the jetty service and start it
    injector.getInstance(JettyService.class).startAndWait();
  }

  @Singleton
  static class HomeHandler implements DemoHandler {
    public Response handle(DemoRequest request) {
      DemoUser user = request.getUser();
      final String greeting;
      if (user != null) {
        greeting = user.getWebId();
      } else {
        greeting = "world";
      }
      return new OkPlainTextResponse("hello, " + greeting);
    }
  }

  @Singleton
  static class PersonHandler implements DemoHandler {
    public Response handle(DemoRequest request) {
      return new OkPlainTextResponse("Hello, " + request.getPathParameter("name"));
    }
  }

  static class LoginFormHandler implements DemoHandler {

    @Override
    public Response handle(DemoRequest request) {
      return new SimpleResponse(Status.OK, new StringBody("<form method='POST'><h1>Login</h1><input name='username'><input type='submit'/></form>", MediaType.TEXT_HTML_UTF8));
    }
  }

  /**
   * sets the login cookie to whatever the 'username' param is. and redirect to the 'target' url param.
   */
  static class LoginPostHandler implements DemoHandler {
    private final SecureCookieService secureCookieService;

    @Inject
    LoginPostHandler(SecureCookieService secureCookieService) {
      this.secureCookieService = secureCookieService;
    }

    @Override
    public Response handle(DemoRequest request) {
      final RedirectResponse response = new RedirectResponse(request.getParameter("target", "/"));
      final String userId = request.getParameter("username");
      response.addHeaderOp(new SetUserCookieHeaderOp(secureCookieService.create(userId)));

      return response;
    }
  }

  static class LogoutHandler implements DemoHandler {
    @Override
    public Response handle(DemoRequest request) {

      final RedirectResponse response = new RedirectResponse("/");
      final Cookie l = request.getCookie(Constants.LOGIN_COOKIE); // TODO: jornado should have a better facility for clearing login cookie

      if (l != null) {
        response.addHeaderOp(new ClearCookieOp(l));
      }

      return response;
    }
  }

  @RequiresLogin
  static class SecretHandler implements DemoHandler {
    @Override
    public Response handle(DemoRequest request) {
      return new OkPlainTextResponse("super secret");
    }
  }

  static class DemoUserService implements UserService<DemoUser> {
    public DemoUser load(String id) {
      // pretend we have a database
      return new DemoUser(id);
    }

    @Override
    public DemoUser create(Request request) {
      return new DemoUser("xyz");
    }
  }
}