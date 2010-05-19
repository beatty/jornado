package jornado;

public interface Handler<R extends Request> {
    Response handle(R request);
}
