package demo;

import com.google.inject.*;
import jornado.*;

/**
 * Sample app
 */
public class DemoApp {
  // declare app-specific types we're going to use -- normally we'd add extra stuff here
  static interface DemoUser extends jornado.WebUser {}
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
      return new OkPlainTextResponse("hello, world");
    }
  }

  @Singleton
  static class PersonHandler implements DemoHandler {
    public Response handle(DemoRequest request) {
      return new OkPlainTextResponse("Hello, " + request.getPathParameter("name"));
    }
  }

  static class DemoUserService implements UserService<DemoUser> {
    public DemoUser load(String id) {
      throw new UnsupportedOperationException();
    }
  }
}