package jornado;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * @author john
 */
public class SecureCookieServiceTest {
  private static final String TEST_KEY = "32oETzKXQAGaYdkL5gEmGeJJFuYh7EQnp2XdTP1o/Vo=";
  private static final String TEST_KEY_2 = "02oETzKXQAGaYdkL5gEmGeJJFuYh7EQnp2XdTP1o/Vo=";

  private final SecureCookieService svc = new SecureCookieService(TEST_KEY);
  private final SecureCookieService svc2 = new SecureCookieService(TEST_KEY_2);

  @Test
  public void test() {
    final String value = "john";
    final String value2 = svc.extract(svc.create(value), Integer.MAX_VALUE);
    assertEquals(value, value2);
  }

  @Test
  public void testUtf8() {
    final String value = "???";
    final String value2 = svc.extract(svc.create(value), Integer.MAX_VALUE);
    assertEquals(value, value2);
  }

  @Test(expected = FailedSignatureValidation.class)
  public void testChangeKey() {
    svc2.extract(svc.create("foo"), Integer.MAX_VALUE);
  }

  @Test(expected = FailedSignatureValidation.class)
  public void testTamper() {
    final String value = "john";
    final String cookie = svc.create(value);
    final String cookieTampered = cookie.substring(1);
    svc.extract(cookieTampered, Integer.MAX_VALUE);
  }


  @Test(expected = SecureCookieTimeoutException.class)
  public void testTimeout() {
    svc.extract(svc.create("foo"), -1);
  }
}
