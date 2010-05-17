package jornado;

import java.util.Collections;
import java.util.List;

public class JsonResponse implements Response {
    private final Object object;

    public JsonResponse(Object object) {
        this.object = object;
    }

    public Status getStatus() {
        return Status.OK;
    }

    public List<HeaderOp> getHeaderOps() {
        return Collections.emptyList();
    }

    public Body getBody() {
        return new JsonBody(object);
    }

    public static class JsonBody implements Body {
        private final Object object;

        public JsonBody(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        public Class<JsonRenderService> getRenderServiceClass() {
            return JsonRenderService.class;
        }

        public MediaType getMediaType() {
            return MediaType.APPLICATION_JSON;
        }
    }
}