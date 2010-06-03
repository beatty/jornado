package jornado;

public class SimpleResponse extends AbstractResponse {
  private final Status status;
  private final Body body;

  public SimpleResponse(Status status, Body body) {
    this.status = status;
    this.body = body;
  }

  public Status getStatus() {
    return status;
  }

  public Body getBody() {
    return body;
  }
}