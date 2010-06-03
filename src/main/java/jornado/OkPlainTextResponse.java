package jornado;

public class OkPlainTextResponse extends AbstractResponse {
  private final String body;

  public OkPlainTextResponse(String body) {
    this.body = body;
  }

  public Status getStatus() {
    return Status.OK;
  }

  public Body getBody() {
    return new StringBody(body, MediaType.TEXT_PLAIN);
  }
}
