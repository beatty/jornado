package demo;

import com.google.inject.*;
import jornado.*;

import javax.servlet.http.Cookie;

/**
 * Sample app
 */
public class DemoApp {
  // declare app-specific types we're going to use -- normally we'd add extra stuff here
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
  static interface DemoHandler extends jornado.Handler<DemoRequest> {}

  // applications can define their own request classes like so
  static class DemoRequest extends RequestWrapper<DemoUser> {
    public DemoRequest(Request<DemoUser> delegate) {
      super(delegate);
    }

    public String getBrowserIdCookie() {
      return getCookieValue("b");
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
  }

  public static void main(String[] args) throws Exception {
    // parse command line
    final Config config = Config.create(args);

    // specify routes
    final Iterable<RouteHandler<DemoRequest>> routes = RouteHandler.iterable(
      new FixedRoute(Method.GET, "/"), HomeHandler.class,
      new FixedRoute(Method.GET, "/login"), LoginHandler.class,
      new FixedRoute(Method.GET, "/logout"), LogoutHandler.class,
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

  /**
   * sets the login cookie to whatever the 'username' param is. and redirect to the 'target' url param.
   */
  static class LoginHandler implements DemoHandler {
    private final SecureCookieService secureCookieService;

    @Inject
    LoginHandler(SecureCookieService secureCookieService) {
      this.secureCookieService = secureCookieService;
    }

    @Override
    public Response handle(DemoRequest request) {
      final RedirectResponse response = new RedirectResponse(request.getParameter("target", "/"));
      final String userId = request.getParameter("username", "john");
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

  static class DemoUserService implements UserService<DemoUser> {
    public DemoUser load(String id) {
      // pretend we have a database
      return new DemoUser(id);
    }
  }
}