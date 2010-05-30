package jornado;

import org.joptparse.Option;
import org.joptparse.OptionParser;

/**
 * WARNING: applications MUST use their own cookieKey. A default one is provided just for demo purposes. TODO: should gen one at runtime for demos.
 */
public class Config {
  @Option(names = {"-x"})
  public boolean debug;

  @Option(names = {"--port"}, defarg = "8000")
  public int port;

  @Option(names = {"--publicUrl"}, defarg = "localhost:8000")
  public String publicUrl;

  @Option(names = {"--js"})
  public String jsDir;

  @Option(names = {"--static"})
  public String staticDir;

  @Option(names = {"--cookieKey"}, defarg="32oETzKXQAGaYdkL5gEmGeJJFuYh7EQnp2XdTP1o/Vo=")
  public String cookieKey;

  public int getPort() {
    return port;
  }

  public String getPublicUrl() {
    return publicUrl;
  }

  public boolean isDebug() {
    return debug;
  }

  public String getJsDir() {
    return jsDir;
  }

  public String getStaticDir() {
    return staticDir;
  }

  public String getCookieKey() {
    return cookieKey;
  }

  public static Config create(String[] args) {
    final Config config = new Config();
    final OptionParser parser = new OptionParser(config);
    parser.parse(args);
    return config;
  }
}