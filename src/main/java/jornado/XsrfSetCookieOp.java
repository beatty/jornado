package jornado;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets the xsrf cookie
 * @author john
 */
public class XsrfSetCookieOp implements HeaderOp {
    private final String token;

    public XsrfSetCookieOp(String token) {
        this.token = token;
    }

    public void execute(HttpServletResponse response) {
        final Cookie cookie = new Cookie(Constants.XSRF_COOKIE, token);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}