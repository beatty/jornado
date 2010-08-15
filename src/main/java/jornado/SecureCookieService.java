package jornado;

import com.google.common.base.Joiner;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import org.apache.commons.codec.binary.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Creates timestamped and signed cookies, and validates and reads them. The client specifies a string value.
 *
 * The cookie format is: base64(utf8-encoded string value) "|" timestamp (seconds since epoch) "|" base64(signature(value,timestamp))
 *
 * @author john
 */
public class SecureCookieService {
  private static final String HMAC_ALGORITHM = "HmacSHA256";
  private static final String VALUE_ENCODING = "UTF-8";

  private final SecretKeySpec signingKey;

  @Inject
  public SecureCookieService(@Named("cookieKey") String key) {
    signingKey = new SecretKeySpec(key.getBytes(), HMAC_ALGORITHM);
  }

  /**
   * Creates a tamper-proof, timestamped cookie with the specified value.
   * @param value A string. Any string is allowed -- the value will be UTF-8 encoded in the cookie.
   * @return the cookie string
   */
  public String create(String value) {
    return create(utf8Bytes(value));
  }

  /**
   * Creates a tamper-proof, timestamped cookie with the specified value.
   * @param value A string. Any string is allowed -- the value will be UTF-8 encoded in the cookie.
   * @return the cookie string
   */
  public String create(byte[] value) {
    final byte[] timestampBytes = currentTimeBytes();
    return Joiner.on('|').join(str(b64(value)), str(timestampBytes), str(b64(sign(value, timestampBytes))));
  }

  /**
   * Extracts the value of the cookie.
   *
   * @param cookie The cookie string
   * @param timeoutSeconds The maximum allowable age of the cookie, specified in seconds.
   * @return the original value
   * @throws SecureCookieTimeoutException if the cookie timestamp is older then than specified timeout value.
   * @throws FailedSignatureValidationException if the cookie failed signature validation. Note that the application should
   * occasionally expect this when keys are rotated.
   */
  public byte[] extractBytes(String cookie, int timeoutSeconds) {
    final String[] parts = cookie.split("\\|");

    if (parts.length != 3) {
      throw new InvalidSecureCookieFormatException(cookie);
    }

    final byte[] givenValue = unb64(asciiBytes(parts[0]));
    final byte[] givenTimestamp = asciiBytes(parts[1]);
    final byte[] givenSignature = unb64(asciiBytes(parts[2]));

    final long ts = Long.parseLong(new String(givenTimestamp));
    if ((System.currentTimeMillis()/1000 - ts) < timeoutSeconds) {
      final byte[] actualSignature = sign(givenValue, givenTimestamp);
      if (Arrays.equals(actualSignature, givenSignature)) {
        return givenValue;
      } else {
        throw new FailedSignatureValidationException();
      }
    } else {
      throw new SecureCookieTimeoutException();
    }
  }

  /**
   * Extracts the value of the cookie.
   *
   * @param cookie The cookie string
   * @param timeoutSeconds The maximum allowable age of the cookie, specified in seconds.
   * @return the original value
   * @throws SecureCookieTimeoutException if the cookie timestamp is older then than specified timeout value.
   * @throws FailedSignatureValidationException if the cookie failed signature validation. Note that the application should
   * occasionally expect this when keys are rotated.
   */
  public String extract(String cookie, int timeoutSeconds) {
    return utf8String(extractBytes(cookie, timeoutSeconds));
  }

  private static byte[] b64(byte[] value) {
    return Base64.encodeBase64(value);
  }

  private static byte[] unb64(byte[] value) {
    return Base64.decodeBase64(value);
  }

  private static String str(byte[] bytes) {
    return new String(bytes);
  }

  private static byte[] utf8Bytes(String value) {
    try {
      return value.getBytes(VALUE_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  private String utf8String(byte[] givenValue) {
    try {
      return new String(givenValue, VALUE_ENCODING);
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] asciiBytes(String value) {
    try {
      return value.getBytes("ASCII");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  private static byte[] currentTimeBytes() {
    return asciiBytes(Long.toString(System.currentTimeMillis() / 1000));
  }

  private byte[] sign(byte[]... parts) {
    try {
      final Mac mac = Mac.getInstance(HMAC_ALGORITHM);
      mac.init(signingKey);
      for (byte[] part : parts) {
        mac.update(part);
      }
      return mac.doFinal();
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeyException e) {
      throw new RuntimeException(e);
    }
  }
}