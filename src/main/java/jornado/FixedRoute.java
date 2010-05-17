package jornado;

/**
 * A fixed route.
 * @author john
 */
public class FixedRoute implements Route {
    private final Method method;
    private final String path;

    public FixedRoute(Method method, String path) {
        this.method = method;
        this.path = path;
    }

    public RouteData match(Request request) {
        if (method == null || method.equals(request.getMethod())) {
            if (path.equals(request.getPath())) {
                return new RouteData() {
                    public String getPathParameter(String name) {
                        return null;
                    }
                };
            }
        }

        return null;
    }
}
