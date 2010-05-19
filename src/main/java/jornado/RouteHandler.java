package jornado;

import java.util.ArrayList;

/**
 * 
 */
public class RouteHandler<R extends Request> {
    private final Route route;
    private final Class<? extends Handler<R>> handlerClass;

    public RouteHandler(Route route, Class<? extends Handler<R>> handlerClass) {
        this.route = route;
        this.handlerClass = handlerClass;
    }

    public Route getRoute() {
        return route;
    }

    public Class<? extends Handler<R>> getHandlerClass() {
        return handlerClass;
    }

    public static <R extends Request> Iterable<RouteHandler<R>> iterable(Object... objects) {
        if (objects.length % 2 != 0) throw new IllegalArgumentException("array must have even number of elements");
        final ArrayList<RouteHandler<R>> routes = new ArrayList<RouteHandler<R>>(objects.length/2);
        for (int i=0; i<objects.length; i+=2) {
          final Route route = (Route) objects[i];
          final Class<? extends Handler<R>> cls = (Class<? extends Handler<R>>) objects[i+1];
          routes.add(new RouteHandler<R>(route, cls));
        }
        return routes;
    }
}
