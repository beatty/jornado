package jornado;

import java.util.Collections;
import java.util.List;

public class NotFoundResponse implements Response {
    public static final NotFoundResponse instance = new NotFoundResponse();
    public Status getStatus() {
        return Status.CODE_404;
    }

    public List<HeaderOp> getHeaderOps() {
        return Collections.emptyList();
    }

    public Body getBody() {
        // TODO
        return new StringBody("not found", MediaType.TEXT_PLAIN);
    }
}
