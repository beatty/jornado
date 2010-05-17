package jornado;

import com.google.common.base.Charsets;
import com.google.inject.Injector;

public class Jornado {
    public static final Jornado global = new Jornado();

    private Injector injector;
    private RequestAdaptor requestAdaptor;

    public void setRequestAdaptor(RequestAdaptor requestAdaptor) {
        this.requestAdaptor = requestAdaptor;
    }

    public RequestAdaptor getRequestAdaptor() {
        return requestAdaptor;
    }

    public static byte[] stringToUtf8Bytes(String value) {
        return value.getBytes(Charsets.UTF_8);
    }
}

