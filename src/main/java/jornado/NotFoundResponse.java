package jornado;

public class NotFoundResponse extends AbstractResponse {
  public Status getStatus() {
    return Status.CODE_404;
  }

  public Body getBody() {
    // TODO
    return new StringBody("not found", MediaType.TEXT_PLAIN);
  }
}
