package jornado;

import com.google.common.base.Charsets;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

/**
* @author john
*/
public class JsonRenderService extends AbstractBufferingRenderService {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public byte[] renderFully(Body body) {
        final JsonResponse.JsonBody jsonBody = (JsonResponse.JsonBody) body;
        try {
            return mapper.writeValueAsString(jsonBody.getObject()).getBytes(Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
