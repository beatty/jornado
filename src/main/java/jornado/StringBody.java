package jornado;

public class StringBody implements Body {
  private final String string;
  private final MediaType mediaType;

  public Class<StringRenderService> getRenderServiceClass() {
    return StringRenderService.class;
  }

  public StringBody(String string, MediaType mediaType) {
    this.string = string;
    this.mediaType = mediaType;
  }

  public String getString() {
    return string;
  }

  public MediaType getMediaType() {
    return mediaType;
  }
}
