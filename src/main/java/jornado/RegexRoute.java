package jornado;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexRoute implements Route {
    private final Method[] methods;
    private final Pattern pattern;
    private final String[] groupNames;

    public RegexRoute(Pattern pattern, String[] groupNames) {
      if (pattern == null) throw new IllegalArgumentException();
      
      this.methods = null;
      this.pattern = pattern;
      this.groupNames = groupNames;
  }

    public RegexRoute(Method method, Pattern pattern, String[] groupNames) {
        if (method == null) throw new IllegalArgumentException();
        if (pattern == null) throw new IllegalArgumentException();
        
        this.methods = new Method[]{method};
        this.pattern = pattern;
        this.groupNames = groupNames;
    }

    public RegexRoute(Method[] methods, Pattern pattern, String[] groupNames) {
      if (pattern == null) throw new IllegalArgumentException();

      this.methods = methods;
      this.pattern = pattern;
      this.groupNames = groupNames;
  }

    /**
     * Convenience constructor
     * @param method
     * @param pattern
     * @param groupNames
     */
    public RegexRoute(Method method, String pattern, String... groupNames) {
        this(method, Pattern.compile(pattern), groupNames);
    }

    /**
     * Convenience constructor
     * @param method
     * @param pattern
     * @param groupNames
     */
    public RegexRoute(Method[] methods, String pattern, String... groupNames) {
        this(methods, Pattern.compile(pattern), groupNames);
    }

    public RouteData match(Request<? extends WebUser> request) {
        if (methodMatches(request.getMethod())) {
            final Matcher matcher = pattern.matcher(request.getPath());
            if (matcher.matches()) {
                return new RegexRouteMatch(matcher, groupNames);
            }
        }
        return null;
    }
    
    protected boolean methodMatches(Method requestMethod) {
      if (methods == null) return true;
      
      for (Method routeMethod : methods) {
        if (routeMethod.equals(requestMethod)) {
          return true;
        }
      }
      
      return false;
    }

    static class RegexRouteMatch implements RouteData {
        private final Matcher matcher;
        private final String[] groupNames;

        RegexRouteMatch(Matcher matcher, String[] groupNames) {
            if (matcher.groupCount() != groupNames.length) {
                throw new IllegalArgumentException(String.format("mismatch group count (%s) and group names count (%s)", matcher.groupCount(), groupNames.length));
            }

            this.matcher = matcher;
            this.groupNames = groupNames;
        }

        public String getPathParameter(String name) {
            if (name == null) throw new IllegalArgumentException("name must be non-null");
            for (int i = 0; i < groupNames.length; i++) {
                if (name.equals(groupNames[i])) {
                    return matcher.group(i+1); // note the offset by one
                }
            }
            throw new IllegalArgumentException(String.format("name %s not in groupNames", name));
        }
    }
}
