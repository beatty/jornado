package jornado;

import java.io.IOException;
import java.io.Writer;

/**
 * Renders an object to a byte[]. Optionally supports incremental renderers.
 * TODO: consider splitting interfaces rather than this ugly design
 */
public interface RenderService {
  void write(Writer writer, Body body) throws IOException;
}