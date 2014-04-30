package crypto;

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
		BigInteger e = relativePrimeTo(phiN);
		
		//Determine d
		BigInteger d = e.modInverse(phiN);
		
		//Create our keys
		Key pubkey = new Key(e, n);
		Key privkey = new Key(d, n);

		return new RSAKeyPair(pubkey, privkey);
	}

	private static BigInteger relativePrimeTo(BigInteger phiN) {
		BigInteger e = BigInteger.probablePrime(32, new Random());
		while(phiN.gcd(e).compareTo(BigInteger.ONE) != 0 || e.compareTo(phiN) > -1)
			e = BigInteger.probablePrime(32, new Random());
		return e;
	}
	
	public static BigInteger encrypt(String plainText, Key publicKey) {
		int totalBytes = plainText.length();
		int completeBlocks = totalBytes / 4;
		int bytesLeftOver = totalBytes % 4;
		BigInteger encryptedMessage = BigInteger.ZERO;
		for (int i = 0; i < completeBlocks; i++) {
			String fourByteString = plainText.substring(i * 4, i * 4 + 4);
			BigInteger msgBlock = asBigInteger(fourByteString);
			BigInteger cipherBlock = encryptBlock(msgBlock, publicKey);
			encryptedMessage = encryptedMessage.shiftLeft(64).add(cipherBlock);
		}
		if (bytesLeftOver > 0) {
			String lastFewChars = plainText.substring(completeBlocks * 4);
			BigInteger msgBlock = asBigInteger(lastFewChars);
			BigInteger cipherBlock = encryptBlock(msgBlock, publicKey);
			encryptedMessage = encryptedMessage.shiftLeft(64).add(cipherBlock);
		}		
		return encryptedMessage;
	}

	private static BigInteger asBigInteger(String s) {
		byte[] bytes = s.getBytes();
		BigInteger m = new BigInteger(bytes);
		return m;
	}
	
	public static String decrypt(BigInteger cipherText, Key privateKey) {
		String decryptedMessage = "";
		
		//Mask of 0xFFFFFFFFFFFFFFFF = 8 bytes
		BigInteger mask = new BigInteger("18446744073709551615");
		
		BigInteger lCipherText = cipherText;
		while(cipherText.compareTo(BigInteger.ZERO) != 0) {
			BigInteger cBlock = cipherText.and(mask);
			BigInteger dBlock = decryptBlock(cBlock, privateKey);
			byte[] bytes = dBlock.toByteArray();
			String mBlock = new String(bytes);
			decryptedMessage = mBlock + decryptedMessage;
			lCipherText = lCipherText.shiftRight(64);
		}
		
		return decryptedMessage;
	}
	
	static BigInteger encryptBlock(BigInteger plainText, Key publicKey) {
		BigInteger e = publicKey.getFirst();
		BigInteger n = publicKey.getSecond();
		
		// C = M^e mod n
		return plainText.modPow(e, n);
	}
	
	static BigInteger decryptBlock(BigInteger cipherText, Key privateKey) {
		BigInteger d = privateKey.getFirst();
		BigInteger n = privateKey.getSecond();
		
		return cipherText.modPow(d, n);
	}
}
