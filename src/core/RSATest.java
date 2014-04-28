package core;

import java.math.BigInteger;
import java.util.Random;

/*
 * A program that allows you to encrypt and decrypt messages with RSA.
 * 
 * By Tylor Lake and Aaron Cohn
 */

public class RSATest {

	public static void main(String[] args) {
		RSAKeyPair keys = Cryptography.generateRSAKeys();
		
		BigInteger plainText = BigInteger.probablePrime(32, new Random());
		System.out.println("Message: " + plainText);
		
		BigInteger cipherText = Cryptography.encrypt(plainText, keys.getPublicKey());
		System.out.println("Cipher: " + cipherText);
		
		BigInteger decodedCipher = Cryptography.decrypt(cipherText, keys.getPrivateKey());
		System.out.println("Original text: " + decodedCipher);
	}
}
