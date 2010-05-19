package jornado;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets a cookie in the current domain with a path of '/'
 * @author john
 */
public class SetCookieHeaderOp implements HeaderOp {
  private final String name;
  private final String value;
  private final int maxAge;

  public SetCookieHeaderOp(String name, String value, int maxAge) {
    this.name = name;
    this.value = value;
    this.maxAge = maxAge;
  }

  @Override
  public void execute(HttpServletResponse response) {
    final Cookie c = new Cookie(name, value);
    c.setMaxAge(maxAge);
    c.setPath("/");
    response.addCookie(c);
  }
}
