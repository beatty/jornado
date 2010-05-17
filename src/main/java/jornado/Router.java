package jornado;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import java.util.List;

/**
 * Resolves request to route handlers + associated data
 */
public class Router<R extends Request> {
    private final Iterable<? extends RouteHandler<R>> routeHandlers;

    @Inject
    @SuppressWarnings("unchecked")
    public Router(@Named("routes") Iterable routeHandlers) {
        this.routeHandlers = routeHandlers;
    }

    /**
     * Given a request, returns the first matching RouteHandlerData. Returns null if
     * there are no matching handlers.
     *
     * @param request the request
     * @return the first matching route+data. may return null.
     */
    public RouteHandlerData<R> route(Request request) {
        for (RouteHandler<R> routeHandler : routeHandlers) {
            final RouteData routeData = routeHandler.getRoute().match(request);
            if (routeData != null) {
                return new RouteHandlerData<R>(routeHandler.getHandlerClass(), routeData);
            }
        }
        return null;
    }
}
