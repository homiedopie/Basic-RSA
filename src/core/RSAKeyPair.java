package core;

import util.Pair;

public class RSAKeyPair {

	private Pair<Key, Key> keyPair;
	
	public RSAKeyPair(Key first, Key second) {
		keyPair = new Pair<Key, Key>(first, second);
	}
	
	public Key getPublicKey() {
		return keyPair.getFirst();
	}
	
	public Key getPrivateKey() {
		return keyPair.getSecond();
	}
}
