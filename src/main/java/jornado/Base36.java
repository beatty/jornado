package jornado;

/**
 * @author john
 */
public class Base36 {
	private static final int RADIX = 36;

	public static String encode(int value) {
		return Integer.toString(value, RADIX);
	}

	public static long decode(String base36String){
		return Integer.parseInt(base36String, RADIX);
	}
}