package jornado;

public interface Handler<T extends Request> {
    Response handle(T request);
}
