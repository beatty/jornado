package jornado;

public enum Method {
    GET, POST, PUT, DELETE, HEAD, OPTIONS, TRACE, CONNECT;

    public static Method fromString(String method) {
        if (method.equals("GET")) {
            return GET;
        } else if (method.equals("POST")) {
            return POST;
        } else if (method.equals("PUT")) {
            return PUT;
        } else if (method.equals("DELETE")) {
            return DELETE;
        } else if (method.equals("HEAD")) {
            return HEAD;
        } else if (method.equals("OPTIONS")) {
            return OPTIONS;
        } else if (method.equals("TRACE")) {
            return TRACE;
        } else if (method.equals("CONNECT")) {
            return CONNECT;
        } else {
            throw new IllegalArgumentException("unsupported method: " + method);
        }
    }
}