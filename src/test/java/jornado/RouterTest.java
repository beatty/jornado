package jornado;

import com.google.common.collect.Lists;
import org.junit.Test;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;

/**
 *
 * @author john
 */
import java.util.Collection;

import static org.junit.Assert.*;

public class RouterTest {

  @SuppressWarnings("unchecked")
  @Test
  public void testRoute() throws Exception {
    final Router router = new Router(Lists.newArrayList(new RouteHandler(new FixedRoute(Method.GET, "/"), RouterTest.class)));

    RouteHandlerData hit0 = router.route(new MockRequest(Method.GET, "/"));
    assertNotNull(hit0);
    assert(RouterTest.class.equals(hit0.getHandlerClass()));
  }

  @Test
  public void testFixedRoute() {
    FixedRoute route = new FixedRoute(Method.GET, "/");

    final Request hit0 = new MockRequest(Method.GET, "/");
    assertNotNull(route.match(hit0));

    final Request miss0 = new MockRequest(Method.POST, "/");
    assertNull(route.match(miss0));

    final Request miss1 = new MockRequest(Method.GET, "/foo");
    assertNull(route.match(miss1));
  }

  @Test
  public void testRegexRoute() {
    final Route route = new RegexRoute(Method.GET, "/person/([A-Za-z0-9]+)", "name");

    final Request hit0 = new MockRequest(Method.GET, "/person/john");
    final RouteData data = route.match(hit0);
    assertNotNull(data);
    assertEquals("john", data.getPathParameter("name"));

    final Request miss0 = new MockRequest(Method.POST, "/");
    assertNull(route.match(miss0));

    final Request miss1 = new MockRequest(Method.GET, "/person/john/foo");
    assertNull(route.match(miss1));
  }

  static class MockRequest implements Request {
    private final Method method;
    private final String path;

    MockRequest(Method method, String path) {
      this.method = method;
      this.path = path;
    }

    @Override
    public Method getMethod() {
      return method;
    }

    @Override
    public String getPath() {
      return path;
    }

    @Override
    public String getReconstructedUrl() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Cookie getCookie(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getCookieValue(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getHeader(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Collection<String> getListParameter(String name) {
      return null;
    }

    @Override
    public Long getLongParameter(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getLongParameter(String name, long defaultValue) {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getParameter(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getParameter(String name, String defaultValue) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean hasParameter(String name) {
      return false; // TODO
    }

    @Override
    public boolean isParameterSet(String name) {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Integer getIntParameter(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getIntParameter(String name, int defaultValue) {
      return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPathParameter(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Long getLongPathParameter(String name) {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public HttpSession getSession() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public WebUser getUser() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getReferer() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isLoggedIn() {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getXsrfCookie() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getRequestId() {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isLoginCookieInvalid() {
      return false; // TODO
    }
  }
}