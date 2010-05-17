package jornado;

import java.util.Collections;
import java.util.List;

public class ErrorResponse implements Response {
    private final String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public Status getStatus() {
        return Status.CODE_503;
    }

    public List<HeaderOp> getHeaderOps() {
        return Collections.emptyList();
    }

    public Body getBody() {
        return new StringBody("oops! " + message, MediaType.TEXT_PLAIN);
    }
}
