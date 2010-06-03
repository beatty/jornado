package jornado;

public interface Response {
  Iterable<HeaderOp> getHeaderOps();
  Status getStatus();
  Body getBody();
  void addHeaderOp(HeaderOp headerOp);
}