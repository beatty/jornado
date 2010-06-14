package jornado;

public interface Route {
    RouteData match(Request<? extends WebUser> request);    
}
