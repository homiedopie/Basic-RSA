package util;

import java.math.BigInteger;

public class BigIntegerUtil {

	public static BigInteger asBigInteger(String s) {
		byte[] bytes = s.getBytes();
		BigInteger m = new BigInteger(bytes);
		return m;
	}

}
