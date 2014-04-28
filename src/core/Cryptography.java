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
		BigInteger q = BigInteger.probablePrime(32, new Random());
		
		//Calculate n;
		BigInteger n = p.multiply(q);
		
		//Calculate phi(n). phiN = (p - 1) * (q - 1)
		BigInteger phiN = (p.subtract(BigInteger.ONE)).multiply((q.subtract(BigInteger.ONE)));
		
		//Select relatively prime e.
		BigInteger e = BigInteger.probablePrime(32, new Random());
		while(phiN.gcd(e).compareTo(BigInteger.ONE) != 0 || e.compareTo(phiN) > -1)
			e = BigInteger.probablePrime(32, new Random());
		
		//Determine d
		BigInteger d = e.modInverse(phiN);
		
		//Create our keys
		Key pubkey = new Key(e, n);
		Key privkey = new Key(d, n);
		
		RSAKeyPair keys = new RSAKeyPair(pubkey, privkey);
		
		return keys;
	}
}
