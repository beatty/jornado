package jornado;

import javax.servlet.http.HttpServletResponse;

public class AddHeaderOp implements HeaderOp {
    private final String name;
    private final String value;

    public AddHeaderOp(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public void execute(HttpServletResponse response) {
        response.setHeader(name, value);
    }
}
