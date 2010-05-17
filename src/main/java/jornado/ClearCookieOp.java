package jornado;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class ClearCookieOp implements HeaderOp {
    private final Cookie cookie;

    public ClearCookieOp(Cookie cookie) {
        this.cookie = cookie;
    }

    public void execute(HttpServletResponse response) {
        // the horror!
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
