package jornado;

import java.util.regex.Pattern;

/**
 * A regex route for forms which matches on both GET and POST. Otherwise it
 * behaves like a normal RegexRoute.
 * @author john
 */
public class FormRegexRoute extends RegexRoute {
  public FormRegexRoute(Pattern pattern, String[] groupNames) {
    super(pattern, groupNames);
  }

  public FormRegexRoute(String pattern, String... groupNames) {
    this(Pattern.compile(pattern), groupNames);
  }

  @Override
  protected boolean methodMatches(Method requestMethod) {
    return requestMethod.equals(Method.GET) || requestMethod.equals(Method.POST);
  }
}
