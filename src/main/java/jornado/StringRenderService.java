package jornado;

import com.google.common.base.Charsets;

public class StringRenderService extends AbstractBufferingRenderService {
    @Override
    public byte[] renderFully(Body body) {
        return ((StringBody) body).getBody().getBytes(Charsets.UTF_8);
    }
}