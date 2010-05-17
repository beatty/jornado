package jornado;

public class Status {
    public static final Status OK = new Status(200);
    public static final Status CODE_302 = new Status(302);
    public static final Status CODE_404 = new Status(404);
    public static final Status CODE_503 = new Status(503);
    
    private final int code;
    private final String reasonPhrase;

    public Status(int code, String reasonPhrase) {
        this.code = code;
        this.reasonPhrase = reasonPhrase;
    }

    public Status(int code) {
        this(code, null);
    }

    public int getCode() {
        return code;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }
}
