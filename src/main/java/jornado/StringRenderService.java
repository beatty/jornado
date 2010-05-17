package jornado;

import java.io.IOException;
import java.io.Writer;

public class StringRenderService implements RenderService {
  @Override
  public void write(Writer writer, Body body) throws IOException {
    StringBody newBody = (StringBody) body;
    writer.write(newBody.getString());
  }
}