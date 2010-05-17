package jornado;

import java.util.Collections;
import java.util.List;

public class OkPlainTextResponse implements Response {
    private final String body;

    public OkPlainTextResponse(String body) {
        this.body = body;
    }

    public Status getStatus() {
        return Status.OK;
    }

    public List<HeaderOp> getHeaderOps() {
        return Collections.emptyList();
    }

    public Body getBody() {
        return new StringBody(body, MediaType.TEXT_PLAIN);
    }
}
