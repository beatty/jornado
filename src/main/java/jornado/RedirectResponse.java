package jornado;

import java.util.LinkedList;
import java.util.List;

public class RedirectResponse implements Response {
    // TODO: create base class that does this
    private final List<HeaderOp> headerOps = new LinkedList<HeaderOp>();

    public RedirectResponse(String url) {
        headerOps.add(new AddHeaderOp("Location", url));
    }

    public void addHeaderOp(HeaderOp headerOp) {
        headerOps.add(headerOp);
    }

    public Status getStatus() {
        return Status.CODE_302;
    }

    public List<HeaderOp> getHeaderOps() {
        return headerOps;
    }

    public Body getBody() {
        return null;
    }
}
