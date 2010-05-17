package jornado;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Writer;

/**
 * @author john
 */
public class JsonRenderService implements RenderService {
  // TODO is this threadsafe?
  private static final ObjectMapper mapper = new ObjectMapper();

  @Override
  public void write(Writer writer, Body body) throws IOException {
    final JsonResponse.JsonBody jsonBody = (JsonResponse.JsonBody) body;
    try {
      mapper.writeValue(writer, jsonBody.getObject());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}