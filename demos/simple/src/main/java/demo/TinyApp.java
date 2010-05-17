package demo;

import com.google.common.collect.Lists;
import com.google.inject.*;
import jornado.*;
import org.joptparse.OptionParser;

/**
 * Sample app
 */
public class TinyApp {
    private final Module module;

    /**
     * Our jornado guice module
     */
    static class Module extends JornadoModule {
        protected Module(final Config config) {
            super(config);
        }

        protected Iterable<RouteHandler<Request>> createRoutes() {
            return Lists.newArrayList(
                    new RouteHandler<Request>(new FixedRoute(Method.GET, "/"), HomeHandler.class),
                    new RouteHandler<Request>(new RegexRoute(Method.GET, "/person/([A-Za-z0-9]+)", "name"), PersonHandler.class));
        }

        @Override
        protected void configure() {
            super.configure();
            bind(UserService.class).to(TinyUserService.class);

            // TODO: tried to bind using bounded wildcard generics, but no go. seems impossible with guice. blergh.
            //bind(new TypeLiteral<UserService<? extends WebUser>>(){}).to(TinyUserService.class);
            //bind(TypeLiteral.get(Types.newParameterizedType(UserService.class, Types.subtypeOf(WebUser.class)))).to(TinyUserService.class);            
        }
    }

    public TinyApp(final Config config) {
        module = new Module(config);
    }

    public static void main(String[] args) throws Exception {
        final Config config = createConfig(args);
        TinyApp app = new TinyApp(config);
        final Injector injector = Guice.createInjector(app.module); // initialize the object tree with Guice
        injector.getInstance(JettyService.class).startAndWait(); // get the jetty service and start it
    }

    private static Config createConfig(String[] args) {
        final Config config = new Config();
        final OptionParser parser = new OptionParser(config);
        parser.parse(args);
        return config;
    }

    static class HomeHandler implements Handler<Request> {
        public Response handle(Request request) {
            return new OkPlainTextResponse("hello, world");
        }
    }

    static class PersonHandler implements Handler<Request> {
        public Response handle(Request request) {
            return new OkPlainTextResponse("Hello, " + request.getPathParameter("name"));
        }
    }

    static class TinyUserService implements UserService<TinyWebUser> {
        public TinyWebUser load(String id) {
            return new TinyWebUser();
        }
    }

    static class TinyWebUser implements WebUser {
        public String getWebId() {
            return "123";
        }
    }
}