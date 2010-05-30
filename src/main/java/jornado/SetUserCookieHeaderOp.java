package jornado;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class SetUserCookieHeaderOp implements HeaderOp {
    private final String userId;

    public SetUserCookieHeaderOp(String userId) {
        this.userId = userId;
    }

    public void execute(HttpServletResponse response) {
        final Cookie cookie = new Cookie(Constants.LOGIN_COOKIE, userId);
        cookie.setPath("/");
        // TODO: expires, domain, etc.
        response.addCookie(cookie);
    }
}
