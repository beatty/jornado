package jornado;

public interface Body {
    Class<RenderService> getRenderServiceClass();
    MediaType getMediaType();
}