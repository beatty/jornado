package jornado;

/**
 * 
 */
public class RouteHandler<T extends Request> {
    private final Route route;
    private final Class<? extends Handler<T>> handlerClass;

    public RouteHandler(Route route, Class<? extends Handler<T>> handlerClass) {
        this.route = route;
        this.handlerClass = handlerClass;
    }

    public Route getRoute() {
        return route;
    }

    public Class<? extends Handler<T>> getHandlerClass() {
        return handlerClass;
    }
}
