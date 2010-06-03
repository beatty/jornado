package jornado;

/**
* @author john
*/
public class InvalidSecureCookieFormatException extends RuntimeException {
  InvalidSecureCookieFormatException(String value) {
    super("bad cookie: " + value);
  }
}
