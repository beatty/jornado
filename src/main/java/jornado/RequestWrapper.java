package jornado;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpSession;
import java.util.Collection;

/**
 * Request wrapper to allow applications to easily create their own request classes
 * @author john
 */
public class RequestWrapper<U extends WebUser> implements Request<U> {
  private Request<U> delegate;

  public RequestWrapper(Request<U> delegate) {
    this.delegate = delegate;
  }

  public Method getMethod() {
    return delegate.getMethod();
  }

  public String getPath() {
    return delegate.getPath();
  }

  public String getReconstructedUrl() {
    return delegate.getReconstructedUrl();
  }

  public Cookie getCookie(String name) {
    return delegate.getCookie(name);
  }

  public String getCookieValue(String name) {
    return delegate.getCookieValue(name);
  }

  public String getHeader(String name) {
    return delegate.getHeader(name);
  }

  public Collection<String> getListParameter(String name) {
    return delegate.getListParameter(name);
  }

  public int getBase62Parameter(String name) {
    return delegate.getBase62Parameter(name);
  }

  public Long getLongParameter(String name) {
    return delegate.getLongParameter(name);
  }

  public long getLongParameter(String name, long defaultValue) {
    return delegate.getLongParameter(name, defaultValue);
  }

  public String getParameter(String name) {
    return delegate.getParameter(name);
  }

  public String getParameter(String name, String defaultValue) {
    return delegate.getParameter(name, defaultValue);
  }

  @Override
  public boolean hasParameter(String name) {
    return delegate.hasParameter(name);
  }

  public boolean isParameterSet(String name) {
    return delegate.isParameterSet(name);
  }

  public Integer getIntParameter(String name) {
    return delegate.getIntParameter(name);
  }

  public int getIntParameter(String name, int defaultValue) {
    return delegate.getIntParameter(name, defaultValue);
  }

  public String getPathParameter(String name) {
    return delegate.getPathParameter(name);
  }

  public Long getLongPathParameter(String name) {
    return delegate.getLongPathParameter(name);
  }

  public HttpSession getSession() {
    return delegate.getSession();
  }

  public U getUser() {
    return delegate.getUser();
  }

  public String getReferer() {
    return delegate.getReferer();
  }

  public boolean isLoggedIn() {
    return delegate.isLoggedIn();
  }

  public String getXsrfCookie() {
    return delegate.getXsrfCookie();
  }

  public String getRequestId() {
    return delegate.getRequestId();
  }

  @Override
  public boolean isLoginCookieInvalid() {
    return delegate.isLoginCookieInvalid();
  }
}
