package jornado;

import java.util.List;

public class SimpleResponse implements Response {
    private final Status status;
    private List<HeaderOp> headerOps;
    private final Body body;

    public SimpleResponse(Status status, List<HeaderOp> headerOps, Body body) {
        this.status = status;
        this.headerOps = headerOps;
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    public List<HeaderOp> getHeaderOps() {
        return headerOps;
    }

    public Body getBody() {
        return body;
    }
}