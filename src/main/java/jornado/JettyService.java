package jornado;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.eclipse.jetty.server.NCSARequestLog;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.RequestLogHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

// TODO: configure jetty to produce access and error logs
// TODO: if a file isn't in static/js context, it looks in the original servlet context?

/**
 * The jetty-based web server wrapped up in a guava service
 */
@Singleton
public class JettyService extends AbstractIdleService {
    private final Server server;

    @Inject
    public JettyService(final JornadoServlet jornadoServlet, final Config config) {
        server = new Server(config.getPort());

        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        server.setHandler(contexts);

        contexts.addHandler(createRequestLogHandler());

        final ServletContextHandler mainHandler = new ServletContextHandler(contexts, "/", false, false);
        mainHandler.addServlet(new ServletHolder(jornadoServlet), "/");

        if (config.isDebug() && config.getStaticDir() != null) {
            ContextHandler staticContextHandler = contexts.addContext("/static", config.getStaticDir());
            staticContextHandler.setHandler(new ResourceHandler());
        }

        if (config.isDebug() && config.getJsDir() != null) {
            ContextHandler jsContextHandler = contexts.addContext("/js", config.getJsDir());
            // TODO: look into capabilities of resourcehandler -- i think it does what I want (gzip, etc)
            jsContextHandler.setHandler(new ResourceHandler());
        }
    }

    private org.eclipse.jetty.server.Handler createRequestLogHandler() {
        RequestLogHandler handler = new RequestLogHandler();
        handler.setRequestLog(new NCSARequestLog());
        return handler;
    }

    @Override
    protected void shutDown() throws Exception {
        server.stop();
    }

    @Override
    protected void startUp() throws Exception {
        server.start();
    }
}