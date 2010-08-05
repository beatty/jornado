package jornado;

public class NotFoundResponse extends AbstractResponse {
  private final String body;
  
  public NotFoundResponse(String body) {
    super();
    this.body = body;
  }

  public NotFoundResponse() {
    this("not found");
  }
  
  public Status getStatus() {
    return Status.CODE_404;
  }

  public Body getBody() {
    return new StringBody(body, MediaType.TEXT_HTML_UTF8);
  }
}
