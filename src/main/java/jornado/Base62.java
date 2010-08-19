package jornado;

import com.google.common.base.Preconditions;

/**
 * Converts non-negative ints to/from base 62 strings.
 */
public class Base62 {
  private static final char[] CHARS = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

  public static String encode(int value) {
    Preconditions.checkArgument(value >= 0, "value must be non-negative");
    String tempVal = "";
    int mod;

    do {
      mod = value % 62;
      tempVal = CHARS[mod] + tempVal;
      value = value / 62;
    } while (value != 0);

    return tempVal;
  }

  public static int decode(String base62String) {
    Preconditions.checkNotNull(base62String);
    char[] chars = base62String.toCharArray();
    int value = 0;
    int multiplier = 1;
    int i = chars.length;

    while (i > 0) {
      char c = chars[--i];
      value = value + (indexOf(c) * multiplier);
      multiplier = multiplier * 62;
    }
    return value;
  }

  // not the fastest, but it's simple.
  private static int indexOf(char c) {
    for (int i=0; i<CHARS.length; i++) {
      if (CHARS[i] == c) return i;
    }
    throw new IllegalArgumentException("not a valid char: " + c);
  }
}
