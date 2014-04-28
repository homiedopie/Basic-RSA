package core;

import java.math.BigInteger;
import java.util.Random;

/*
 * A class of static methods to implement the RSA encryption/decryption algorithm.
 * 
 * By Tylor Lake and Aaron Cohn
 */

public class Cryptography {
	
	public static RSAKeyPair generateRSAKeys()
	{
		//Select two prime numbers p and q.
		BigInteger p = BigInteger.probablePrime(32, new Random());
		System.out.println("p: " + p);
		BigInteger q = BigInteger.probablePrime(32, new Random());
		System.out.println("q: " + q);
		
		//Calculate n;
		BigInteger n = p.multiply(q);
		System.out.println("n: " + n);
		
		//Calculate phi(n). phiN = (p - 1) * (q - 1)
		BigInteger phiN = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
		System.out.println("phi(n): " + phiN);
		
		//Select relatively prime e.
		BigInteger e = relativePrimeTo(phiN);
		System.out.println("e: " + e);
		
		//Determine d
		BigInteger d = e.modInverse(phiN);
		System.out.println("d: " + d);
		
		//Create our keys
		Key pubkey = new Key(e, n);
		Key privkey = new Key(d, n);
		
		RSAKeyPair keys = new RSAKeyPair(pubkey, privkey);
		
		return keys;
	}

	private static BigInteger relativePrimeTo(BigInteger phiN) {
		BigInteger e = BigInteger.probablePrime(32, new Random());
		while(phiN.gcd(e).compareTo(BigInteger.ONE) != 0 || e.compareTo(phiN) > -1)
			e = BigInteger.probablePrime(32, new Random());
		return e;
	}
	
	public static BigInteger encrypt(BigInteger plainText, Key publicKey) {
		BigInteger e = publicKey.getFirst();
		BigInteger n = publicKey.getSecond();
		
		// C = M^e mod n
		return plainText.modPow(e, n);
	}
	
	public static BigInteger decrypt(BigInteger cipherText, Key privateKey) {
		BigInteger d = privateKey.getFirst();
		BigInteger n = privateKey.getSecond();
		
		return cipherText.modPow(d, n);
	}
}
