package jornado;

public class RedirectResponse extends AbstractResponse {
  public RedirectResponse(String url) {
    addHeaderOp(new AddHeaderOp("Location", url));
  }

  public Status getStatus() {
    return Status.CODE_302;
  }

  public Body getBody() {
    return null;
  }
}
