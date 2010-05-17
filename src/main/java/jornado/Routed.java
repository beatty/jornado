package jornado;

public class Routed {
    private final Route route;
    private final RouteData data;
    private final Handler handler;

    public Routed(Route route, RouteData data, Handler handler) {
        this.route = route;
        this.data = data;
        this.handler = handler;
    }

    public Route getRoute() {
        return route;
    }

    public RouteData getData() {
        return data;
    }

    public Handler getHandler() {
        return handler;
    }
}
