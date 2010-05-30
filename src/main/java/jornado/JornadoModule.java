package jornado;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

import java.util.Collections;

/**
 * A guice module that contains the configuration for jornado
 */
public abstract class JornadoModule<R extends Request> extends AbstractModule {
  private final Config config;
  private final Iterable<RouteHandler<R>> routes;

  protected JornadoModule(final Config config, Iterable<RouteHandler<R>> routes) {
    this.config = config;
    this.routes = routes;
  }

  protected Class getRequestClass() {
    return Request.class;
  }

  /**
   * @deprecated use version where you pass in routes instead
   */
  @Deprecated
  protected JornadoModule(final Config config) {
    this.config = config;
    routes = createRoutes();
  }

  /**
   * @return routes
   * @deprecated pass in on constructor instead
   */
  @Deprecated
  protected Iterable<RouteHandler<R>> createRoutes() {
    return Collections.emptyList();
  }

  @Override
  protected void configure() {
    bind(Config.class).toInstance(config);
    bindIterable("routes", routes);

    bind(TypeLiteral.get(String.class)).annotatedWith(Names.named("cookieKey")).toInstance(config.getCookieKey());

    // inject-enable the handlers
    Matchers.subclassesOf(Handler.class);

    bind(SecureCookieService.class);

    // set up the timing intercepters
    final TimingInterceptor timingInterceptor = new TimingInterceptor();
    bindInterceptor(Matchers.any(), Matchers.annotatedWith(Timed.class), timingInterceptor);
    bindInterceptor(Matchers.annotatedWith(Timed.class), Matchers.any(), timingInterceptor);
    bindInterceptor(Matchers.subclassesOf(Handler.class), Matchers.any(), timingInterceptor);
  }

  protected void bindIterable(String name, Iterable<RouteHandler<R>> value) {
    bind(TypeLiteral.get(Iterable.class)).annotatedWith(Names.named(name)).toInstance(value);
  }
}