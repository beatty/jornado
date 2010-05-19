package jornado;

import org.joptparse.Option;
import org.joptparse.OptionParser;

public class Config {
    @Option(names={"-x"})
    public boolean debug;

    @Option(names={"--port"}, defarg="8000")
    public int port;

    @Option(names={"--publicUrl"}, defarg="localhost:8000")
    public String publicUrl;

    @Option(names={"--js"})
    public String jsDir;

    @Option(names={"--static"})
    public String staticDir;

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

    public static Config create(String[] args) {
      final Config config = new Config();
      final OptionParser parser = new OptionParser(config);
      parser.parse(args);
      return config;
    }
}