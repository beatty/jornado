package jornado;

import javax.servlet.http.HttpServletResponse;

/**
 * A header operation. A header operation is anything that manipulates the HTTP response before
 * the body bytes start going out.
 */
public interface HeaderOp {
    void execute(HttpServletResponse response);
}
