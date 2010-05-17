package jornado;

public class StringBody implements Body {
    private final String body;
    private final MediaType mediaType;

    public Class getRenderServiceClass() {
        return StringRenderService.class;
    }

    public StringBody(String body, MediaType mediaType) {
        this.body = body;
        this.mediaType = mediaType;
    }

    public String getBody() {
        return body;
    }

    public MediaType getMediaType() {
        return mediaType;
    }
}
