package jornado;

public interface Route {
    RouteData match(Request request);    
}
