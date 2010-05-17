package jornado;

public interface Body {
    Class<? extends RenderService> getRenderServiceClass();
    MediaType getMediaType();
}