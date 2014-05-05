package crypto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;

/*
 * A program that allows you to encrypt and decrypt messages with RSA.
 * 
 * By Tylor Lake and Aaron Cohn
 */

public class RSATest {

	public static void test() {
		RSAKeyPair keys = Cryptography.generateRSAKeys();
		
		String msg = null;
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		try {
			msg = stdIn.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Message: " + msg);
		
		BigInteger cipherText = Cryptography.encrypt(msg, keys.getPublicKey());
		System.out.println("Cipher: " + cipherText);
		
		String decodedText = Cryptography.decrypt(cipherText, keys.getPrivateKey());
		System.out.println("Original text: " + decodedText);
	}
}
