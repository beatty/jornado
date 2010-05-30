package jornado;

/**
* @author john
*/
class InvalidSecureCookieFormatException extends RuntimeException {
  InvalidSecureCookieFormatException(String value) {
    super("bad cookie: " + value);
  }
}
