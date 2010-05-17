package jornado;

// TODO: copy stuff from javax.ws.rs.core
// TODO: cheating on the parameters -- only supporting charset param right now
// TODO: cache string on create
public class MediaType {
    public static final MediaType TEXT_HTML_UTF8 = new MediaType("text", "html", "UTF-8");
    public static final MediaType TEXT_PLAIN = new MediaType("text", "plain", "UTF-8");
    public static final MediaType APPLICATION_JSON = new MediaType("application", "json", null);

    private final String type;
    private final String subtype;
    private final String charset;

    public MediaType(String type, String subtype, String charset) {
        this.type = type;
        this.subtype = subtype;
        this.charset = charset;
    }

    public MediaType(String type, String subtype) {
        this(type, subtype, null);
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public String getCharset() {
        return charset;
    }

    public String toString() {
        return type + (subtype != null ? "/" + subtype : "") + (charset != null ? ";charset=" + charset : "");
    }
}