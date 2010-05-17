package jornado;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 * The request. Subset/superset of HttpServletRequest with crap removed and some extra features, like
 * support for users.
 * @param <U>
 */
public interface Request<U extends WebUser> {
    Method getMethod();
    String getPath();
    String getReconstructedUrl();
    Cookie getCookie(String name);
    String getCookieValue(String name);
    String getHeader(String name);
    Long getLongParameter(String name);
    long getLongParameter(String name, long defaultValue);
    String getParameter(String name);
    String getParameter(String name, String defaultValue);
    boolean isParameterSet(String name);
    Integer getIntParameter(String name);
    int getIntParameter(String name, int defaultValue);
    String getPathParameter(String name);
    Long getLongPathParameter(String name);
    HttpSession getSession();
    U getUser();
    String getReferer();
    boolean isLoggedIn();
    String getXsrfCookie();
    String getRequestId();
}
