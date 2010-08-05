package jornado;

public class OkHtmlResponse extends AbstractResponse {
  private final String body;

  public OkHtmlResponse(String body) {
    this.body = body;
  }

  public Status getStatus() {
    return Status.OK;
  }

  public Body getBody() {
    return new StringBody(body, MediaType.TEXT_HTML_UTF8);
  }
}
