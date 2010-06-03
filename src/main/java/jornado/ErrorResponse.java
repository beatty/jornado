package jornado;

public class ErrorResponse extends AbstractResponse {
  private final String reasonPhrase;

  public ErrorResponse(String reasonPhrase) {
    this.reasonPhrase = reasonPhrase;
  }

  public Status getStatus() {
    return new Status(503, reasonPhrase);
  }

  public Body getBody() {
    // TODO
    return new StringBody("oops! " + reasonPhrase, MediaType.TEXT_PLAIN);
  }
}
