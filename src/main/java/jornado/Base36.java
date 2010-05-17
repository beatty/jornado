package jornado;

/**
 * @author john
 */
public class Base36 {
	public static int radix = 36;

	public static String encode(long longValue){
		return Long.toString(longValue, radix);
	}

	public static long decode(String strValue){
		return Long.parseLong(strValue, radix);
	}

	public static void main(String[] args) {
		System.out.println(Base36.encode(1234l));
		System.out.println(Base36.decode(Base36.encode(1234l)));
	}
}