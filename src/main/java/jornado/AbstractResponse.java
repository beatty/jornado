package jornado;

import java.util.LinkedList;
import java.util.List;

/**
 * A base class for creating response classes.
 *
 * @author john
 */
public abstract class AbstractResponse implements Response {
  private final List<HeaderOp> headerOps = new LinkedList<HeaderOp>();

  @Override
  public void addHeaderOp(HeaderOp headerOp) {
    headerOps.add(headerOp);
  }

  @Override
  public Iterable<HeaderOp> getHeaderOps() {
    return headerOps;
  }

  @Override
  public Body getBody() {
    return null;
  }
}
